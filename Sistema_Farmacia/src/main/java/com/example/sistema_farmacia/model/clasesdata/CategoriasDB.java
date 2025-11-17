package com.example.sistema_farmacia.model.clasesdata;

import com.example.sistema_farmacia.model.clasesplantillas.Categoria;
import java.util.HashMap;
import java.util.Map;

public class CategoriasDB {
    // Colección para almacenar los objetos Categoria
    private Map<String, Categoria> listaCategorias;

    public CategoriasDB() {
        this.listaCategorias = new HashMap<>();
    }

    public void agregarCategoria(Categoria categoria) {
        listaCategorias.put(categoria.getCategoriaNombre(), categoria);
    }

    public Map<String, Categoria> getListaCategorias() {
        return listaCategorias;
    }
}