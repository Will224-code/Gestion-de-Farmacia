package model.clasesreportes;

import model.clasesdata.ProductosDB;
import model.clasesplantillas.Producto;
import java.time.LocalDate;
import java.util.ArrayList;


public class ReporteInventario {
    private ProductosDB productoDB;
    private LocalDate fechaGeneracion;
    private ArrayList<Producto> productosexistentes;

    // Constructor ReporteInventario(producto : ProductosDB)
    public ReporteInventario(ProductosDB producto) {
        this.productoDB = producto;
        this.fechaGeneracion = sacarHoy();
        this.productosexistentes = convertirArraylist();
    }

    // Métodos Principales (Lógica de Reporte de Inventario)

    public String crearReporteInventario() {
        return "Reporte de Inventario generado al " + fechaGeneracion.toString();
    }

    public void listarProductos() {
        // Muestra o retorna una lista de todos los productos
    }

    public void listarProductosAgotar() {
        // Muestra productos con stock bajo o próximos a agotarse
    }

    public void listarProductosCaducar() {
        // Muestra productos con fecha de caducidad próxima
    }

    public ArrayList<Producto> convertirArraylist() {
        // Convierte el Map de ProductosDB en un ArrayList para procesamiento
        return new ArrayList<>();
    }

    public LocalDate sacarHoy() {
        // Obtiene la fecha actual de la generación del reporte
        return LocalDate.now();
    }
}