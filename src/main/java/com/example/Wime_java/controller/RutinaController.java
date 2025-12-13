package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.example.Wime_java.repository.NotificacionRepository;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.RutinaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private NotificacionService notificacionService;

    // ✅ Cambiar estado de rutina
    @PutMapping("/{id}/estado")
public ResponseEntity<Map<String, Object>> cambiarEstadoRutina(
        @PathVariable Long id,
        @RequestBody Map<String, String> body,
        HttpSession session) {

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "success", false,
            "message", "❌ Sesión no iniciada"
        ));
    }

    String nuevoEstado = body.get("estado");
    if (nuevoEstado == null || nuevoEstado.isBlank()) {
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "⚠️ Estado inválido"
        ));
    }

    Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
    if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "success", false,
            "message", "⚠️ Rutina no encontrada o no pertenece al usuario"
        ));
    }

    try {
        Rutina actualizada = rutinaService.actualizarEstado(id, nuevoEstado);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "✅ Estado actualizado correctamente",
            "estado", actualizada.getEstado()
        ));

    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(Map.of(
            "success", false,
            "message", "❌ Error al actualizar estado: " + e.getMessage()
        ));
    }
}


// ✅ Crear rutina con sesión
@PostMapping("/crear")
public ResponseEntity<?> crearRutina(@RequestBody Rutina rutina, HttpSession session) {
    try {
        // 1. Obtener el id del usuario logueado
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "No hay usuario logueado"
            ));
        }

        // 2. Asignar el usuario a la rutina
        rutina.setIdUsuario(idUsuario);

        // 3. Asignar valores por defecto
        if (rutina.getEstado() == null || rutina.getEstado().isBlank()) {
            rutina.setEstado("pendiente");
        }

        // 4. Guardar la rutina
        rutinaRepository.save(rutina);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Rutina creada correctamente"
        ));

    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of(
            "success", false,
            "message", "Error al crear la rutina: " + e.getMessage()
        ));
    }
}

    // ✅ Listar rutinas del usuario logueado
    @GetMapping("/listar")
    public Map<String, Object> listarRutinas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        List<Rutina> rutinas = rutinaRepository.findByIdUsuario(idUsuario);

        response.put("success", true);
        response.put("rutinas", rutinas);
        return response;
    }

    // ✅ Obtener rutina por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRutina(@PathVariable Long id, HttpSession session) {
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "❌ Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isPresent() && rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.ok(rutinaOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "⚠️ Rutina no encontrada"));
        }
    }

    // ✅ Editar rutina con notificación
    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, Object>> editarRutina(
            @PathVariable Long id,
            @RequestBody Rutina datosEditados,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "❌ Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "⚠️ Rutina no encontrada o no pertenece al usuario"));
        }

        Rutina rutina = rutinaOpt.get();
        rutina.setNombreRutina(datosEditados.getNombreRutina());
        rutina.setPrioridad(datosEditados.getPrioridad());
        rutina.setFrecuencia(datosEditados.getFrecuencia());
        rutina.setFechaFin(datosEditados.getFechaFin());
        rutina.setDescripcion(datosEditados.getDescripcion());
        rutina.setEstado(datosEditados.getEstado());

        rutinaRepository.save(rutina);

        // 🔔 Notificación
        String titulo = "Rutina modificada";
        String mensaje = "La rutina '" + rutina.getNombreRutina() + "' ha sido actualizada.";
        notificacionService.crearNotificacion(idUsuario, titulo, mensaje);

        response.put("success", true);
        response.put("message", "✅ Rutina actualizada con éxito");
        return ResponseEntity.ok(response);
    }

    // ✅ Eliminar rutina con notificación
    @DeleteMapping("/eliminar/{id}")
    public Map<String, Object> eliminarRutina(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isPresent() && rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            Rutina rutina = rutinaOpt.get();
            rutinaRepository.deleteById(id);

            // 🔔 Notificación al eliminar
            String titulo = "Rutina eliminada";
            String mensaje = "Se ha eliminado la rutina: " + rutina.getNombreRutina();
            notificacionService.crearNotificacion(idUsuario, titulo, mensaje);

            response.put("success", true);
            response.put("message", "🗑️ Rutina eliminada");
        } else {
            response.put("success", false);
            response.put("message", "⚠️ Rutina no encontrada o no pertenece al usuario");
        }

        return response;
    }
}
