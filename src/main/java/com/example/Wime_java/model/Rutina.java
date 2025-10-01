package com.example.Wime_java.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRutina;

    @Column(name = "IDusuarios", nullable = false)
    private Long idUsuario;

    @Column(name = "NombreRutina", nullable = false)
    private String nombreRutina;

    @Column(name = "Prioridad", nullable = false)
    private String prioridad;

    @Column(name = "FechaAsignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "FechaFin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "Frecuencia", nullable = false)
    private String frecuencia;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    private String estado = "pendiente";

    @Column(name = "Compartir_Con")
    private String compartirCon;
}
