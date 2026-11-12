package com.example.proyecto_final_poo.Models;

import java.time.LocalDateTime;

public class HistorialEstado {
    private int id;
    private int idVehiculo;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;

    public HistorialEstado() {
    }

    public HistorialEstado(int id, int idVehiculo, String estadoAnterior,
                           String estadoNuevo, LocalDateTime fechaCambio) {
        this.id = id;
        this.idVehiculo = idVehiculo;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}