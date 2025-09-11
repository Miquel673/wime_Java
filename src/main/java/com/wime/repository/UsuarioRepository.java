package com.wime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wime.model.usuario;

public interface UsuarioRepository extends JpaRepository<usuario, Long> {
    // Buscar por email
    Optional<usuario> findByEmailUsuario(String emailUsuario);
}
