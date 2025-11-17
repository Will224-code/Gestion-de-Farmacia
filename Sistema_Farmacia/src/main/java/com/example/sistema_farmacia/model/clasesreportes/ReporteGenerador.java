package com.example.sistema_farmacia.model.clasesreportes;

import com.example.sistema_farmacia.model.clasesdata.VentasDB;
import com.example.sistema_farmacia.model.clasesdata.ProductosDB;
import java.time.LocalDate;

public class ReporteGenerador {
    private VentasDB ventasDB;
    private ProductosDB productosDB;

    // Constructor que recibe las bases de datos necesarias
    public ReporteGenerador(VentasDB ventas, ProductosDB productos) {
        this.ventasDB = ventas;
        this.productosDB = productos;
    }

    // Método para generar un reporte diario
    public ReporteDiario generarReporteVentasDiario(LocalDate fecha) {
        return new ReporteDiario(ventasDB, fecha);
    }

    // Método para generar un reporte de inventario
    public ReporteInventario generarReporteInventario() {
        // Asumiendo que existe una clase ReporteInventario que usa ProductosDB
        return new ReporteInventario(productosDB);
    }
}