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
    private Long idUsuario;  // 🔥 cambiar de Integer a Long

    @Column(name = "NombreUsuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "EmailUsuario", nullable = false, unique = true, length = 150)
    private String emailUsuario;

    @Column(name = "ContrasenaUsuario", nullable = false, length = 255)
    private String contrasenaUsuario;

    /*@Column(name = "Edad")
    private Integer birthDay; // o int si era numérico/* */


    @Column(name = "Tipo", nullable = false, length = 50)
    private String tipo = "Corriente";

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = "Activo";

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

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
