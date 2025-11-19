package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recibo {
    private String idRecibo;
    private Venta venta;
    private double total;
    private ArrayList<Producto> productos;

    // Constructor
    public Recibo(Venta venta) {
        this.venta = venta;
        this.idRecibo = generarIdRecibo();
        this.total = venta.getTotal();
        this.productos = venta.getVenta();
    }

    // Lógica para generar ID del recibo
    private String generarIdRecibo() {
        return "REC-" + System.currentTimeMillis();
    }

    public String generarContenido() {
        // Lógica para formatear el contenido del recibo
        return "Contenido del recibo: ID " + idRecibo;
    }

    public Venta getVenta() {
        return venta;
    }
}