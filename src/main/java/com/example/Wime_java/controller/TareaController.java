package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.TareaRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/tareas") // 👈 Ahora sí concuerda con tu JS
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    // ✅ Crear tarea
    @PostMapping("/crear")
    public Map<String, Object> crearTarea(
            @RequestParam String titulo,
            @RequestParam String prioridad,
            @RequestParam String fecha_limite,
            @RequestParam(required = false) String descripcion,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        // Validar sesión
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        // Validar campos obligatorios
        if (titulo == null || titulo.isEmpty() ||
            prioridad == null || prioridad.isEmpty() ||
            fecha_limite == null || fecha_limite.isEmpty()) {

            response.put("success", false);
            response.put("message", "⚠️ Faltan campos obligatorios");
            return response;
        }

        try {
            Tarea tarea = new Tarea();
            tarea.setIdUsuario(idUsuario);
            tarea.setTitulo(titulo.trim());
            tarea.setPrioridad(prioridad);
            tarea.setFechaLimite(java.time.LocalDate.parse(fecha_limite));
            tarea.setDescripcion(descripcion != null ? descripcion.trim() : "");
            tarea.setEstado("pendiente");

            tareaRepository.save(tarea);

            response.put("success", true);
            response.put("message", "✅ Tarea creada con éxito");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Error al guardar");
            response.put("error", e.getMessage());
        }

        return response;
    }

    // ✅ Listar tareas del usuario logueado
    @GetMapping("/listar")
    public Map<String, Object> listarTareas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long idUsuario = (Long) session.getAttribute("id_usuario"); // 👈 uniforme (Long)
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        List<Tarea> tareas = tareaRepository.findByIdUsuario(idUsuario);
        response.put("success", true);
        response.put("tareas", tareas);
        return response;
    }

    // ✅ Eliminar tarea
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
}
