package model.clasesreportes;

import model.clasesdata.VentasDB;

public class ReporteMensual extends model.clasesreportes.ReporteVentas {
    private int anio;
    private int mes;

    // Constructor ReporteMensual(ventasDB : VentasDB, anio : int, mes : int)
    public ReporteMensual(VentasDB ventasDB, int anio, int mes) {
        super(ventasDB);
        this.anio = anio;
        this.mes = mes;
        // La inicialización del total y la ganancia se realiza en la implementación.
    }

    @Override
    public String generarReporte() {
        return "Reporte Mensual de Ventas: Mes " + mes + " del Año " + anio;
    }

    @Override
    public double sacarTotalVenta() {
        return super.sacarTotalVenta();
    }

    @Override
    public double sacarTotalGanacia() {
        return super.sacarTotalGanacia();
    }

    public void mostrarInfoVentas() {
        // Muestra la lista de ventas realizadas durante el mes
    }
}