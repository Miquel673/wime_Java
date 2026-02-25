package com.example.Wime_java.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaTableroDTO {

    // =========================
    // 🔹 Datos de la tarea
    // =========================
    private Long idTarea;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private LocalDate fechaLimite;

    // =========================
    // 🔹 Información de compartición
    // =========================
    private boolean esCompartida;   // true si el usuario NO es el creador

    // =========================
    // 🔹 Datos del creador
    // =========================
    private Long idCreador;
    private String nombreCreador;
    private String imagenPerfilCreador;

}