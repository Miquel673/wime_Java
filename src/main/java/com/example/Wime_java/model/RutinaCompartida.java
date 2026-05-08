package com.example.Wime_java.model;

import java.time.LocalDateTime;

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
@Table(name = "rutinas_usuarios")
@Data
public class RutinaCompartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_rutina", nullable = false)
    private Long idRutina;

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