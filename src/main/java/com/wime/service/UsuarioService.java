package com.wime.service;


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

    public usuario registrarUsuario(usuario usuario) {
        // encriptamos la contraseña antes de guardar
        usuario.setContrasenaUsuario(passwordEncoder.encode(usuario.getContrasenaUsuario()));
        return usuarioRepository.save(usuario);
    }

    public usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email).orElse(null);
    }
}
