// src/main/java/com/wime/service/LoginService.java
package com.wime.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.wime.model.usuario;
import com.wime.repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public LoginResponse login(String email, String contrasena) {
        Optional<usuario> usuarioOpt = usuarioRepo.findByEmailUsuario(email);

        if (usuarioOpt.isEmpty()) {
            return new LoginResponse(false, "❌ Usuario no encontrado.");
        }

        usuario usuario = usuarioOpt.get();

        // Verificar contraseña
        if (!BCrypt.checkpw(contrasena, usuario.getContrasena())) {
            return new LoginResponse(false, "❌ Contraseña incorrecta.");
        }

        // Verificar estado
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            return new LoginResponse(false, "Tu cuenta está inactiva. Contacta con soporte.", "Inactivo");
        }

        // Verificar inactividad (más de 60 días)
        LocalDateTime ultimoLogin = usuario.getUltimoLogin();
        if (ultimoLogin != null) {
            long diasInactivo = Duration.between(ultimoLogin, LocalDateTime.now()).toDays();
            if (diasInactivo > 60) {
                usuario.setEstado("Inactivo");
                usuarioRepo.save(usuario);
                return new LoginResponse(false, "❌ Tu cuenta ha sido desactivada por inactividad.", "Inactivo");
            }
        }

        // Actualizar último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepo.save(usuario);

        return new LoginResponse(true, "✅ Inicio de sesión exitoso.",
                usuario.getNombreUsuario(),
                usuario.getIdUsuario(),
                usuario.getTipo());
    }

    // Clase interna para la respuesta
    public static class LoginResponse {
        public boolean success;
        public String message;
        public String reason;
        public String nombreUsuario;
        public Long idUsuario;
        public String tipo;

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public LoginResponse(boolean success, String message, String reason) {
            this.success = success;
            this.message = message;
            this.reason = reason;
        }

        public LoginResponse(boolean success, String message, String nombreUsuario, Long idUsuario, String tipo) {
            this.success = success;
            this.message = message;
            this.nombreUsuario = nombreUsuario;
            this.idUsuario = idUsuario;
            this.tipo = tipo;
        }
    }
}
