package model.clasesplantillas;

import java.time.LocalDate;
import java.util.ArrayList;


public class Venta {
    private String idVenta;
    private LocalDate fechaVenta;
    private model.clasesplantillas.Cliente cliente;
    private String descripcion;
    private double total;
    private boolean requiereReceta;
    private ArrayList<model.clasesplantillas.Producto> venta; // Lista de productos vendidos

    // Constructor Ventas(cliente : Cliente)
    public Venta(model.clasesplantillas.Cliente cliente) {
        this.cliente = cliente;
        this.idVenta = generarIdVenta();
        this.fechaVenta = generarFecha();
        this.venta = new ArrayList<>();
        this.total = 0.0;
        this.requiereReceta = false; // Valor inicial
    }

    // Métodos Principales (Lógica de Negocio)

    public String generarIdVenta() {
        // Lógica para generar un ID único
        return "VEN-" + System.currentTimeMillis();
    }

    public LocalDate generarFecha() {
        // Genera la fecha actual
        return LocalDate.now();
    }

    public void agregarProducto(model.clasesplantillas.Producto producto) {
        this.venta.add(producto);
    }

    public double calcularTotal() {
        double subtotal = 0.0;
        for (model.clasesplantillas.Producto p : venta) {
            subtotal += p.getPrecioVenta();
        }
        // Aplicar el descuento llamando a aplicarDescuentoCliente()
        double descuento = aplicarDescuentoCliente();
        this.total = subtotal - descuento;
        return this.total;
    }

    public double aplicarDescuentoCliente() {
        return this.total * (cliente.getPorcentajeDescuento() / 100.0);
    }

    public model.clasesplantillas.Recibo generarRecibo() {
        return new model.clasesplantillas.Recibo(this);
    }

    // Métodos Get (Accesores)

    public String getIdVenta() {
        return idVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public model.clasesplantillas.Cliente getCliente() {
        return cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getTotal() {
        return total;
    }

    public boolean getRequiereRecesa() {
        return requiereReceta;
    }

    public ArrayList<model.clasesplantillas.Producto> getVenta() {
        return venta;
    }

    // Métodos Set (Mutadores)

    public void setCliente(model.clasesplantillas.Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRequiereReceta(boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }
}