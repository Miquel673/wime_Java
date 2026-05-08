package com.example.Wime_java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.RutinaCompartida;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.NotificacionRepository;
import com.example.Wime_java.repository.RutinaCompartidaRepository;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.UsuarioRepository;

@Service
public class RutinaService {

    private final RutinaRepository rutinaRepository;
    private final RutinaCompartidaRepository rutinaCompartidaRepository;
    private final NotificacionService notificacionService;
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public RutinaService(
            RutinaRepository rutinaRepository,
            RutinaCompartidaRepository rutinaCompartidaRepository,
            NotificacionService notificacionService,
            NotificacionRepository notificacionRepository,
            UsuarioRepository usuarioRepository) {
        this.rutinaRepository = rutinaRepository;
        this.rutinaCompartidaRepository = rutinaCompartidaRepository;
        this.notificacionService = notificacionService;
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Rutina guardarRutina(Rutina rutina) {
        if (rutina.getEstado() == null || rutina.getEstado().isBlank()) {
            rutina.setEstado("pendiente");
        }
        Rutina nuevaRutina = rutinaRepository.save(rutina);
        crearNotificacionSegura(rutina.getIdUsuario(), "Nueva rutina creada", "Se ha creado la rutina: " + rutina.getNombreRutina());
        return nuevaRutina;
    }

    public List<Rutina> listarPorUsuario(Long idUsuario) {
        return rutinaRepository.findByIdUsuario(idUsuario);
    }

    public Optional<Rutina> obtenerPorId(Long idRutina) {
        return rutinaRepository.findById(idRutina);
    }

    public Rutina editarRutina(Rutina rutina) {
        Rutina actualizada = rutinaRepository.save(rutina);
        crearNotificacionSegura(rutina.getIdUsuario(), "Rutina modificada", "La rutina '" + rutina.getNombreRutina() + "' ha sido actualizada.");
        return actualizada;
    }

    public void eliminarRutina(Long idRutina) {
        Optional<Rutina> rutinaOpt = rutinaRepository.findById(idRutina);
        if (rutinaOpt.isPresent()) {
            Rutina rutina = rutinaOpt.get();
            rutinaRepository.deleteById(idRutina);
            crearNotificacionSegura(rutina.getIdUsuario(), "Rutina eliminada", "Se ha eliminado la rutina: " + rutina.getNombreRutina());
        }
    }

    public Rutina actualizarEstado(Long id, String nuevoEstado) {
        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isEmpty()) throw new IllegalArgumentException("Rutina no encontrada");

        nuevoEstado = nuevoEstado.toLowerCase().replace("_", " ");
        if (!nuevoEstado.equals("pendiente") && !nuevoEstado.equals("en progreso") && !nuevoEstado.equals("completada")) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        Rutina rutina = rutinaOpt.get();
        rutina.setEstado(nuevoEstado);
        if ("completada".equals(nuevoEstado)) {
            rutina.setFechaCompletoRutina(LocalDate.now());
        }
        return rutinaRepository.save(rutina);
    }

    public void actualizarRutinasVencidas(Long idUsuario) {
        LocalDate hoy = LocalDate.now();
        List<Long> idsRutinas = rutinaCompartidaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(RutinaCompartida::getIdRutina)
                .distinct()
                .toList();
        List<Rutina> rutinas = rutinaRepository.findAllById(idsRutinas);

        for (Rutina rutina : rutinas) {
            if (rutina.getFechaFin() == null) continue;

            boolean estaVencida = rutina.getFechaFin().isBefore(hoy);
            boolean noEsCompletada = !"completada".equalsIgnoreCase(rutina.getEstado());
            boolean noEsYaVencida = !"vencida".equalsIgnoreCase(rutina.getEstado());

            if (estaVencida && noEsCompletada && noEsYaVencida) {
                rutina.setEstado("vencida");
                rutinaRepository.save(rutina);
                notificarRutinaVencida(rutina);
                continue;
            }

            if (!noEsCompletada || !noEsYaVencida) continue;

            if (rutina.getFechaFin().isEqual(hoy) || rutina.getFechaFin().isEqual(hoy.plusDays(1))) {
                notificarRutinaPorVencer(rutina);
            }
        }
    }

    private void notificarRutinaVencida(Rutina rutina) {
        String nombreCreador = obtenerNombreUsuario(rutina.getIdUsuario());
        List<RutinaCompartida> participantes = rutinaCompartidaRepository.findByIdRutina(rutina.getIdRutina());

        for (RutinaCompartida relacion : participantes) {
            boolean esCompartida = !relacion.getIdUsuario().equals(rutina.getIdUsuario());
            String mensaje = esCompartida
                    ? "La rutina compartida '" + rutina.getNombreRutina() + "' de " + nombreCreador + " cambió automáticamente a vencida."
                    : "La rutina '" + rutina.getNombreRutina() + "' cambió automáticamente a vencida.";
            crearNotificacionSegura(relacion.getIdUsuario(), "Rutina vencida", mensaje);
        }
    }

    private void notificarRutinaPorVencer(Rutina rutina) {
        String nombreCreador = obtenerNombreUsuario(rutina.getIdUsuario());
        List<RutinaCompartida> participantes = rutinaCompartidaRepository.findByIdRutina(rutina.getIdRutina());

        for (RutinaCompartida relacion : participantes) {
            boolean esCompartida = !relacion.getIdUsuario().equals(rutina.getIdUsuario());
            String mensaje = esCompartida
                    ? "La rutina compartida '" + rutina.getNombreRutina() + "' de " + nombreCreador + " está a punto de vencer (fecha fin: " + rutina.getFechaFin() + ")."
                    : "La rutina '" + rutina.getNombreRutina() + "' está a punto de vencer (fecha fin: " + rutina.getFechaFin() + ").";

            if (!notificacionRepository.existsByIdUsuarioAndTipoAndMensaje(relacion.getIdUsuario(), "Rutina por vencer", mensaje)) {
                crearNotificacionSegura(relacion.getIdUsuario(), "Rutina por vencer", mensaje);
            }
        }
    }

    private String obtenerNombreUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario.intValue())
                .map(Usuario::getNombreUsuario)
                .orElse("el creador");
    }

    private void crearNotificacionSegura(Long idUsuario, String tipo, String mensaje) {
        try {
            notificacionService.crearNotificacion(idUsuario, tipo, mensaje);
        } catch (Exception e) {
            System.err.println("Error al crear notificación de rutina: " + e.getMessage());
        }
    }
}