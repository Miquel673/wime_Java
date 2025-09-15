package com.wime.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wime.model.usuario;
import com.wime.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 Guardar un usuario con contraseña encriptada
    public usuario registrarUsuario(usuario usuario) {
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);
        return usuarioRepository.save(usuario);
    }

    // 🔹 Validar login
    public boolean validarLogin(String email, String contrasenaPlano) {
        Optional<usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(email);

        if (usuarioOpt.isPresent()) {
            usuario usuario = usuarioOpt.get();
            // Compara la contraseña en texto plano con la encriptada
            return passwordEncoder.matches(contrasenaPlano, usuario.getContrasena());
        }
        return false;
    }
}

