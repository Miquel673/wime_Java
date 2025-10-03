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
@Table(name = "rutinas")
@Data
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRutina")
    private Long idRutina;

    @Column(name = "IDusuarios", nullable = false)
    private Long idUsuario;

    @Column(name = "NombreRutina", nullable = false, length = 200)
    private String nombreRutina;

    @Column(name = "FechaAsignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "FechaFin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "Fechacompletorutina", nullable = true)
    private LocalDate fechaCompletoRutina;

    @Column(name = "Prioridad", nullable = false, length = 50)
    private String prioridad;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "Estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "Frecuencia", length = 50)
    private String frecuencia;
}
