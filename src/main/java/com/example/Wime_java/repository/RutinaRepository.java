package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Wime_java.model.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    // 🔹 Obtener todas las rutinas de un usuario
    List<Rutina> findByIdUsuario(Long idUsuario);

    Long countByIdUsuario(Long idUsuario);

    // 🔹 Contar rutinas por usuario y estado
    Long countByIdUsuarioAndEstado(Long idUsuario, String estado);

    // 🔹 Obtener rutinas recientes (últimos 7 días) usando SQL nativo — compatible con Hibernate 6
    @Query(value = "SELECT * FROM rutinas WHERE IDusuarios = :idUsuario AND FechaFin >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)", nativeQuery = true)
    List<Rutina> findRutinasRecientes(@Param("idUsuario") Long idUsuario);
}