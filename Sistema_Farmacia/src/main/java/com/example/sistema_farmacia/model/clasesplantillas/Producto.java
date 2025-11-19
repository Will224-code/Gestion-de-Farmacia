package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Producto {
    private String nombre;
    private LocalDate fechaCaducidad;
    private double precioVenta;
    private int unidadesExi;
    private String codigo;
    private ArrayList<Categoria> categoria;

    // Constructor
    public Producto(String nombre, LocalDate fechaCaducidad, double precioVenta, int unidadesExi, String codigo, Categoria categoria) {
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.precioVenta = precioVenta;
        this.unidadesExi = unidadesExi;
        this.codigo = codigo;
        this.categoria = new ArrayList<>();
        this.categoria.add(categoria);
    }

    // Métodos esenciales
    public void agregarCategoria(Categoria categoria) {
        this.categoria.add(categoria);
    }

    public int getUnidadesExi() {
        return unidadesExi;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }
}