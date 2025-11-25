package com.example.sistema_farmacia.controller.verificacionDatos;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.*;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.FechaCaducidadException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.ProductoAgotadoException;
import com.example.sistema_farmacia.model.clasesplantillas.Producto;

import java.time.LocalDate;

/**
 * Clase utilitaria para centralizar todas las validaciones del sistema.
 * Contiene métodos estáticos que lanzan excepciones personalizadas.
 */
public class Verificador {

    // ============ VALIDACIONES BÁSICAS ============

    /**
     * Verifica que un texto no esté vacío
     * @param texto El texto a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws CampoVacioException si el texto está vacío o null
     */
    public static void verificarNoVacio(String texto, String nombreCampo) throws CampoVacioException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new CampoVacioException("El campo '" + nombreCampo + "' no puede estar vacío");
        }
    }

    /**
     * Verifica que un texto sea convertible a número
     * @param texto El texto a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws DatoNoNumericoException si no es un número válido
     */
    public static void verificarNumerico(String texto, String nombreCampo) throws DatoNoNumericoException {
        try {
            Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoNoNumericoException("El campo '" + nombreCampo + "' debe ser numérico. Valor recibido: " + texto);
        }
    }

    /**
     * Verifica que un texto sea convertible a entero
     * @param texto El texto a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws DatoNoNumericoException si no es un entero válido
     */
    public static void verificarEntero(String texto, String nombreCampo) throws DatoNoNumericoException {
        try {
            Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoNoNumericoException("El campo '" + nombreCampo + "' debe ser un número entero. Valor recibido: " + texto);
        }
    }

    /**
     * Verifica que un número sea positivo
     * @param numero El número a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws DatoNegativoException si el número es negativo
     */
    public static void verificarPositivo(double numero, String nombreCampo) throws DatoNegativoException {
        if (numero < 0) {
            throw new DatoNegativoException("El campo '" + nombreCampo + "' no puede ser negativo. Valor: " + numero);
        }
    }

    /**
     * Verifica que un número entero sea positivo
     * @param numero El número a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws DatoNegativoException si el número es negativo
     */
    public static void verificarPositivo(int numero, String nombreCampo) throws DatoNegativoException {
        if (numero < 0) {
            throw new DatoNegativoException("El campo '" + nombreCampo + "' no puede ser negativo. Valor: " + numero);
        }
    }

    // ============ VALIDACIONES DE DUPLICADOS ============

    /**
     * Verifica que no exista un duplicado
     * @param existe true si ya existe el elemento
     * @param tipoDato Tipo de dato duplicado (ej: "código", "nombre")
     * @param valor Valor duplicado
     * @throws DuplicadoException si existe el elemento
     */
    public static void verificarDuplicado(boolean existe, String tipoDato, String valor) throws DuplicadoException {
        if (existe) {
            throw new DuplicadoException("Ya existe un registro con " + tipoDato + ": " + valor);
        }
    }

    // ============ VALIDACIONES DE EXISTENCIA ============

    /**
     * Verifica que un objeto exista (no sea null)
     * @param objeto El objeto a verificar
     * @param tipoEntidad Tipo de entidad (ej: "Producto", "Cliente")
     * @param identificador Identificador usado en la búsqueda
     * @throws NoEncontradoException si el objeto es null
     */
    public static void verificarExiste(Object objeto, String tipoEntidad, String identificador) throws NoEncontradoException {
        if (objeto == null) {
            throw new NoEncontradoException(tipoEntidad + " no encontrado con identificador: " + identificador);
        }
    }

    // ============ VALIDACIONES ESPECÍFICAS DE PRODUCTOS ============

    /**
     * Verifica que la fecha de caducidad sea válida (posterior a hoy)
     * @param fechaCaducidad Fecha a verificar
     * @throws FechaCaducidadException si la fecha ya pasó
     */
    public static void verificarFechaCaducidad(LocalDate fechaCaducidad) throws FechaCaducidadException {
        if (fechaCaducidad == null) {
            throw new FechaCaducidadException("La fecha de caducidad no puede ser nula");
        }
        if (fechaCaducidad.isBefore(LocalDate.now())) {
            throw new FechaCaducidadException("La fecha de caducidad (" + fechaCaducidad + ") ya expiró. Fecha actual: " + LocalDate.now());
        }
    }

    /**
     * Verifica que haya stock suficiente
     * @param producto El producto a verificar
     * @param cantidadSolicitada Cantidad que se desea
     * @throws ProductoAgotadoException si no hay suficiente stock
     */
    public static void verificarStock(Producto producto, int cantidadSolicitada) throws ProductoAgotadoException {
        if (producto.getUnidadesExi() < cantidadSolicitada) {
            throw new ProductoAgotadoException(
                    "Stock insuficiente para '" + producto.getNombre() + "'. " +
                            "Disponible: " + producto.getUnidadesExi() + ", Solicitado: " + cantidadSolicitada
            );
        }
    }

    /**
     * Verifica que el producto tenga al menos una unidad disponible
     * @param producto El producto a verificar
     * @throws ProductoAgotadoException si no hay stock
     */
    public static void verificarStockDisponible(Producto producto) throws ProductoAgotadoException {
        if (producto.getUnidadesExi() <= 0) {
            throw new ProductoAgotadoException("El producto '" + producto.getNombre() + "' está agotado");
        }
    }

    // ============ VALIDACIONES DE RANGOS ============

    /**
     * Verifica que un número esté dentro de un rango
     * @param valor Valor a verificar
     * @param minimo Valor mínimo permitido
     * @param maximo Valor máximo permitido
     * @param nombreCampo Nombre del campo
     * @throws DatoNegativoException si está fuera de rango
     */
    public static void verificarRango(double valor, double minimo, double maximo, String nombreCampo) throws DatoNegativoException {
        if (valor < minimo || valor > maximo) {
            throw new DatoNegativoException(
                    "El campo '" + nombreCampo + "' debe estar entre " + minimo + " y " + maximo + ". Valor: " + valor
            );
        }
    }
}