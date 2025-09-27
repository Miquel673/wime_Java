package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class LoginApiController {

    private final UsuarioService usuarioService;

    public LoginApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String email,
                                     @RequestParam String contrasena,
                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioService.login(email, contrasena);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // ✅ Guardar en sesión
                session.setAttribute("usuario", usuario);
                session.setAttribute("id_usuario", usuario.getIdUsuario().longValue());
                session.setAttribute("rol", usuario.getTipo()); // 👈 Guardamos el tipo (ej: "admin" o "usuario")

                response.put("success", true);
                response.put("message", "✅ Login exitoso");
                response.put("usuario", usuario.getNombreUsuario());
                response.put("rol", usuario.getTipo()); // 👈 Devolvemos el rol
                response.put("idUsuario", usuario.getIdUsuario());

            } else {
                response.put("success", false);
                response.put("message", "❌ Usuario o contraseña incorrectos o cuenta inactiva.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "⚠️ Error interno en el servidor.");
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/check-session")
    public Map<String, Object> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            response.put("active", true);
            response.put("usuario", usuario.getNombreUsuario());
            response.put("rol", usuario.getTipo()); // 👈 También lo devolvemos aquí
        } else {
            response.put("active", false);
        }

        return response;
    }


@PostMapping("/logout")
public Map<String, Object> logout(HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    try {
        session.invalidate(); // ❌ Cierra la sesión completa
        response.put("success", true);
        response.put("message", "Sesión cerrada correctamente.");
    } catch (Exception e) {
        response.put("success", false);
        response.put("message", "Error al cerrar la sesión.");
        response.put("error", e.getMessage());
    }

    return response;
}


}
