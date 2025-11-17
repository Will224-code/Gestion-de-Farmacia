package com.example.sistema_farmacia.model.clasesdata;

import com.example.sistema_farmacia.model.clasesplantillas.Cliente;
import java.util.HashMap;
import java.util.Map;

public class ClientesDB {
    // Colección para almacenar los objetos Cliente, usando el ID como clave
    private Map<String, Cliente> listaClientes;

    public ClientesDB() {
        this.listaClientes = new HashMap<>();
    }

    // Agregar un cliente al repositorio
    public void agregarCliente(Cliente cliente) {
        listaClientes.put(cliente.generarID(), cliente);
    }

    // Eliminar un cliente por su ID
    public void eliminarCliente(String id) {
        listaClientes.remove(id);
    }

    // Retorna el mapa completo
    public Map<String, Cliente> getListaClientes() {
        return listaClientes;
    }
}