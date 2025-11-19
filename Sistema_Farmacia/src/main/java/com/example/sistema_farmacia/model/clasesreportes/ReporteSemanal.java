package model.clasesreportes;

import model.clasesdata.VentasDB;
import java.time.LocalDate;


public class ReporteSemanal extends model.clasesreportes.ReporteVentas {
    // Atributo privado según el UML
    private LocalDate fechaInicio;

    // Constructor ReporteSemanal(ventasDB : VentasDB, ffecha : LocalDate)
    public ReporteSemanal(VentasDB ventasDB, LocalDate ffecha) {
        super(ventasDB);
        this.fechaInicio = ffecha;
    }

    @Override
    public String generarReporte() {
        return "Reporte Semanal de Ventas iniciando el: " + fechaInicio.toString();
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
        // Muestra la lista de ventas realizadas en la semana.
    }
}