package com.example.Wime_java.config;

import java.util.List;

import org.springframework.stereotype.Component;

@Component // Singleton administrado por Spring
public class TareaConfig {

    private final List<String> estadosValidos = List.of(
        "PENDIENTE",
        "EN_PROGRESO",
        "COMPLETADA"
    );

    public List<String> getEstadosValidos() {
        return estadosValidos;
    }

    public String getEstadoPorDefecto() {
        return "PENDIENTE";
    }

    public boolean estadoValido(String estado) {
        return estadosValidos.contains(estado);
    }
}
