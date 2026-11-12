package com.example.proyecto_final_poo.Models;

public class Repuesto {
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private int idProveedor;

    public Repuesto() {
    }

    public Repuesto(int id, String nombre, String categoria, double precio, int stock, int idProveedor) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.idProveedor = idProveedor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
}