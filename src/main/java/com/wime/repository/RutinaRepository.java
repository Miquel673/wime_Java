package com.wime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wime.model.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    // Todas las rutinas de un usuario
    List<Rutina> findByIdUsuario(Long idUsuario);

    // Rutinas filtradas por estado
    List<Rutina> findByIdUsuarioAndEstado(Long idUsuario, String estado);
}
