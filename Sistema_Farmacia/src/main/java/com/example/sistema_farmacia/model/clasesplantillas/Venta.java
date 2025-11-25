package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idVenta;
    private LocalDate fechaVenta;
    private Cliente cliente;
    private String descripcion;
    private double total;              // Total con descuento
    private boolean requiereReceta;
    private Recibo recibo;
    private ArrayList<Producto> venta; // Lista de productos vendidos

    // Constructor
    public Venta(Cliente cliente) {
        this.cliente = cliente;
        this.idVenta = generarIdVenta();
        this.fechaVenta = LocalDate.now();
        this.venta = new ArrayList<>();
        this.total = 0.0;
        this.requiereReceta = false;
    }

    public String generarIdVenta() {
        return "VEN-" + System.currentTimeMillis();
    }

    // Agrega el producto a la lista (no recalcula automáticamente el total aquí)
    public void agregarProducto(Producto producto) {
        this.venta.add(producto);
    }

    // Calcula y retorna el total SIN descuento
    public double getTotalSinDescuento() {
        double subtotal = 0.0;
        for (Producto p : venta) {
            subtotal += p.getPrecioVenta();
        }
        return subtotal;
    }

    // Calcula el descuento que se debe aplicar usando el porcentaje del cliente sobre el subtotal
    public double getDescuentoAplicado() {
        return getTotalSinDescuento() * (cliente.getPorcentajeDescuento() / 100.0);
    }

    // Calcula y retorna el total final (con descuento)
    public double getTotal() {
        // Calcula el total cada vez, para estar seguros
        double subtotal = getTotalSinDescuento();
        double descuento = getDescuentoAplicado();
        this.total = subtotal - descuento;
        return this.total;
    }

    // Permite recalculo manual si lo deseas explícito
    public void recalcularTotal() {
        this.total = getTotal();
    }

    // --- Métodos accesorios y mutadores ---
    public String getIdVenta() { return idVenta; }
    public LocalDate getFechaVenta() { return fechaVenta; }
    public boolean isRequiereReceta() { return requiereReceta; }
    public Cliente getCliente() { return cliente; }
    public String getDescripcion() { return descripcion; }
    // getTotal() -- total con descuento ya está arriba
    public ArrayList<Producto> getVenta() { return venta; }
    public Recibo getRecibo() { return recibo; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setRequiereReceta(boolean requiereReceta) { this.requiereReceta = requiereReceta; }
}
