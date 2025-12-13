package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Wime_java.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    Long countByIdUsuarioAndEstado(Long idUsuario, String estado); // 👈 nuevo

    public List<Tarea> findByIdUsuario(Long idUsuario); // 👈 trae todas las tareas de ese usuario
    List<Tarea> findByIdTareaIn(List<Long> ids);
}
