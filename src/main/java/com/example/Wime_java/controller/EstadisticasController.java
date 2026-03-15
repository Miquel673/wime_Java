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

@GetMapping("/estadisticas-tablero")
public Map<String, Object> estadisticasTablero(HttpSession session) {

    Map<String, Object> response = new HashMap<>();

    Long idUsuario = (Long) session.getAttribute("id_usuario");

    if (idUsuario == null) {
        response.put("success", false);
        return response;
    }

    Long total = tareaRepository.countByIdUsuario(idUsuario);

    Long completadas = tareaRepository.countByIdUsuarioAndEstado(idUsuario, "completada");

    Long pendientes = tareaRepository.countByIdUsuarioAndEstado(idUsuario, "pendiente")
            + tareaRepository.countByIdUsuarioAndEstado(idUsuario, "en_progreso");

    Long vencidas = tareaRepository.countTareasVencidas(idUsuario);

    response.put("success", true);
    response.put("total", total);
    response.put("completadas", completadas);
    response.put("pendientes", pendientes);
    response.put("vencidas", vencidas);

    return response;
}

}
