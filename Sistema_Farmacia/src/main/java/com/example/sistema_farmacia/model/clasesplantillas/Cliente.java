package com.example.sistema_farmacia.model.clasesplantillas;

import java.util.ArrayList;

public class Cliente {
    private String idCliente;
    private String nombre;
    private TipoCliente tipoCliente;
    private double porcentajeDescuento;
    private ArrayList<Venta> historialCompras;

    // Constructor
    public Cliente(String nom, String dir, String num, boolean esCli) {
        this.idCliente = generarID();
        this.nombre = nom;
        this.historialCompras = new ArrayList<>();
        // Inicialización básica
        this.tipoCliente = TipoCliente.NUEVO;
        this.porcentajeDescuento = 0.0;
    }

    // Lógica para generar ID
    public String generarID() {
        return "CLI-" + System.currentTimeMillis();
    }

    // Lógica para asignar tipo y porcentaje
    public void asignarTipoCliente(TipoCliente tipo) {
        this.tipoCliente = tipo;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}