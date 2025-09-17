package com.wime.service;

import com.wime.model.Usuario;
import com.wime.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarLogin(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        // Compara la contraseña (en un entorno real, usar un hash)
        return usuario != null && usuario.getContrasenaUsuario().equals(contrasena);
    }
}