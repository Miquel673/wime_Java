package com.example.Wime_java.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.RutinaCompartida;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.RutinaCompartidaRepository;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.UsuarioRepository;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.RutinaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired private RutinaRepository rutinaRepository;
    @Autowired private RutinaService rutinaService;
    @Autowired private NotificacionService notificacionService;
    @Autowired private RutinaCompartidaRepository rutinaCompartidaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private EmailService emailService;

    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoRutina(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", " Sesión no iniciada"));
        }

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Estado inválido"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Rutina no encontrada o no pertenece al usuario"));
        }

        Rutina actualizada = rutinaService.actualizarEstado(id, nuevoEstado);
        notificacionService.crearNotificacion(idUsuario, "Rutina actualizada", "La rutina '" + actualizada.getNombreRutina() + "' cambió su estado a: " + actualizada.getEstado());
        return ResponseEntity.ok(Map.of("success", true, "message", "Estado actualizado correctamente", "estado", actualizada.getEstado()));
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearRutina(@RequestBody Map<String, Object> datos, HttpSession session) {
        try {
            Long idUsuario = obtenerUsuarioSesion(session);
            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "No hay usuario logueado"));
            }

            Rutina rutina = construirRutinaDesdePayload(datos, idUsuario, null);
            Rutina guardada = rutinaService.guardarRutina(rutina);

            RutinaCompartida creador = new RutinaCompartida();
            creador.setIdRutina(guardada.getIdRutina());
            creador.setIdUsuario(idUsuario);
            creador.setRol(RutinaCompartida.Rol.CREADOR);
            rutinaCompartidaRepository.save(creador);

            compartirRutinaInternamente(guardada, obtenerEmailsPayload(datos), idUsuario);
            return ResponseEntity.ok(Map.of("success", true, "message", "Rutina creada correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error al crear la rutina: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    public Map<String, Object> listarRutinas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", " Sesión no iniciada");
            return response;
        }

        rutinaService.actualizarRutinasVencidas(idUsuario);

        
        List<Long> idsRutinas = rutinaCompartidaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(RutinaCompartida::getIdRutina)
                .distinct()
                .toList();

        List<Map<String, Object>> rutinasTablero = rutinaRepository.findAllById(idsRutinas)
                .stream()
                .map(rutina -> mapearRutinaTablero(rutina, idUsuario))
                .toList();

        response.put("success", true);
        response.put("rutinas", rutinasTablero);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRutina(@PathVariable Long id, HttpSession session) {
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", " Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isPresent() && rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.ok(Map.of("success", true, "rutina", rutinaOpt.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Rutina no encontrada"));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, Object>> editarRutina(@PathVariable Long id, @RequestBody Map<String, Object> datosEditados, HttpSession session) {
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", " Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Rutina no encontrada o no pertenece al usuario"));
        }

        try {
            Rutina rutinaActualizada = construirRutinaDesdePayload(datosEditados, idUsuario, rutinaOpt.get());
            rutinaActualizada.setIdRutina(id);
            rutinaService.editarRutina(rutinaActualizada);
            return ResponseEntity.ok(Map.of("success", true, "message", "Rutina actualizada con éxito"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/compartir/{id}")
    public ResponseEntity<Map<String, Object>> compartirRutina(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", " Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Rutina no encontrada o no pertenece al usuario"));
        }

        int compartidas = compartirRutinaInternamente(rutinaOpt.get(), body.get("emails"), idUsuario);
        if (compartidas == 0) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No se pudo compartir la rutina"));
        }

        return ResponseEntity.ok(Map.of("success", true, "message", "Rutina compartida con " + compartidas + " usuario(s)"));
    }

    @DeleteMapping("/eliminar/{id}")
    public Map<String, Object> eliminarRutina(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", " Sesión no iniciada");
            return response;
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isPresent() && rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            rutinaService.eliminarRutina(id);
            response.put("success", true);
            response.put("message", "🗑️ Rutina eliminada");
        } else {
            response.put("success", false);
            response.put("message", "Rutina no encontrada o no pertenece al usuario");
        }
        return response;
    }

    @DeleteMapping("/remover-compartida/{idRutina}")
    public ResponseEntity<?> removerCompartida(@PathVariable Long idRutina, HttpSession session) {
        Long idUsuario = obtenerUsuarioSesion(session);
        if (idUsuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        RutinaCompartida relacion = rutinaCompartidaRepository.findByIdRutina(idRutina)
                .stream()
                .filter(r -> r.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElse(null);

        if (relacion == null || relacion.getRol() == RutinaCompartida.Rol.CREADOR) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        rutinaCompartidaRepository.delete(relacion);
        return ResponseEntity.ok(Map.of("success", true, "message", "Rutina eliminada de tu lista"));
    }

    private Long obtenerUsuarioSesion(HttpSession session) {
        return (Long) session.getAttribute("id_usuario");
    }

    private Rutina construirRutinaDesdePayload(Map<String, Object> datos, Long idUsuario, Rutina base) {
        Rutina rutina = base != null ? base : new Rutina();
        rutina.setIdUsuario(idUsuario);

        String nombreRutina = valorTexto(datos.get("nombreRutina"));
        String fechaAsignacionStr = valorTexto(datos.get("fechaAsignacion"));
        String fechaFinStr = valorTexto(datos.get("fechaFin"));
        String prioridad = normalizarPrioridad(valorTexto(datos.get("prioridad")));
        String frecuencia = normalizarFrecuencia(valorTexto(datos.get("frecuencia")));
        String descripcion = valorTexto(datos.get("descripcion"));
        String estado = normalizarEstado(valorTexto(datos.get("estado")));

        if (nombreRutina == null || nombreRutina.isBlank()) throw new IllegalArgumentException("El nombre de la rutina es obligatorio");
        if (fechaAsignacionStr == null || fechaFinStr == null) throw new IllegalArgumentException("Las fechas de inicio y finalización son obligatorias");

        LocalDate fechaAsignacion = LocalDate.parse(fechaAsignacionStr);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr);
        validarFechaNoAnterior(fechaAsignacion, "La fecha de inicio no puede ser anterior a la fecha actual");
        validarFechaNoAnterior(fechaFin, "La fecha final no puede ser anterior a la fecha actual");
        if (fechaFin.isBefore(fechaAsignacion)) throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha de inicio");

        rutina.setNombreRutina(nombreRutina);
        rutina.setDescripcion((descripcion == null || descripcion.isBlank()) ? null : descripcion);
        rutina.setPrioridad(prioridad);
        rutina.setFrecuencia(frecuencia);
        rutina.setFechaAsignacion(fechaAsignacion);
        rutina.setFechaFin(fechaFin);
        rutina.setEstado(estado == null ? "pendiente" : estado);
        return rutina;
    }

    private void validarFechaNoAnterior(LocalDate fecha, String mensaje) {
        if (fecha != null && fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(mensaje);
        }
    }


    private Map<String, Object> mapearRutinaTablero(Rutina rutina, Long idUsuarioSesion) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("idRutina", rutina.getIdRutina());
        dto.put("nombreRutina", rutina.getNombreRutina());
        dto.put("descripcion", rutina.getDescripcion());
        dto.put("fechaAsignacion", rutina.getFechaAsignacion());
        dto.put("fechaFin", rutina.getFechaFin());
        dto.put("prioridad", rutina.getPrioridad());
        dto.put("estado", rutina.getEstado());
        dto.put("frecuencia", rutina.getFrecuencia());
        dto.put("esCompartida", !rutina.getIdUsuario().equals(idUsuarioSesion));

        if (!rutina.getIdUsuario().equals(idUsuarioSesion)) {
            usuarioRepository.findById(rutina.getIdUsuario().intValue()).ifPresent(usuario -> {
                dto.put("nombreCreador", usuario.getNombreUsuario());
                dto.put("imagenPerfilCreador", usuario.getFotoPerfil());
            });
        }
        return dto;
    }

    private int compartirRutinaInternamente(Rutina rutina, String rawEmails, Long idUsuarioOrigen) {
        if (rawEmails == null || rawEmails.isBlank()) return 0;

        Set<String> emailsDestino = Arrays.stream(rawEmails.split(","))
                .map(String::trim)
                .filter(e -> !e.isBlank() && e.contains("@"))
                .collect(Collectors.toSet());

        if (emailsDestino.isEmpty()) return 0;

        List<Usuario> usuarios = usuarioRepository.findByEmailUsuarioIn(List.copyOf(emailsDestino));
        int compartidas = 0;

        for (Usuario usuarioDestino : usuarios) {
            Long idDestino = usuarioDestino.getIdUsuario();
            if (idDestino.equals(idUsuarioOrigen) || rutinaCompartidaRepository.existsByIdRutinaAndIdUsuario(rutina.getIdRutina(), idDestino)) {
                continue;
            }

            RutinaCompartida compartida = new RutinaCompartida();
            compartida.setIdRutina(rutina.getIdRutina());
            compartida.setIdUsuario(idDestino);
            compartida.setRol(RutinaCompartida.Rol.COMPARTIDA);
            rutinaCompartidaRepository.save(compartida);

            notificacionService.crearNotificacion(idDestino, "Nueva rutina compartida", "Se ha compartido contigo la rutina: " + rutina.getNombreRutina());
            try {
                emailService.sendMassEmail(List.of(usuarioDestino.getEmailUsuario()), "Te compartieron una rutina en WIME", "Hola " + usuarioDestino.getNombreUsuario() + ",\n\nTe compartieron la rutina: " + rutina.getNombreRutina());
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo enviar correo de rutina compartida: " + e.getMessage());
            }
            compartidas++;
        }

        return compartidas;
    }

    private String obtenerEmailsPayload(Map<String, Object> datos) {
        String emails = valorTexto(datos.get("emails"));
        if (emails == null || emails.isBlank()) {
            emails = valorTexto(datos.get("compartirCon"));
        }
        return emails;
    }

    private String valorTexto(Object valor) {
        return valor == null ? null : String.valueOf(valor).trim();
    }

    private String normalizarFrecuencia(String frecuencia) {
        if (frecuencia == null || frecuencia.isBlank()) return "diario";
        String valor = frecuencia.toLowerCase();
        return switch (valor) {
            case "diaria", "diario" -> "diario";
            case "semanal" -> "semanal";
            case "mensual" -> "mensual";
            default -> throw new IllegalArgumentException("Frecuencia inválida");
        };
    }

    private String normalizarPrioridad(String prioridad) {
        if (prioridad == null || prioridad.isBlank()) return "media";
        String valor = prioridad.toLowerCase();
        return switch (valor) {
            case "alta", "media", "baja" -> valor;
            case "rojo" -> "alta";
            case "amarillo" -> "media";
            case "verde" -> "baja";
            default -> throw new IllegalArgumentException("Prioridad inválida");
        };
    }

    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) return "pendiente";
        String valor = estado.toLowerCase().replace("_", " ");
        if (!List.of("pendiente", "en progreso", "completada").contains(valor)) {
            throw new IllegalArgumentException("Estado inválido");
        }
        return valor;
    }
}