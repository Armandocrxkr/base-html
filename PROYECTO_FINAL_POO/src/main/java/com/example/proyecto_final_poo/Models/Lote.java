package com.example.proyecto_final_poo.Models;

import java.time.LocalDate;

public class Lote {
    private int id;
    private int idProveedor;
    private LocalDate fecha;
    private String estado;

    public Lote() {
    }

    public Lote(int id, int idProveedor, LocalDate fecha, String estado) {
        this.id = id;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}