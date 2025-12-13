package com.example.Wime_java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Wime_java.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmailUsuario(String emailUsuario);
    boolean existsByEmailUsuario(String emailUsuario);
    List<Usuario> findByEmailUsuarioIn(List<String> emails);
}

