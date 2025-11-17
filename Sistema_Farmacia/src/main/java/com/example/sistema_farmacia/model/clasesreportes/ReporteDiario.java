package com.example.sistema_farmacia.model.clasesreportes;
import com.example.sistema_farmacia.model.clasesdata.VentasDB;

import java.time.LocalDate;

public class ReporteDiario extends ReporteVentas {
    private LocalDate fecha;

    public ReporteDiario(VentasDB ventasDB, LocalDate fecha) {
        super(ventasDB);
        this.fecha = fecha;
        // Lógica de filtrado y cálculo para el día específico
    }

    @Override
    public String generarReporte() {
        return "Reporte Diario para la fecha: " + fecha;
    }
}