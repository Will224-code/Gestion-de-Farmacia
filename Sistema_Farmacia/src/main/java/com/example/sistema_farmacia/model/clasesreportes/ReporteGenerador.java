package model.clasesreportes;

import model.clasesdata.VentasDB;
import model.clasesdata.ProductosDB;
import java.time.LocalDate;

public class ReporteGenerador {
    private VentasDB ventasDB;
    private ProductosDB productosDB;

    // Constructor ReporteGenerador(ventas : VentasDB, productos : ProductosDB)
    public ReporteGenerador(VentasDB ventas, ProductosDB productos) {
        this.ventasDB = ventas;
        this.productosDB = productos;
    }


    public model.clasesreportes.ReporteDiario generarReporteVentasDiario(LocalDate fecha) {
        return new model.clasesreportes.ReporteDiario(ventasDB, fecha);
    }

    public model.clasesreportes.ReporteSemanal generarReporteVentasSemanal(LocalDate fechaInicio) {
        return new model.clasesreportes.ReporteSemanal(ventasDB, fechaInicio);
    }

    public model.clasesreportes.ReporteMensual generarReporteVentasMensual(int anio, int mes) {
        return new model.clasesreportes.ReporteMensual(ventasDB, anio, mes);
    }

    public model.clasesreportes.ReporteAnual generarReporteVentasAnual(int anio) {
        return new model.clasesreportes.ReporteAnual(ventasDB, anio);
    }

    public model.clasesreportes.ReporteInventario generarReporteInventario() {
        return new model.clasesreportes.ReporteInventario(productosDB);
    }
}