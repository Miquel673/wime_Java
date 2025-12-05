package com.example.Wime_java.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class RegisterController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 🔹 Registro de usuario
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validar si el email ya existe
            if (usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario()).isPresent()) {
                response.put("success", false);
                response.put("message", "El correo ya está registrado.");
                return ResponseEntity.badRequest().body(response);
            }

            // Encriptar contraseña
            usuario.setContrasenaUsuario(passwordEncoder.encode(usuario.getContrasenaUsuario()));

            // Asignar valores por defecto
            usuario.setTipo("usuario"); // o "admin" si es necesario
            usuario.setEstado("activo");
            usuario.setUltimoLogin(LocalDateTime.now());

            // Guardar en la BD
            usuarioRepository.save(usuario);

            response.put("success", true);
            response.put("message", "Usuario registrado con éxito.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno en el servidor.");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
