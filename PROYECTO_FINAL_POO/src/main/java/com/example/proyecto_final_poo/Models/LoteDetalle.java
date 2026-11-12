package com.example.proyecto_final_poo.Models;

public class LoteDetalle {
    private int id;
    private int idLote;
    private int idRepuesto;
    private int cantidad;

    public LoteDetalle() {
    }

    public LoteDetalle(int id, int idLote, int idRepuesto, int cantidad) {
        this.id = id;
        this.idLote = idLote;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}