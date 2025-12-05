package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/panel")
    public Map<String, Object> cargarPanel(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equalsIgnoreCase("Administrador")) {
            response.put("success", false);
            response.put("message", "❌ Acceso denegado");
            return response;
        }

        response.put("success", true);
        response.put("message", "✅ Bienvenido al panel de administración");
        return response;
    }
}
