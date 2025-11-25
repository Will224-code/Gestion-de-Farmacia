package com.example.sistema_farmacia.model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Recibo {
    private String idRecibo;
    private LocalDate fecha;
    private Venta venta;
    private double total;
    private double descuento;
    private ArrayList<Producto> productos;

    // Constructor
    public Recibo(Venta venta) {
        this.venta = venta;
        this.idRecibo = generarIdRecibo();
        this.fecha = venta.getFechaVenta();
        this.total = venta.getTotal(); // Total ya con descuento
        this.descuento = venta.getDescuentoAplicado();
        this.productos = venta.getVenta();
    }

    // Ticket agrupado y profesional
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== RECIBO DE VENTA ==========\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Recibo No.: ").append(idRecibo).append("\n");
        sb.append("-------------------------------------\n");
        sb.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
        sb.append("Teléfono: ").append(venta.getCliente().getNumeroContacto()).append("\n");
        sb.append("Tipo cliente: ").append(venta.getCliente().getTipoCliente()).append("\n");
        sb.append("-------------------------------------\n\n");
        sb.append(String.format("%-15s %7s %10s\n", "Producto", "Cantidad", "Precio"));
        sb.append("-------------------------------------\n");

        // Agrupa productos por nombre y cuenta cantidades
        Map<String, Integer> conteo = new LinkedHashMap<>();
        Map<String, Double> precios = new LinkedHashMap<>();
        for (Producto p : productos) {
            conteo.put(p.getNombre(), conteo.getOrDefault(p.getNombre(), 0) + 1);
            precios.putIfAbsent(p.getNombre(), p.getPrecioVenta());
        }
        for (String nombre : conteo.keySet()) {
            sb.append(String.format("%-15s x%-6d $%.2f\n", nombre, conteo.get(nombre), precios.get(nombre)));
        }
        sb.append("-------------------------------------\n");
        sb.append(String.format("Total sin descuento: $%.2f\n", venta.getTotalSinDescuento()));
        sb.append(String.format("Descuento aplicado:  $%.2f\n", venta.getDescuentoAplicado()));
        sb.append(String.format("TOTAL A COBRAR:     $%.2f\n", total));
        sb.append("=====================================\n");
        return sb.toString();
    }

    public void listarProductos() {
        for(Producto p: productos){
            System.out.println("------------");
            System.out.println("Código: " + p.getCodigo());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Precio: " + p.getPrecioVenta());
            System.out.println("------------");
        }
    }

    private String generarIdRecibo() {
        return "REC-" + System.currentTimeMillis();
    }

    // Getters
    public LocalDate getFecha() { return fecha; }
    public double getTotal() { return total; }
    public Venta getVenta() { return venta; }
    public double getDescuento() { return descuento; }
}
