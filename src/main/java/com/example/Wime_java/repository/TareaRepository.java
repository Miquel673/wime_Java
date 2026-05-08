package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Wime_java.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    Long countByIdUsuarioAndEstado(Long idUsuario, String estado); // 👈 nuevo
    Long countByIdUsuario(Long idUsuario);


    public List<Tarea> findByIdUsuario(Long idUsuario); // 👈 trae todas las tareas de ese usuario
    List<Tarea> findByIdTareaIn(List<Long> ids);

    @Query("""
SELECT COUNT(t) FROM Tarea t
WHERE t.idUsuario = :idUsuario
AND t.fechaLimite < CURRENT_DATE
AND t.estado <> 'completada'
""")
Long countTareasVencidas(Long idUsuario);

}
