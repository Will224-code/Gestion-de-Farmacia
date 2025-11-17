package com.example.sistema_farmacia.model.clasesdata;

import com.example.sistema_farmacia.model.clasesplantillas.Producto;
import java.util.HashMap;
import java.util.Map;

public class ProductosDB {
    // Colección para almacenar los objetos Producto
    private Map<String, Producto> listaProductos;

    public ProductosDB() {
        this.listaProductos = new HashMap<>();
    }

    public void agregarProducto(Producto producto) {
        // Asumiendo que Producto tiene un método getCodigo()
        // listaProductos.put(producto.getCodigo(), producto);
    }

    public Map<String, Producto> getListaProductos() {
        return listaProductos;
    }
}