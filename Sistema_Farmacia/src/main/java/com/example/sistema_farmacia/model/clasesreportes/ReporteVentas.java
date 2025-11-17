package com.example.sistema_farmacia.model.clasesreportes;

import model.clasesdata.VentasDB;
import com.example.sistema_farmacia.model.clasesplantillas.Venta;
import java.util.ArrayList;

public class ReporteVentas {
    protected VentasDB ventasDB;
    protected ArrayList<Venta> ventas;
    protected double totalVenta;

    // Constructor base
    public ReporteVentas(VentasDB ventasDB) {
        this.ventasDB = ventasDB;
        this.ventas = new ArrayList<>();
    }

    // Método principal que se sobrescribe en las clases hijas
    public String generarReporte() {
        return "Reporte base de ventas.";
    }

    public double sacarTotalVenta() {
        // Lógica de cálculo
        return totalVenta;
    }
}