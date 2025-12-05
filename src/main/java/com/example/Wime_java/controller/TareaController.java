package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


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

import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.TareaRepository;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.TareaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private EmailService emailService;


    // ✅ Cambiar estado de tarea
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoTarea(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long idUsuario = (Long) session.getAttribute("id_usuario");
            if (idUsuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "❌ Sesión no iniciada"
                ));
            }

            String nuevoEstado = body.get("estado");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "⚠️ No se proporcionó un estado válido"
                ));
            }

            Optional<Tarea> tareaOpt = tareaRepository.findById(id);
            if (tareaOpt.isEmpty() || !tareaOpt.get().getIdUsuario().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "⚠️ Tarea no encontrada o no pertenece al usuario"
                ));
            }

            Tarea tareaActualizada = tareaService.actualizarEstado(id, nuevoEstado);

            // 🔔 Notificación del cambio de estado
            notificacionService.crearNotificacion(
                    idUsuario,
                    "Tarea actualizada",
                    "La tarea '" + tareaActualizada.getTitulo() + "' cambió su estado a: " + nuevoEstado
            );

            response.put("success", true);
            response.put("message", "✅ Estado de la tarea actualizado correctamente");
            response.put("estado", nuevoEstado);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "❌ Error interno al actualizar el estado: " + e.getMessage()
            ));
        }
    }

@PostMapping("/crear")
public ResponseEntity<Map<String, Object>> guardarTarea(
        @RequestBody Map<String, Object> datos,
        HttpSession session) {

    Long idUsuario = (Long) session.getAttribute("id_usuario");

    if (idUsuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "❌ Sesión no iniciada"
        ));
    }

    Tarea tarea = new Tarea();
    tarea.setIdUsuario(idUsuario);
    tarea.setTitulo((String) datos.get("titulo"));
    tarea.setPrioridad((String) datos.get("prioridad"));
    tarea.setDescripcion((String) datos.get("descripcion"));
    tarea.setEstado("pendiente");

    // Fecha
    LocalDate fechaLimite = LocalDate.parse((String) datos.get("fechaLimite"));
    tarea.setFechaLimite(fechaLimite);

    try {
        // ----------------------------
        // 1️⃣ Obtener correos del front
        // ----------------------------
        String correosTexto = (String) datos.get("compartirCon"); // 👈 IMPORTANTE
        System.out.println("📌 Correos recibidos: " + correosTexto);

        List<String> destinatarios = new ArrayList<>();

        if (correosTexto != null && !correosTexto.isBlank()) {
            destinatarios = Arrays.stream(correosTexto.split(","))
                    .map(String::trim)
                    .filter(mail -> mail.contains("@"))
                    .toList();
        }

        System.out.println("📩 Correos detectados: " + destinatarios);

        // ----------------------------
        // 2️⃣ Guardar la tarea
        // ----------------------------
        tareaService.guardarTarea(tarea);

        // ----------------------------
        // 3️⃣ Notificación interna
        // ----------------------------
        notificacionService.crearNotificacion(
                idUsuario,
                "Nueva tarea creada",
                "Se ha creado la tarea: " + tarea.getTitulo()
        );

        // ----------------------------
        // 4️⃣ Envío de correos HTML
        // ----------------------------
        if (!destinatarios.isEmpty()) {
            String mensajeHTML = "El usuario ha compartido la tarea: " + tarea.getTitulo() + "\n" + "Descripción: " + tarea.getDescripcion()
                    + "\n" + "\nFecha límite: " + tarea.getFechaLimite().toString()
                    + "\n" + "\nPrioridad: " + tarea.getPrioridad();
            emailService.sendMassEmail(destinatarios, "Nueva tarea compartida", mensajeHTML);
        } else {
            System.out.println("📭 No se enviaron correos — lista vacía.");
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "✅ Tarea creada con éxito"
        ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "❌ Error al guardar la tarea: " + e.getMessage()
        ));
    }
}

    // ✅ Listar tareas del usuario logueado
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarTareas(HttpSession session) {
        Long idUsuario = (Long) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "❌ Sesión no iniciada"
            ));
        }

        List<Tarea> tareas = tareaService.obtenerTareasPorUsuario(idUsuario);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "tareas", tareas
        ));
    }

    // ✅ Obtener tarea por ID (para editar)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerTareaPorId(
            @PathVariable Long id,
            HttpSession session) {

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

        return ResponseEntity.ok(Map.of(
                "success", true,
                "tarea", tareaOpt.get()
        ));
    }

    // ✅ Editar tarea
    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, Object>> editarTarea(
            @PathVariable Long id,
            @RequestBody Tarea tareaActualizada,
            HttpSession session) {

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
        tarea.setTitulo(tareaActualizada.getTitulo());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setEstado(tareaActualizada.getEstado());
        tarea.setPrioridad(tareaActualizada.getPrioridad());
        tarea.setFechaLimite(tareaActualizada.getFechaLimite());

        tareaRepository.save(tarea);

        // 🔔 Notificación de edición
        notificacionService.crearNotificacion(
                idUsuario,
                "Tarea editada",
                "Se ha actualizado la tarea: " + tarea.getTitulo()
        );

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "✅ Tarea actualizada correctamente"
        ));
    }

    // ✅ Eliminar tarea
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarTarea(
            @PathVariable Long id,
            HttpSession session) {

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
        tareaService.eliminarTarea(id);

        // 🔔 Notificación de eliminación
        notificacionService.crearNotificacion(
                idUsuario,
                "Tarea eliminada",
                "Se ha eliminado la tarea: " + tarea.getTitulo()
        );

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "🗑️ Tarea eliminada correctamente"
        ));

        
    }
    
}
