package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recibo {
    private String idRecibo;
    private LocalDate fecha;
    private Venta venta;
    private double total;
    private double descuentoAplicado;
    private ArrayList<Producto> productos;

    // Constructor
    public Recibo(Venta venta) {
        this.venta = venta;
        this.idRecibo = generarIdRecibo();
        this.total = venta.getTotal();
        this.productos = venta.getVenta();
    }

    public void listarProductos() {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public String generarContenido() {
        return idRecibo;
    }

    public String generarIdRecibo() {
        return idRecibo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Venta getVenta() {
        return venta;
    }

    public double getTotal() {
        return total;
    }
}