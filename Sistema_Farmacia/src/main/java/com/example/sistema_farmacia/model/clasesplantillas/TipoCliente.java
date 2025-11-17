package com.example.sistema_farmacia.model.clasesplantillas;

public enum TipoCliente {
    // Enumeración para diferenciar perfiles de clientes
    BENEFICIARIO("beneficiario"),
    REGULAR("regular"),
    NUEVO("nuevo");

    private final String tipo;

    TipoCliente(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}