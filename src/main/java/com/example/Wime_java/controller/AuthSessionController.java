package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/google")
public class AuthSessionController {

    @GetMapping("/check-session")
    public Map<String, Object> checkSession(HttpSession session) {

        Map<String, Object> res = new HashMap<>();

        String usuario = (String) session.getAttribute("usuario_google");

        if (usuario == null) {
            res.put("active", false);
        } else {
            res.put("active", true);
            res.put("usuario", usuario);
            res.put("nombre", session.getAttribute("nombre_google"));
        }

        return res;
    }
}
