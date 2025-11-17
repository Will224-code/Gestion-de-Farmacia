package com.example.sistema_farmacia.model.clasesreportes;
import model.clasesdata.VentasDB;

public class ReporteMensual extends ReporteVentas {
    public ReporteMensual(VentasDB ventasDB, int anio, int mes) {
        super(ventasDB);
    }
}