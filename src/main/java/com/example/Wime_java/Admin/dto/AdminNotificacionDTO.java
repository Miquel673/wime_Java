package com.example.Wime_java.Admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminNotificacionDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private String tipo;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
}