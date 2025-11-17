package com.example.sistema_farmacia.model.clasesdata;

import com.example.sistema_farmacia.model.clasesplantillas.Recibo;
import java.util.HashMap;
import java.util.Map;

public class VentasDB {
    // Colección para almacenar los objetos Recibo (el registro de la venta)
    private Map<String, Recibo> listaVentas;

    public VentasDB() {
        this.listaVentas = new HashMap<>();
    }

    public void agregarVentas(Recibo recibo) {
        // Asumiendo que Recibo tiene un getter para su ID
        // listaVentas.put(recibo.getIdRecibo(), recibo);
    }

    public Map<String, Recibo> getListaVentas() {
        return listaVentas;
    }
}