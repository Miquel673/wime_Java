package com.example.Wime_java.controller;

import java.util.HashMap;
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
import com.example.Wime_java.repository.RutinaRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaRepository rutinaRepository;

    // Listar rutinas del usuario logueado
    @GetMapping("/listar")
    public Map<String, Object> listarRutinas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return response;
        }

        response.put("success", true);
        response.put("rutinas", rutinaRepository.findByIdUsuario(idUsuario));
        return response;
    }


    // ✅ Crear rutina
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearRutina(@RequestBody Rutina rutina, HttpSession session) {
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "❌ Sesión no iniciada"
            ));
        }

        rutina.setIdUsuario(idUsuario);
        rutina.setEstado("pendiente");

        rutinaRepository.save(rutina);
        return ResponseEntity.ok(Map.of("success", true, "message", "✅ Rutina creada con éxito"));
    }

    // ✅ Obtener rutina por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRutina(@PathVariable Long id, HttpSession session) {
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "❌ Sesión no iniciada"));
        }

        Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
        if (rutinaOpt.isPresent() && rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.ok(rutinaOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "⚠️ Rutina no encontrada"));
        }
    }

@PutMapping("/editar/{id}")
public ResponseEntity<Map<String, Object>> editarRutina(
        @PathVariable Long id,
        @RequestBody Rutina datosEditados,
        HttpSession session) {

    Map<String, Object> response = new HashMap<>();

    // ⚠️ Validar sesión
    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "❌ Sesión no iniciada"
        ));
    }

    // Buscar rutina
    Optional<Rutina> rutinaOpt = rutinaRepository.findById(id);
    if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdUsuario().equals(idUsuario)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", "⚠️ Rutina no encontrada o no pertenece al usuario"
        ));
    }

    Rutina rutina = rutinaOpt.get();

    // Actualizar campos editables
    rutina.setNombreRutina(datosEditados.getNombreRutina());
    rutina.setPrioridad(datosEditados.getPrioridad());
    rutina.setFrecuencia(datosEditados.getFrecuencia());
    rutina.setFechaFin(datosEditados.getFechaFin());
    rutina.setDescripcion(datosEditados.getDescripcion());
    rutina.setEstado(datosEditados.getEstado());

    rutinaRepository.save(rutina);

    response.put("success", true);
    response.put("message", "✅ Rutina actualizada con éxito");
    return ResponseEntity.ok(response);
}

    // ✅ Eliminar rutina
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
            rutinaRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "🗑️ Rutina eliminada");
        } else {
            response.put("success", false);
            response.put("message", "⚠️ Rutina no encontrada o no pertenece al usuario");
        }

        return response;
    }
}
