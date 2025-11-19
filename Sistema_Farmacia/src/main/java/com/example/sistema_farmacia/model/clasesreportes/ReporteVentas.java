package model.clasesreportes;

import model.clasesdata.VentasDB;
import model.clasesplantillas.Venta;
import java.util.ArrayList;

public class ReporteVentas {
    private VentasDB ventasDB;
    private ArrayList<Venta> ventas;
    private double totalVenta;
    private double gananciaTotal;

    // Constructor ReporteVentas(ventasDB : VentasDB)
    public ReporteVentas(VentasDB ventasDB) {
        // Inicializa el atributo ventasDB
        this.ventasDB = ventasDB;
        this.ventas = new ArrayList<>();
        this.totalVenta = 0.0;
        this.gananciaTotal = 0.0;
    }


    public String generarReporte() {
        return "Reporte base de ventas.";
    }

    public double sacarTotalVenta() {
        // Encargado de sacar el total de la venta (no tiene contenido en la clase base)
        return totalVenta;
    }

    public double sacarTotalGanacia() {
        // Encargado de sacar la ganancia total (no tiene contenido en la clase base)
        return gananciaTotal;
    }

    public void mostrarInfoVentas() {
        // Mostrará la información de las ventas realizadas, como una lista
    }

    public ArrayList<Venta> sacarArrayListVentas() {
        // Encargado de transformar el Map<> en un ArrayList<>
        return ventas;
    }
}