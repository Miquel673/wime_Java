package com.wime.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wime.model.usuario;
import com.wime.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 📌 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String contrasena,
            HttpSession session) {

        Optional<usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "❌ Usuario no encontrado"));
        }

        usuario usuario = usuarioOpt.get();

        // ⚠️ Por ahora usamos comparación directa (luego podemos usar BCrypt si las claves están encriptadas)
        if (!usuario.getContrasenaUsuario().equals(contrasena)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "❌ Contraseña incorrecta"));
        }

        // 🛑 Verificar estado
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "reason", "Inactivo", "message", "Tu cuenta está inactiva."));
        }

        // ⏱️ Verificar último login
        if (usuario.getUltimoLogin() != null) {
            LocalDateTime ahora = LocalDateTime.now();
            if (usuario.getUltimoLogin().plusDays(60).isBefore(ahora)) {
                usuario.setEstado("Inactivo");
                usuarioRepository.save(usuario);
                return ResponseEntity.badRequest().body(Map.of("success", false, "reason", "Inactivo", "message", "❌ Cuenta desactivada por inactividad."));
            }
        }

        // ✅ Guardar sesión
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
        session.setAttribute("tipo", usuario.getTipo());

        // 🔄 Actualizar último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // ✅ Respuesta exitosa
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("nombre", usuario.getNombreUsuario());
        response.put("tipo", usuario.getTipo());

        return ResponseEntity.ok(response);
    }

    // 📌 Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true, "message", "Sesión cerrada"));
    }
}
