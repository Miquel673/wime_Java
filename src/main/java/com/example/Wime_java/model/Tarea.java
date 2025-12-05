package com.example.Wime_java.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tareas")
@Data
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDtarea")
    private Long idTarea;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario; // 👈 cambiado a Long

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "prioridad", nullable = false, length = 50)
    private String prioridad;

    @Column(name = "fecha_limite", nullable = true)
    private LocalDate fechaLimite;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;
}
