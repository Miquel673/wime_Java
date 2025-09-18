package com.example.Wime_java.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDusuario") // Usa el nombre exacto de la columna en tu base de datos
    private Integer idUsuario;

    @Column(name = "NombreUsuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "EmailUsuario", unique = true, nullable = false)
    private String emailUsuario;

    @Column(name = "ContrasenaUsuario", nullable = false)
    private String contrasenaUsuario; // Atributo en camelCase y anotación correcta

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro;

    @Column(name = "Edad")
    private int edad; // Atributo en camelCase
}