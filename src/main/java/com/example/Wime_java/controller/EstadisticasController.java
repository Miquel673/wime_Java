package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class EstadisticasController {

@Autowired private TareaRepository tareaRepository;
    @Autowired private RutinaRepository rutinaRepository;

    @GetMapping("/estadisticas-tablero")
    public Map<String, Object> estadisticasTablero(HttpSession session, @RequestParam(defaultValue = "todas") String tipo) {
        Map<String, Object> response = new HashMap<>();
        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            return response;
        }

        long total = 0, completadas = 0, pendientes = 0, vencidas = 0;
        String filtro = tipo == null ? "todas" : tipo.toLowerCase();

        if ("tarea".equals(filtro) || "todas".equals(filtro)) {
            total += tareaRepository.countByIdUsuario(idUsuario);
            completadas += tareaRepository.countByIdUsuarioAndEstado(idUsuario, "completada");
            pendientes += tareaRepository.countByIdUsuarioAndEstado(idUsuario, "pendiente") + tareaRepository.countByIdUsuarioAndEstado(idUsuario, "en_progreso");
            vencidas += tareaRepository.countTareasVencidas(idUsuario);
        }
        if ("rutina".equals(filtro) || "todas".equals(filtro)) {
            total += rutinaRepository.countByIdUsuario(idUsuario);
            completadas += rutinaRepository.countByIdUsuarioAndEstado(idUsuario, "completada");
            pendientes += rutinaRepository.countByIdUsuarioAndEstado(idUsuario, "pendiente") + rutinaRepository.countByIdUsuarioAndEstado(idUsuario, "en progreso");
        }

        response.put("success", true);
        response.put("total", total);
        response.put("completadas", completadas);
        response.put("pendientes", pendientes);
        response.put("vencidas", vencidas);
        return response;
    }

}
