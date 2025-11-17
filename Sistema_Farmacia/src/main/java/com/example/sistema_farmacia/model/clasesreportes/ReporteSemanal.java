package com.example.sistema_farmacia.model.clasesreportes;
import model.clasesdata.VentasDB;
import java.time.LocalDate;

public class ReporteSemanal extends ReporteVentas {
    public ReporteSemanal(VentasDB ventasDB, LocalDate fechaInicio) {
        super(ventasDB);
    }
}