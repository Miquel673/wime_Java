package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.model.TareaCompartida;

public interface TareaCompartidaRepository
        extends JpaRepository<TareaCompartida, Long> {

    List<TareaCompartida> findByIdUsuario(Long idUsuario);

    boolean existsByIdTareaAndIdUsuario(Long idTarea, Long idUsuario);

    List<TareaCompartida> findByIdTarea(Long idTarea);

@Query("""
    SELECT tc.idTarea
    FROM TareaCompartida tc
    WHERE tc.idUsuario = :idUsuario
""")
List<Long> findIdsTareasCompartidas(@Param("idUsuario") Long idUsuario);


    
}
