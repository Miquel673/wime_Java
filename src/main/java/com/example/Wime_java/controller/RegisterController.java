package com.example.Wime_java.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class RegisterController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody Usuario usuario) {

        Map<String, Object> response = new HashMap<>();

        try {

            // 🔹 Validación básica
            if (usuario.getEmailUsuario() == null ||
                usuario.getNombreUsuario() == null ||
                usuario.getContrasenaUsuario() == null) {

                response.put("success", false);
                response.put("message", "Datos incompletos.");
                return ResponseEntity.badRequest().body(response);
            }

            // 🔹 Verificar si el correo ya existe
            if (usuarioRepository.existsByEmailUsuario(usuario.getEmailUsuario())) {
                response.put("success", false);
                response.put("message", "El correo ya está registrado.");
                return ResponseEntity.badRequest().body(response);
            }

            // 🔹 Encriptar contraseña
            usuario.setContrasenaUsuario(
                passwordEncoder.encode(usuario.getContrasenaUsuario())
            );

            // 🔹 Valores por defecto
            usuario.setTipo("usuario");
            usuario.setEstado("activo");
            usuario.setUltimoLogin(LocalDateTime.now());

            usuarioRepository.save(usuario);

            response.put("success", true);
            response.put("message", "Usuario registrado con éxito.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("success", false);
            response.put("message", "Error interno del servidor.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}