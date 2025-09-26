package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Wime_java.model.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    List<Rutina> findByIdUsuario(Long idUsuario);

}
