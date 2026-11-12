package com.example.proyecto_final_poo.Models;


import java.time.LocalDateTime;

public class Tarjeta {
    private int id;
    private int idCliente;
    private String tipo; // "CREDITO" o "DEBITO"
    private String titular;
    private String ultimosDigitos;
    private LocalDateTime fechaRegistro;

    public Tarjeta() {
    }

    public Tarjeta(int id, int idCliente, String tipo, String titular, String ultimosDigitos, LocalDateTime fechaRegistro) {
        this.id = id;
        this.idCliente = idCliente;
        this.tipo = tipo;
        this.titular = titular;
        this.ultimosDigitos = ultimosDigitos;
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}