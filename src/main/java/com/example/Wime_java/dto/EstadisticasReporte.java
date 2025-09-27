package com.example.Wime_java.dto;

public class EstadisticasReporte {
    private int tareasCompletadas;
    private int tareasEnProceso;
    private int tareasPendientes;

    private int rutinasCompletadas;
    private int rutinasEnProceso;
    private int rutinasPendientes;

    // === Getters y Setters ===
    public int getTareasCompletadas() { return tareasCompletadas; }
    public void setTareasCompletadas(int tareasCompletadas) { this.tareasCompletadas = tareasCompletadas; }

    public int getTareasEnProceso() { return tareasEnProceso; }
    public void setTareasEnProceso(int tareasEnProceso) { this.tareasEnProceso = tareasEnProceso; }

    public int getTareasPendientes() { return tareasPendientes; }
    public void setTareasPendientes(int tareasPendientes) { this.tareasPendientes = tareasPendientes; }

    public int getRutinasCompletadas() { return rutinasCompletadas; }
    public void setRutinasCompletadas(int rutinasCompletadas) { this.rutinasCompletadas = rutinasCompletadas; }

    public int getRutinasEnProceso() { return rutinasEnProceso; }
    public void setRutinasEnProceso(int rutinasEnProceso) { this.rutinasEnProceso = rutinasEnProceso; }

    public int getRutinasPendientes() { return rutinasPendientes; }
    public void setRutinasPendientes(int rutinasPendientes) { this.rutinasPendientes = rutinasPendientes; }
}
