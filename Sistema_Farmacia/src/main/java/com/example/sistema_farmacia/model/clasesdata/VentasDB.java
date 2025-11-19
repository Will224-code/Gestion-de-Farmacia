package model.clasesdata;

import model.clasesplantillas.Recibo;
import java.util.HashMap;
import java.util.Map;

public class VentasDB {
    // Atributo principal: Mapa que almacena recibos usando el ID como clave
    private Map<String, Recibo> listaVentas;

    // Constructor VentasDB()
    public VentasDB() {
        this.listaVentas = new HashMap<>();
    }

    // Métodos Principales

    public void agregarVentas(Recibo recibo) {
        // Agrega el recibo a la lista usando su ID
        listaVentas.put(recibo.generarIdRecibo(), recibo);
    }

    public void eliminarVentas(String id) {
        // Elimina la venta de la lista buscándola por su ID
        listaVentas.remove(id);
    }

    public Map<String, Recibo> getListaVentas() {
        // Retorna el Map<> completo
        return listaVentas;
    }
}