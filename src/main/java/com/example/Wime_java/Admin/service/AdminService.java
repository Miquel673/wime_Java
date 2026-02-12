package com.example.Wime_java.Admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 🔹 Cambiar estado
    public void cambiarEstado(Integer id, String nuevoEstado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    // 🔹 Cambiar tipo
    public void cambiarTipo(Integer id, String nuevoTipo) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setTipo(nuevoTipo);
        usuarioRepository.save(usuario);
    }

    // 🔹 Eliminar usuario
    public void eliminarUsuario(Integer id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }

        usuarioRepository.deleteById(id);
    }
}
