// src/main/java/com/wime/repository/UsuarioRepository.java
package com.wime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wime.model.usuario;

public interface UsuarioRepository extends JpaRepository<usuario, Long> {
    Optional<usuario> findByEmailUsuario(String emailUsuario);
}
