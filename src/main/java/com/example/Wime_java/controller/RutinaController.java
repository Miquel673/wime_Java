package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Eliminar rutina
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
