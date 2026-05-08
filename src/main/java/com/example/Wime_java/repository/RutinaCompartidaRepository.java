package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Wime_java.model.RutinaCompartida;

public interface RutinaCompartidaRepository extends JpaRepository<RutinaCompartida, Long> {
    boolean existsByIdRutinaAndIdUsuario(Long idRutina, Long idUsuario);
    List<RutinaCompartida> findByIdRutina(Long idRutina);
    List<RutinaCompartida> findByIdUsuario(Long idUsuario);
}