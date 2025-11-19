package model.clasesdata;

import model.clasesplantillas.Categoria;
import java.util.HashMap;
import java.util.Map;


public class CategoriasDB {
    // Atributo principal: Mapa que almacena categorías usando el nombre como clave
    private Map<String, Categoria> listaCategorias;

    // Constructor CategoriasDB()
    public CategoriasDB() {
        this.listaCategorias = new HashMap<>();
    }

    // Métodos Principales

    public void agregarCategoria(Categoria categoria) {
        // Agrega la categoría usando su nombre como clave
        listaCategorias.put(categoria.getCategoriaNombre(), categoria);
    }

    public void eliminarCategoria(String nombre) {
        // Elimina la categoría de la lista por su nombre
        listaCategorias.remove(nombre);
    }

    public void modificarCategoria(String nombreViejo, Categoria categoria) {
        // Elimina la categoría anterior y agrega el objeto Categoria modificado
        listaCategorias.remove(nombreViejo);
        listaCategorias.put(categoria.getCategoriaNombre(), categoria);
    }

    public Map<String, Categoria> getListaCategorias() {
        // Retorna el Map<> completo
        return listaCategorias;
    }
}