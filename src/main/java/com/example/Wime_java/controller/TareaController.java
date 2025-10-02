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

import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.NotificacionRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.example.Wime_java.service.TareaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/tareas") // 👈 Ahora sí concuerda con tu JS
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }
    

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    // ✅ Cambiar estado de tarea
    @PutMapping("/{id}/estado")
    public Map<String, Object> cambiarEstadoTarea(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        // 1. Validar sesión
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            response.put("success", false);
            response.put("message", "⚠️ Faltan datos");
            return response;
        }

        // 2. Buscar la tarea
        Optional<Tarea> tareaOpt = tareaRepository.findById(id);
        if (tareaOpt.isEmpty() || !tareaOpt.get().getIdUsuario().equals(idUsuario)) {
            response.put("success", false);
            response.put("message", "⚠️ Tarea no encontrada o no pertenece al usuario");
            return response;
        }

        // 3. Actualizar estado
        Tarea tarea = tareaOpt.get();
        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);

        // 4. Guardar notificación
        Notificacion notif = new Notificacion();
        notif.setIdUsuario(idUsuario);
        notif.setTipo("tarea");
        notif.setMensaje("Se ha cambiado el estado de una Tarea.");
        notificacionRepository.save(notif);

        // 5. Respuesta
        response.put("success", true);
        response.put("message", "✅ Estado de la tarea actualizado");
        return response;
    }


    // ✅ Crear tarea
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> guardarTarea(
            @RequestBody Tarea tarea,
            HttpSession session) {

        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "❌ Sesión no iniciada"
            ));
        }

        tarea.setIdUsuario(idUsuario);
        tarea.setEstado("pendiente");

        try {
            tareaService.guardarTarea(tarea);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "✅ Tarea creada con éxito"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "❌ Error al guardar"
            ));
        }
    }


    // ✅ Listar tareas del usuario logueado// ✅ Listar tareas del usuario logueado
@GetMapping("/listar")
public Map<String, Object> listarTareas(HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        response.put("success", false);
        response.put("message", "❌ Sesión no iniciada");
        return response;
    }

    // 👇 Ahora sí usamos el método correcto
    List<Tarea> tareas = tareaRepository.findByIdUsuario(idUsuario);

    response.put("success", true);
    response.put("tareas", tareas);
    return response;
}


@DeleteMapping("/eliminar/{id}")
public Map<String, Object> eliminarTarea(@PathVariable Long id, HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        response.put("success", false);
        response.put("message", "❌ Sesión no iniciada");
        return response;
    }

    Optional<Tarea> tareaOpt = tareaRepository.findById(id);
    if (tareaOpt.isPresent() && tareaOpt.get().getIdUsuario().equals(idUsuario)) {
        tareaRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "🗑️ Tarea eliminada");
    } else {
        response.put("success", false);
        response.put("message", "⚠️ Tarea no encontrada o no pertenece al usuario");
    }

    return response;
}
    
// ✅ Obtener tarea por ID
@GetMapping("/{id}")
public ResponseEntity<?> obtenerTarea(@PathVariable Long id, HttpSession session) {
    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "❌ Sesión no iniciada"));
    }

    Optional<Tarea> tareaOpt = tareaRepository.findById(id);
    if (tareaOpt.isPresent() && tareaOpt.get().getIdUsuario().equals(idUsuario)) {
        return ResponseEntity.ok(tareaOpt.get());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "⚠️ Tarea no encontrada"));
    }
}

// ✅ Editar tarea
@PutMapping("/editar/{id}")
public ResponseEntity<Map<String, Object>> editarTarea(
        @PathVariable Long id,
        @RequestBody Tarea datosEditados,
        HttpSession session) {

    Map<String, Object> response = new HashMap<>();

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "❌ Sesión no iniciada"
        ));
    }

    Optional<Tarea> tareaOpt = tareaRepository.findById(id);
    if (tareaOpt.isEmpty() || !tareaOpt.get().getIdUsuario().equals(idUsuario)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", "⚠️ Tarea no encontrada o no pertenece al usuario"
        ));
    }

    Tarea tarea = tareaOpt.get();
    tarea.setTitulo(datosEditados.getTitulo());
    tarea.setPrioridad(datosEditados.getPrioridad());
    tarea.setFechaLimite(datosEditados.getFechaLimite());
    tarea.setDescripcion(datosEditados.getDescripcion());
    tarea.setEstado(datosEditados.getEstado());

    tareaRepository.save(tarea);

    response.put("success", true);
    response.put("message", "✅ Tarea actualizada con éxito");
    return ResponseEntity.ok(response);
}





}
