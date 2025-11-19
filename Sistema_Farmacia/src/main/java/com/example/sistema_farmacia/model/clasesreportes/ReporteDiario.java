package model.clasesreportes;

import model.clasesdata.VentasDB;
import java.time.LocalDate;

public class ReporteDiario extends model.clasesreportes.ReporteVentas {
    private LocalDate fecha;

    public ReporteDiario(VentasDB ventasDB, LocalDate fecha) {
        super(ventasDB);
        this.fecha = fecha;
    }

    @Override
    public String generarReporte() {
        return "Reporte Diario de Ventas para la fecha: " + fecha.toString();
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
        // Implementación de la lógica de visualización
    }
}