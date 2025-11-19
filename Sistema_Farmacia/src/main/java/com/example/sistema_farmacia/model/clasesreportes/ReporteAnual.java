package model.clasesreportes;

import model.clasesdata.VentasDB;


public class ReporteAnual extends model.clasesreportes.ReporteVentas {
    private int anio;

    // Constructor ReporteAnual(ventasDB : VentasDB, anio : int)
    public ReporteAnual(VentasDB ventasDB, int anio) {
        super(ventasDB);
        this.anio = anio;
    }

    @Override
    public String generarReporte() {
        return "Reporte Anual de Ventas: Año " + anio;
    }

    @Override
    public double sacarTotalVenta() {
        // Lógica para filtrar ventas por 'anio' y sumar el total
        return super.sacarTotalVenta();
    }

    @Override
    public double sacarTotalGanacia() {
        // Lógica para calcular la ganancia total del año
        return super.sacarTotalGanacia();
    }

    public void mostrarInfoVentas() {
        // Muestra la información de las ventas realizadas en el año
    }
}