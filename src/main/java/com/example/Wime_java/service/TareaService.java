package com.example.Wime_java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.Wime_java.config.TareaConfig;
import com.example.Wime_java.dto.TareaTableroDTO;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.model.TareaCompartida;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.NotificacionRepository;
import com.example.Wime_java.repository.TareaCompartidaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.example.Wime_java.repository.UsuarioRepository;


@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final TareaCompartidaRepository tareaCompartidaRepository;
    private final TareaConfig tareaConfig;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;
    private final NotificacionRepository notificacionRepository;

    public TareaService(
        TareaRepository tareaRepository,
        TareaCompartidaRepository tareaCompartidaRepository,
        TareaConfig tareaConfig,
        UsuarioRepository usuarioRepository,
        NotificacionService notificacionService,
        NotificacionRepository notificacionRepository) {

    this.tareaRepository = tareaRepository;
    this.tareaCompartidaRepository = tareaCompartidaRepository;
    this.tareaConfig = tareaConfig;
    this.usuarioRepository = usuarioRepository;
    this.notificacionService = notificacionService;
    this.notificacionRepository = notificacionRepository;
}

        // ==========================================================
// 📌 LISTAR TAREAS PARA TABLERO (con info creador)
// ==========================================================
public List<TareaTableroDTO> obtenerTareasParaTablero(Long idUsuario) {

    List<TareaCompartida> relaciones =
            tareaCompartidaRepository.findByIdUsuario(idUsuario);


                Map<Long, TareaCompartida> relacionPorTarea = relaciones.stream()
            .collect(Collectors.toMap(
                    TareaCompartida::getIdTarea,
                    r -> r,
                    (a, b) -> a
            ));

    // Fallback para tareas existentes sin relación en tareas_usuarios
    // (por ejemplo, datos históricos/importados antes de crear el vínculo).
    List<Tarea> tareasPropias = tareaRepository.findByIdUsuario(idUsuario);
    for (Tarea tarea : tareasPropias) {
        relacionPorTarea.putIfAbsent(tarea.getIdTarea(), crearRelacionCreadorTemporal(tarea.getIdTarea(), idUsuario));
    }

    List<TareaTableroDTO> resultado = new java.util.ArrayList<>();

    for (TareaCompartida relacion : relacionPorTarea.values()) {

        Tarea tarea = tareaRepository
                .findById(relacion.getIdTarea())
                .orElse(null);

        if (tarea == null) continue;

        TareaTableroDTO dto = new TareaTableroDTO();

        dto.setIdTarea(tarea.getIdTarea());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaLimite(tarea.getFechaLimite());

        // 🔎 Buscar creador
        TareaCompartida creadorRelacion =
                tareaCompartidaRepository
                        .findByIdTarea(tarea.getIdTarea())
                        .stream()
                        .filter(r -> r.getRol() == TareaCompartida.Rol.CREADOR)
                        .findFirst()
                        .orElse(null);

        if (creadorRelacion != null) {

            Usuario creador = usuarioRepository
                .findById(creadorRelacion.getIdUsuario().intValue())
                .orElse(null);

            if (creador != null) {
                dto.setIdCreador(creador.getIdUsuario());
                dto.setNombreCreador(creador.getNombreUsuario());
                dto.setImagenPerfilCreador(creador.getFotoPerfil());       
             }
        }

        dto.setEsCompartida(
                relacion.getRol() == TareaCompartida.Rol.COMPARTIDA
        );

        resultado.add(dto);
    }

    return resultado;
}

private TareaCompartida crearRelacionCreadorTemporal(Long idTarea, Long idUsuario) {
    TareaCompartida relacion = new TareaCompartida();
    relacion.setIdTarea(idTarea);
    relacion.setIdUsuario(idUsuario);
    relacion.setRol(TareaCompartida.Rol.CREADOR);
    return relacion;
}

    // ==========================================================
    // 📌 LISTAR TAREAS DEL USUARIO
    // (propias + compartidas)
    // ==========================================================
    public List<Tarea> obtenerTareasPorUsuario(Long idUsuario) {

        // 1️⃣ Tareas creadas por el usuario
        List<Tarea> tareasPropias =
                tareaRepository.findByIdUsuario(idUsuario);

        // 2️⃣ IDs de tareas compartidas con el usuario
        List<Long> idsCompartidas =
                tareaCompartidaRepository.findIdsTareasCompartidas(idUsuario);

        // 3️⃣ Obtener las tareas compartidas reales
        List<Tarea> tareasCompartidas = idsCompartidas.isEmpty()
                ? List.of()
                : tareaRepository.findByIdTareaIn(idsCompartidas);

        // 4️⃣ Unificar ambas listas sin duplicados
        return Stream
                .concat(tareasPropias.stream(), tareasCompartidas.stream())
                .collect(Collectors.toMap(
                        Tarea::getIdTarea,
                        t -> t,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();
    }

    // ==========================================================
    // 📌 OBTENER TAREA POR ID
    // ==========================================================
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id);
    }

    // ==========================================================
    // 📌 GUARDAR TAREA
    // ==========================================================
    public Tarea guardarTarea(Tarea tarea) {

        if (tarea.getEstado() == null) {
            tarea.setEstado(tareaConfig.getEstadoPorDefecto());
        }

        if (!tareaConfig.estadoValido(tarea.getEstado())) {
            throw new IllegalArgumentException(
                    " Estado de tarea inválido");
        }

        return tareaRepository.save(tarea);
    }

    // ==========================================================
    // 📌 ELIMINAR TAREA
    // ==========================================================
    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    // ==========================================================
    // 📌 ACTUALIZAR ESTADO DE LA TAREA
    // ==========================================================
    public Tarea actualizarEstado(Long id, String nuevoEstado) {

        String estadoNormalizado = nuevoEstado
                .trim()
                .toUpperCase()
                .replace(" ", "_");

        if (!tareaConfig.estadoValido(estadoNormalizado)) {
            throw new IllegalArgumentException(
                    " Estado inválido: " + nuevoEstado);
        }

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                " No se encontró la tarea con ID: " + id));

        tarea.setEstado(estadoNormalizado);
        return tareaRepository.save(tarea);
    }

public void actualizarTareasVencidas(Long idUsuario) {

    LocalDate hoy = LocalDate.now();

    List<Long> idsTareas = tareaCompartidaRepository.findByIdUsuario(idUsuario)
            .stream()
            .map(TareaCompartida::getIdTarea)
            .distinct()
            .toList();

    List<Tarea> tareas = tareaRepository.findAllById(idsTareas);
    for (Tarea tarea : tareas) {

        if (tarea.getFechaLimite() == null) continue;

        boolean estaVencida = tarea.getFechaLimite().isBefore(hoy);

        boolean noEsCompletada =
                !"COMPLETADA".equalsIgnoreCase(tarea.getEstado());

        boolean noEsYaVencida =
                !"VENCIDA".equalsIgnoreCase(tarea.getEstado());

        if (estaVencida && noEsCompletada && noEsYaVencida) {

            tarea.setEstado("VENCIDA");
            tareaRepository.save(tarea);
            notificarVencida(tarea);
            continue;
        }

        if (!noEsCompletada || !noEsYaVencida) {
            continue;
        }

        if (tarea.getFechaLimite().isEqual(hoy) || tarea.getFechaLimite().isEqual(hoy.plusDays(1))) {
            notificarProximaAVencer(tarea);
        }
    }
}

private void notificarVencida(Tarea tarea) {
    String nombreCreador = obtenerNombreUsuario(tarea.getIdUsuario());
    List<TareaCompartida> participantes = tareaCompartidaRepository.findByIdTarea(tarea.getIdTarea());

    for (TareaCompartida relacion : participantes) {
        boolean esCompartida = !relacion.getIdUsuario().equals(tarea.getIdUsuario());
        String mensaje = esCompartida
                ? "La tarea compartida '" + tarea.getTitulo() + "' de " + nombreCreador + " cambió automáticamente a VENCIDA."
                : "La tarea '" + tarea.getTitulo() + "' cambió automáticamente a VENCIDA.";

        notificacionService.crearNotificacion(relacion.getIdUsuario(), "Tarea vencida", mensaje);
    }
}

private void notificarProximaAVencer(Tarea tarea) {
    String nombreCreador = obtenerNombreUsuario(tarea.getIdUsuario());
    List<TareaCompartida> participantes = tareaCompartidaRepository.findByIdTarea(tarea.getIdTarea());

    for (TareaCompartida relacion : participantes) {
        boolean esCompartida = !relacion.getIdUsuario().equals(tarea.getIdUsuario());
        String mensaje = esCompartida
                ? "La tarea compartida '" + tarea.getTitulo() + "' de " + nombreCreador + " está a punto de vencer (fecha límite: " + tarea.getFechaLimite() + ")."
                : "La tarea '" + tarea.getTitulo() + "' está a punto de vencer (fecha límite: " + tarea.getFechaLimite() + ").";

        if (!notificacionRepository.existsByIdUsuarioAndTipoAndMensaje(relacion.getIdUsuario(), "Tarea por vencer", mensaje)) {
            notificacionService.crearNotificacion(relacion.getIdUsuario(), "Tarea por vencer", mensaje);
        }
    }
}

private String obtenerNombreUsuario(Long idUsuario) {
    return usuarioRepository.findById(idUsuario.intValue())
            .map(Usuario::getNombreUsuario)
            .orElse("el creador");
}

}
