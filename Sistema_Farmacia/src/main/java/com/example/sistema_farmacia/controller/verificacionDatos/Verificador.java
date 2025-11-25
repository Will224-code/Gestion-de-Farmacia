package com.example.sistema_farmacia.controller.verificacionDatos;


import com.example.sistema_farmacia.controller.Excepciones.excepcionesGenerales.AccesoConcurrenteException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesGenerales.OperacionFueraDeHorarioException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.*;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.CategoriaInexistenteException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.FechaCaducidadException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.ProductoAgotadoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesReportes.ExportacionFallidaException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesReportes.FechaInvalidaException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesReportes.ReporteSinDatosException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesVentas.DescuentoNoAplicableException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesVentas.OperacionNoPermitidaException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesVentas.RecetaObligatoriaException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesVentas.RelacionNoCompatibleException;
import com.example.sistema_farmacia.model.clasesplantillas.Producto;
import com.example.sistema_farmacia.model.clasesplantillas.Categoria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;


public class Verificador {

    // ============ VALIDACIONES BÁSICAS ============
     //Verifica que un texto no esté vacio
    public static void verificarNoVacio(String texto, String nombreCampo) throws CampoVacioException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new CampoVacioException("El campo '" + nombreCampo + "' no puede estar vacío");
        }
    }

     //Verifica que un objeto no sea null
    public static void verificarNoNulo(Object objeto, String nombreCampo) throws CampoVacioException {
        if (objeto == null) {
            throw new CampoVacioException("El campo '" + nombreCampo + "' es requerido");
        }
    }

    // Verifica que un texto sea convertible a número decimal
    public static void verificarNumerico(String texto, String nombreCampo) throws DatoNoNumericoException {
        try {
            Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoNoNumericoException("El campo '" + nombreCampo + "' debe ser numérico. Valor recibido: " + texto);
        }
    }

    //Verifica que un texto sea convertible a entero
    public static void verificarEntero(String texto, String nombreCampo) throws DatoNoNumericoException {
        try {
            Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoNoNumericoException("El campo '" + nombreCampo + "' debe ser un número entero. Valor recibido: " + texto);
        }
    }

    /**
     * Verifica que un número decimal sea positivo
     * @param numero El número a verificar
     * @param nombreCampo Nombre del campo para el mensaje de error
     * @throws DatoNegativoException si el número es negativo
     */
    public static void verificarPositivo(double numero, String nombreCampo) throws DatoNegativoException {
        if (numero < 0) {
            throw new DatoNegativoException("El campo '" + nombreCampo + "' no puede ser negativo. Valor: " + numero);
        }
    }

    // Verifica que un número entero sea positivo
    public static void verificarPositivo(int numero, String nombreCampo) throws DatoNegativoException {
        if (numero < 0) {
            throw new DatoNegativoException("El campo '" + nombreCampo + "' no puede ser negativo. Valor: " + numero);
        }
    }

    // ============ VALIDACIONES DE DUPLICADOS ============

    // Verifica que no exista un duplicado
    public static void verificarDuplicado(boolean existe, String tipoDato, String valor) throws DuplicadoException {
        if (existe) {
            throw new DuplicadoException("Ya existe un registro con " + tipoDato + ": " + valor);
        }
    }

    // ============ VALIDACIONES DE EXISTENCIA ============

    // Verifica que un objeto exista (no sea null)
    public static void verificarExiste(Object objeto, String tipoEntidad, String identificador) throws NoEncontradoException {
        if (objeto == null) {
            throw new NoEncontradoException(tipoEntidad + " no encontrado con identificador: " + identificador);
        }
    }

    // ============ VALIDACIONES ESPECÍFICAS DE PRODUCTOS ============

   //Verifica que la fecha de caducidad sea válida (posterior a hoy)
    public static void verificarFechaCaducidad(LocalDate fechaCaducidad) throws FechaCaducidadException {
        if (fechaCaducidad == null) {
            throw new FechaCaducidadException("La fecha de caducidad no puede ser nula");
        }
        if (fechaCaducidad.isBefore(LocalDate.now())) {
            throw new FechaCaducidadException(
                    "La fecha de caducidad (" + fechaCaducidad + ") ya expiró. Fecha actual: " + LocalDate.now()
            );
        }
    }

    //Verifica que un producto no haya caducado
    public static void verificarProductoNoCaducado(Producto producto) throws FechaCaducidadException {
        if (producto.getFechaCaducidad().isBefore(LocalDate.now())) {
            throw new FechaCaducidadException(
                    "El producto '" + producto.getNombre() + "' está caducado. " +
                            "Fecha de vencimiento: " + producto.getFechaCaducidad()
            );
        }
    }

   // Verifica que haya stock suficiente
    public static void verificarStock(Producto producto, int cantidadSolicitada) throws ProductoAgotadoException {
        if (producto.getUnidadesExi() < cantidadSolicitada) {
            throw new ProductoAgotadoException(
                    "Stock insuficiente para '" + producto.getNombre() + "'. " +
                            "Disponible: " + producto.getUnidadesExi() + ", Solicitado: " + cantidadSolicitada
            );
        }
    }

    // Verifica que el producto tenga al menos una unidad disponible
    public static void verificarStockDisponible(Producto producto) throws ProductoAgotadoException {
        if (producto.getUnidadesExi() <= 0) {
            throw new ProductoAgotadoException("El producto '" + producto.getNombre() + "' está agotado");
        }
    }

    //Verifica que una categoría exista
    public static void verificarCategoriaExiste(Categoria categoria, String nombreCategoria) throws CategoriaInexistenteException {
        if (categoria == null) {
            throw new CategoriaInexistenteException(
                    "La categoría '" + nombreCategoria + "' no existe. " +
                            "Por favor, créala primero en el módulo de Categorías."
            );
        }
    }

    // ============ VALIDACIONES DE RANGOS ============

    // Verifica que un número esté dentro de un rango
    public static void verificarRango(double valor, double minimo, double maximo, String nombreCampo) throws DatoNegativoException {
        if (valor < minimo || valor > maximo) {
            throw new DatoNegativoException(
                    "El campo '" + nombreCampo + "' debe estar entre " + minimo + " y " + maximo + ". Valor: " + valor
            );
        }
    }

    //Verifica que un entero esté dentro de un rango
    public static void verificarRango(int valor, int minimo, int maximo, String nombreCampo) throws DatoNegativoException {
        if (valor < minimo || valor > maximo) {
            throw new DatoNegativoException(
                    "El campo '" + nombreCampo + "' debe estar entre " + minimo + " y " + maximo + ". Valor: " + valor
            );
        }
    }

    // ============ VALIDACIONES DE VENTAS ============

   //Verifica que se haya proporcionado una receta cuando es obligatoria
    public static void verificarRecetaObligatoria(boolean tieneReceta, String nombreProducto) throws RecetaObligatoriaException {
        if (!tieneReceta) {
            throw new RecetaObligatoriaException(
                    "El producto '" + nombreProducto + "' requiere receta médica. " +
                            "Por favor, proporciona la receta antes de continuar con la venta."
            );
        }
    }

    //Verifica que un descuento sea aplicable
    public static void verificarDescuentoAplicable(double descuento, boolean esAplicable, String razon) throws DescuentoNoAplicableException {
        if (!esAplicable) {
            throw new DescuentoNoAplicableException(
                    "El descuento del " + descuento + "% no puede aplicarse. " +
                            "Razón: " + razon
            );
        }
    }

    // ============ VALIDACIONES DE REPORTES ============

    // Verifica que haya datos para generar un reporte
    public static void verificarDatosReporte(Collection<?> datos, String tipoReporte) throws ReporteSinDatosException {
        if (datos == null || datos.isEmpty()) {
            throw new ReporteSinDatosException(
                    "No hay datos disponibles para generar el reporte de " + tipoReporte + ". " +
                            "Verifica los filtros o el rango de fechas seleccionado."
            );
        }
    }

    //Verifica que una fecha sea válida
    public static void verificarFechaValida(LocalDate fecha, String nombreCampo) throws FechaInvalidaException {
        if (fecha == null) {
            throw new FechaInvalidaException(
                    "La fecha '" + nombreCampo + "' no puede estar vacía. " +
                            "Formato esperado: dd/MM/yyyy"
            );
        }
    }

   //Verifica que un rango de fechas sea válido
    public static void verificarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws FechaInvalidaException {
        if (fechaInicio == null || fechaFin == null) {
            throw new FechaInvalidaException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new FechaInvalidaException(
                    "La fecha de inicio (" + fechaInicio + ") no puede ser posterior a la fecha de fin (" + fechaFin + ")"
            );
        }
    }

    //Verifica que la exportación de un archivo fue exitosa
    public static void verificarExportacionExitosa(boolean exitosa, String rutaArchivo, String error) throws ExportacionFallidaException {
        if (!exitosa) {
            throw new ExportacionFallidaException(
                    "Error al exportar el archivo a: " + rutaArchivo + ". " +
                            "Error: " + error + ". Verifica los permisos de escritura."
            );
        }
    }

    // ============ VALIDACIONES DE RECIBOS Y RELACIONES ============

    //Verifica que exista una relación válida entre dos entidades
    public static void verificarRelacionCompatible(boolean relacionExiste, String tipoEntidad1,
                                                   String tipoEntidad2, String identificador) throws RelacionNoCompatibleException {
        if (!relacionExiste) {
            throw new RelacionNoCompatibleException(
                    "No se puede asociar " + tipoEntidad1 + " con " + tipoEntidad2 + ". " +
                            "Identificador no encontrado: " + identificador + ". " +
                            "Verifica que la " + tipoEntidad2 + " exista."
            );
        }
    }

    // ============ VALIDACIONES GENERALES DE OPERACIONES ============

    // Verifica que una operación sea permitida
    public static void verificarOperacionPermitida(boolean esPermitida, String nombreOperacion,
                                                   String razon) throws OperacionNoPermitidaException {
        if (!esPermitida) {
            throw new OperacionNoPermitidaException(
                    "La operación '" + nombreOperacion + "' no está permitida. " +
                            "Razón: " + razon
            );
        }
    }

    //Verifica que la operación se realice dentro del horario permitido
    public static void verificarHorarioOperacion(LocalTime horaActual, LocalTime horaInicio,
                                                 LocalTime horaFin, String nombreOperacion) throws OperacionFueraDeHorarioException {
        if (horaActual.isBefore(horaInicio) || horaActual.isAfter(horaFin)) {
            throw new OperacionFueraDeHorarioException(
                    "La operación '" + nombreOperacion + "' no puede realizarse fuera del horario permitido. " +
                            "Horario: " + horaInicio + " - " + horaFin + ". Hora actual: " + horaActual
            );
        }
    }

    //Verifica que no haya conflictos de acceso concurrente
    public static void verificarAccesoConcurrente(boolean recursoDisponible, String nombreRecurso) throws AccesoConcurrenteException {
        if (!recursoDisponible) {
            throw new AccesoConcurrenteException(
                    "El recurso '" + nombreRecurso + "' está siendo modificado por otro usuario. " +
                            "Por favor, intenta nuevamente en unos momentos."
            );
        }
    }

    // ============ VALIDACIONES DE COLECCIONES ============

   //Verifica que una colección no esté vacía
    public static void verificarColeccionNoVacia(Collection<?> coleccion, String nombreColeccion) throws CampoVacioException {
        if (coleccion == null || coleccion.isEmpty()) {
            throw new CampoVacioException("La lista de " + nombreColeccion + " no puede estar vacía");
        }
    }

    //Verifica que una colección tenga un tamaño mínimo
    public static void verificarTamañoMinimo(Collection<?> coleccion, int tamañoMinimo,
                                             String nombreColeccion) throws CampoVacioException {
        if (coleccion == null || coleccion.size() < tamañoMinimo) {
            throw new CampoVacioException(
                    "La lista de " + nombreColeccion + " debe tener al menos " + tamañoMinimo + " elemento(s). " +
                            "Actual: " + (coleccion == null ? 0 : coleccion.size())
            );
        }
    }
}