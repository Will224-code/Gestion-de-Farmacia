package com.example.sistema_farmacia.model.clasesplantillas;

import java.util.ArrayList;

public class Categoria {
    private String categoriaNombre;
    private String descripcion;

    // Constructor
    public Categoria(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    // Métodos get y set
    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public ArrayList<Producto> getProductos() {
        // Simulación: Lógica para devolver productos de esta categoría.
        return new ArrayList<Producto>();
    }
}