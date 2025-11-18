package com.example.sistema_farmacia.model.clasesplantillas;

public class Categoria {
    private String categoriaNombre;
    private String descripcion;

    public Categoria(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
        this.descripcion = "";
    }

    public Categoria(String categoriaNombre, String descripcion) {
        this.categoriaNombre = categoriaNombre;
        this.descripcion = descripcion;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "categoriaNombre='" + categoriaNombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}