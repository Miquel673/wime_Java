package com.wime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wime.model.usuario;
import com.wime.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody usuario request) {
        usuario usuario = usuarioRepository.findByCorreoAndClave(
                request.getCorreo(), request.getClave()
        );

        if (usuario != null) {
            return ResponseEntity.ok("✅ Login exitoso, bienvenido " + usuario.getNombre());
        } else {
            return ResponseEntity.status(401).body("❌ Credenciales incorrectas");
        }
    }
}
