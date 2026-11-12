package com.example.proyecto_final_poo.Models;

public class FacturaDetalle {
    private int id;
    private int idFactura;
    private int idRepuesto;
    private int cantidad;
    private double precioUnitario;

    public FacturaDetalle() {
    }

    public FacturaDetalle(int id, int idFactura, int idRepuesto, int cantidad, double precioUnitario) {
        this.id = id;
        this.idFactura = idFactura;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}