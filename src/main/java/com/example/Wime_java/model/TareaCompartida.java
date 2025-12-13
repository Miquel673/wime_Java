package com.example.Wime_java.model;

import java.time.LocalDateTime;

import com.example.Wime_java.model.TareaCompartida.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tareas_usuarios")
@Data
public class TareaCompartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_tarea", nullable = false)
    private Long idTarea;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

public enum Rol {
        CREADOR,
        COMPARTIDA
    }

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion = LocalDateTime.now();





}
