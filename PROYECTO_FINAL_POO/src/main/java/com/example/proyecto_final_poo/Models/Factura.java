package com.example.proyecto_final_poo.Models;

import java.time.LocalDateTime;

public class Factura {
    private int id;
    private int idCliente;
    private int idVehiculo;
    private String tipoReparacion;
    private double manoObra;
    private double impuesto;
    private double montoTotal;
    private String metodoPago; // "EFECTIVO" o "TARJETA"
    private Integer idTarjeta; // null si el pago fue en efectivo
    private LocalDateTime fecha;

    public Factura() {
    }

    public Factura(int id, int idCliente, int idVehiculo, String tipoReparacion,
                   double manoObra, double impuesto, double montoTotal,
                   String metodoPago, Integer idTarjeta, LocalDateTime fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.tipoReparacion = tipoReparacion;
        this.manoObra = manoObra;
        this.impuesto = impuesto;
        this.montoTotal = montoTotal;
        this.metodoPago = metodoPago;
        this.idTarjeta = idTarjeta;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getTipoReparacion() { return tipoReparacion; }
    public void setTipoReparacion(String tipoReparacion) { this.tipoReparacion = tipoReparacion; }

    public double getManoObra() { return manoObra; }
    public void setManoObra(double manoObra) { this.manoObra = manoObra; }

    public double getImpuesto() { return impuesto; }
    public void setImpuesto(double impuesto) { this.impuesto = impuesto; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getIdTarjeta() { return idTarjeta; }
    public void setIdTarjeta(Integer idTarjeta) { this.idTarjeta = idTarjeta; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}