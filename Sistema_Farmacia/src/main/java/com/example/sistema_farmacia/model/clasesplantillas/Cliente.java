package model.clasesplantillas;

import java.util.ArrayList;

public class Cliente {
    // Atributos privados
    private String idCliente;
    private String nombre;
    private String direccion;
    private String numeroContacto;
    private TipoCliente tipoCliente;
    private boolean esClienteAmigo;
    private double porcentajeDescuento;
    private ArrayList<model.clasesplantillas.Venta> historialCompras;

    // Constructor Cliente(nom: String, dir: String, num: String, esCli: boolean)
    public Cliente(String nom, String dir, String num, boolean esCli) {
        this.idCliente = generarID();
        this.nombre = nom;
        this.direccion = dir;
        this.numeroContacto = num;
        this.esClienteAmigo = esCli;
        this.historialCompras = new ArrayList<>();

        this.tipoCliente = TipoCliente.NUEVO;
        this.porcentajeDescuento = 0.0;
    }

    // Métodos de lógica de negocio

    public String generarID() {
        // Lógica para generar un ID único
        return "CLI-" + System.currentTimeMillis();
    }

    public void asignarTipoCliente(TipoCliente tipo) {
        this.tipoCliente = tipo;
    }

    public void agregarCompra(model.clasesplantillas.Venta ventas) {
        this.historialCompras.add(ventas);
    }

    // Métodos Set (Mutadores)

    public void setNombre(String dato) {
        this.nombre = dato;
    }

    public void setDireccion(String dato) {
        this.direccion = dato;
    }

    public void setNumeroContacto(String dato) {
        this.numeroContacto = dato;
    }

    public void setEsClienteAmigo(boolean dato) {
        this.esClienteAmigo = dato;
    }

    public void setPorcentajeDescuento(double dato) {
        this.porcentajeDescuento = dato;
    }

    // Métodos Get (Accesores)

    public String getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public boolean getEsClienteAmigo() {
        return esClienteAmigo;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public ArrayList<model.clasesplantillas.Venta> getHistorialCompras() {
        return historialCompras;
    }
}