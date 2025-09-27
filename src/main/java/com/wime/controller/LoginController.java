// src/main/java/com/wime/controller/LoginController.java
package com.wime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wime.service.LoginService;
import com.wime.service.LoginService.LoginResponse;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public LoginResponse login(@RequestParam String email,@RequestParam String contrasena,HttpSession session) {

        LoginResponse response = loginService.login(email, contrasena);

        if (response.success) {
            // Guardar en sesión
            session.setAttribute("usuario", response.nombreUsuario);
            session.setAttribute("id_usuario", response.idUsuario);
            session.setAttribute("tipo", response.tipo);
        }

        return response;
    }
}
