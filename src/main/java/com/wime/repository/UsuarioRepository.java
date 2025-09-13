// src/main/java/com/wime/repository/UsuarioRepository.java
package com.wime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wime.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailUsuario(String email);
}
