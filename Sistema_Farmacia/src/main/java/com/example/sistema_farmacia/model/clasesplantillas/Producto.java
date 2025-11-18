package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Producto {
    private String nombre;
    private String descripcion;
    private LocalDate fechaCaducidad;
    private double precio;
    private double precioVenta;
    private int unidadesExi;
    private String codigo;
    private ArrayList<Categoria> categorias;

    public Producto(String nombre, String descripcion, LocalDate fechaCad, double precio, double precioVenta, int unidadesExi, String codigo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCaducidad = fechaCad;
        this.precio = precio;
        this.precioVenta = precioVenta;
        this.unidadesExi = unidadesExi;
        this.codigo = codigo;
        this.categorias = new ArrayList<>();
    }

    public void agregarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void eliminarCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getUnidadesExi() {
        return unidadesExi;
    }

    public void setUnidadesExi(int unidadesExi) {
        this.unidadesExi = unidadesExi;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCaducidad=" + fechaCaducidad +
                ", precio=" + precio +
                ", precioVenta=" + precioVenta +
                ", unidadesExi=" + unidadesExi +
                ", codigo='" + codigo + '\'' +
                ", categorias=" + categorias +
                '}';
    }
}