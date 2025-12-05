package com.example.Wime_java.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDnotificacion")
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "leida", nullable = false)
    private boolean leida = false;

        // ✅ Constructor vacío
    public Notificacion() {
        this.fecha = LocalDateTime.now(); // <-- AQUI LA MAGIA
        this.leida = false;
    }

    // ✅ Constructor completo (si lo tienes)
    public Notificacion(Long idUsuario, String titulo, String mensaje) {
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public String getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public boolean isLeida() { return leida; }

    public void setId(Long id) { this.id = id; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setLeida(boolean leida) { this.leida = leida; }
}
