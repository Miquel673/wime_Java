package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class EstadisticasController {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @GetMapping("/estadisticas")
    public Map<String, Object> obtenerEstadisticas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long idUsuario = (Long) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "⚠️ Sesión no iniciada");
            return response;
        }

        Long tareasCompletadas = tareaRepository.countByIdUsuarioAndEstado(idUsuario, "completada");
        Long rutinasFinalizadas = rutinaRepository.countByIdUsuarioAndEstado(idUsuario, "completada");

        Long enProceso = tareaRepository.countByIdUsuarioAndEstado(idUsuario, "en progreso")
                + rutinaRepository.countByIdUsuarioAndEstado(idUsuario, "en progreso");

        response.put("success", true);
        response.put("tareas_completadas", tareasCompletadas);
        response.put("rutinas_finalizadas", rutinasFinalizadas);
        response.put("en_proceso", enProceso);

        return response;
    }
}
