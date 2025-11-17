package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idVenta;
    private LocalDate fechaVenta;
    private Cliente cliente;
    private double total;
    private ArrayList<Producto> venta;

    // Constructor
    public Venta(Cliente cliente) {
        this.cliente = cliente;
        this.idVenta = generarIdVenta();
        this.fechaVenta = LocalDate.now();
        this.venta = new ArrayList<>();
        this.total = 0.0;
    }

    // Lógica para agregar productos y calcular el total
    public void agregarProducto(Producto producto) {
        this.venta.add(producto);
        this.total = calcularTotal();
    }

    public double calcularTotal() {
        // Lógica de cálculo y aplicación de descuento
        return 0.0;
    }

    public Recibo generarRecibo() {
        return new Recibo(this);
    }

    // Getters esenciales para el recibo
    public double getTotal() {
        return total;
    }

    public ArrayList<Producto> getVenta() {
        return venta;
    }
}