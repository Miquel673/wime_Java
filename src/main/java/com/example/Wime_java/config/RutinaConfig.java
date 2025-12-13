package com.example.Wime_java.config;

import java.util.Set;

public class RutinaConfig {

    private static RutinaConfig instancia;

    private final Set<String> estadosValidos;

    private RutinaConfig() {
        this.estadosValidos = Set.of(
            "PENDIENTE",
            "EN_PROGRESO",
            "COMPLETADA"
        );
    }

    public static RutinaConfig getInstancia() {
        if (instancia == null) {
            instancia = new RutinaConfig();
        }
        return instancia;
    }

    public boolean estadoValido(String estado) {
        return estadosValidos.contains(estado);
    }
}
