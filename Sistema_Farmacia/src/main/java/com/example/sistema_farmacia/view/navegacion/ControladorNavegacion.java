package com.example.sistema_farmacia.view.navegacion;

import javafx.scene.Parent;
import java.util.HashMap;
import java.util.Map;


public class ControladorNavegacion {
    private Parent pantallaActual;
    private final Map<String, Parent> pantallas;

    public ControladorNavegacion() {
        pantallas = new HashMap<>();
    }


    public void agregarPantalla(String nombre, Parent pantalla) {
        pantallas.put(nombre, pantalla);
    }


    public Parent cambiarPantalla(String nombre) {
        pantallaActual = pantallas.get(nombre);
        return pantallaActual;
    }


     //Método para cerrar la aplicación desde navegación.

    public void salirAplicacion() {
        System.exit(0);
    }
}
