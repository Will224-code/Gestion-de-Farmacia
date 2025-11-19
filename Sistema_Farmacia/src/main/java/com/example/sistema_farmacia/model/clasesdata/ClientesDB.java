package model.clasesdata;

import model.clasesplantillas.Cliente;
import java.util.HashMap;
import java.util.Map;


public class ClientesDB {
    // Atributo principal: Mapa que almacena clientes usando el nombre como clave
    private Map<String, Cliente> listaClientes;

    // Constructor ClientesDB()
    public ClientesDB() {
        this.listaClientes = new HashMap<>();
    }

    // Métodos Principales

    public void agregarCliente(Cliente cliente) {
        // Agrega el cliente usando su nombre como clave
        listaClientes.put(cliente.getNombre(), cliente);
    }

    public void eliminarCliente(String nombre) {
        // Elimina el cliente de la lista por su nombre
        listaClientes.remove(nombre);
    }

    public void modificarCliente(String nombreViejo, Cliente cliente) {
        // Elimina el cliente anterior y agrega el objeto Cliente modificado
        listaClientes.remove(nombreViejo);
        listaClientes.put(cliente.getNombre(), cliente);
    }

    public Map<String, Cliente> getListaClientes() {
        // Retorna el Map<> completo
        return listaClientes;
    }
}