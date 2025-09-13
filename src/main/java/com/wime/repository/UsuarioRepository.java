package com.wime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wime.model.usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<usuario, Long> {
    usuario findByCorreoAndClave(String correo, String clave);
}
