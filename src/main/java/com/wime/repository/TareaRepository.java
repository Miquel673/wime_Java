package com.wime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wime.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Todas las tareas de un usuario
    List<Tarea> findByIdUsuario(Long idUsuario);

    // Tareas filtradas por estado
    List<Tarea> findByIdUsuarioAndEstado(Long idUsuario, String estado);
}
