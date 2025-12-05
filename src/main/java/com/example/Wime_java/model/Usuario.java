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
    @Column(name = "IDusuario")
    private Integer idUsuario;

    @Column(name = "NombreUsuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "EmailUsuario", nullable = false, unique = true, length = 150)
    private String emailUsuario;

    @Column(name = "ContrasenaUsuario", nullable = false, length = 255)
    private String contrasenaUsuario;

    @Column(name = "Tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    // Guarda la URL relativa que usará el frontend (ej: "/uploads/fotos_perfil/usuario_20_...jpg")
    @Column(name = "foto_perfil")
    private String fotoPerfil;

    public Usuario() {}

    public Usuario(String nombreUsuario, String emailUsuario, String contrasenaUsuario, String tipo, String estado, LocalDateTime ultimoLogin) {
        this.nombreUsuario = nombreUsuario;
        this.emailUsuario = emailUsuario;
        this.contrasenaUsuario = contrasenaUsuario;
        this.tipo = tipo;
        this.estado = estado;
        this.ultimoLogin = ultimoLogin;
    }


    // 🔹 Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    

    // 🔹 toString()
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", ultimoLogin=" + ultimoLogin +
                '}';
    }
}
