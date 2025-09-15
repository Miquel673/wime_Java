// src/main/java/com/wime/controller/AuthController.java
package com.wime.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wime.model.usuario;
import com.wime.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTO para el login
    public static class LoginRequest {
        private String emailUsuario;
        private String contrasena;

        public String getEmailUsuario() {
            return emailUsuario;
        }

        public void setEmailUsuario(String emailUsuario) {
            this.emailUsuario = emailUsuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

    // DTO para la respuesta
    public static class LoginResponse {
        private String mensaje;
        private String nombreUsuario;

        public LoginResponse(String mensaje, String nombreUsuario) {
            this.mensaje = mensaje;
            this.nombreUsuario = nombreUsuario;
        }

        public String getMensaje() {
            return mensaje;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Optional<usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(request.getEmailUsuario());

        if (usuarioOpt.isPresent()) {
            usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
                usuario.setUltimoLogin(LocalDateTime.now());
                usuarioRepository.save(usuario);

                return new LoginResponse("Login exitoso", usuario.getNombreUsuario());
            } else {
                return new LoginResponse("Contraseña incorrecta", null);
            }
        } else {
            return new LoginResponse("Usuario no encontrado", null);
        }
    }
}
