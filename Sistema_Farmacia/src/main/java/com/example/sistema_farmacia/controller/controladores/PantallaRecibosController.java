package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesVentas.RelacionNoCompatibleException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.NoEncontradoException;
import com.example.sistema_farmacia.controller.verificacionDatos.Verificador;
import com.example.sistema_farmacia.model.clasesdata.ProductosDB;
import com.example.sistema_farmacia.model.clasesdata.VentasDB;
import com.example.sistema_farmacia.model.clasesdata.ClientesDB;
import com.example.sistema_farmacia.model.clasesplantillas.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import java.time.LocalDate;
import java.util.List;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PantallaRecibosController extends ControladorBase {

    @FXML private TableView<Venta> tablaRecibos;
    @FXML private TableColumn<Venta, String> colId, colFecha, colTotal, colCliente, colOpciones;
    @FXML private DatePicker filtroFecha;
    @FXML private TextField filtroCliente;
    @FXML private Button btnFiltrar;

    private VentasDB ventasDB;
    private ProductosDB productosDB;
    private ClientesDB clientesDB;
    private ObservableList<Venta> ventasMostradas = FXCollections.observableArrayList();

    // Setters para inyección de modelos
    public void setVentasDB(VentasDB ventasDB) { this.ventasDB = ventasDB; }
    public void setProductosDB(ProductosDB productosDB) { this.productosDB = productosDB; }
    public void setClientesDB(ClientesDB clientesDB) { this.clientesDB = clientesDB; }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdVenta()));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaVenta().toString()));
        colTotal.setCellValueFactory(data -> new SimpleStringProperty(String.format("$ %.2f", data.getValue().getTotal())));
        colCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNombre()));

        colOpciones.setCellFactory(tc -> new TableCell<>() {
            final Button btnVer = new Button("Ver");
            final Button btnDevolver = new Button("Devolver");
            {
                btnVer.setOnAction(e -> mostrarDetalleRecibo(getTableView().getItems().get(getIndex())));
                btnDevolver.setOnAction(e -> mostrarDialogoDevolucion(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new VBox(6, btnVer, btnDevolver));
            }
        });

        btnFiltrar.setOnAction(e -> filtrarRecibos());
        filtroFecha.setOnAction(e -> filtrarRecibos());
        filtroCliente.setOnAction(e -> filtrarRecibos());

        filtrarRecibos(); // Carga inicial: todos los recibos más nuevos primero
    }

    private void filtrarRecibos() {
        try {
            LocalDate fecha = filtroFecha.getValue();
            String clienteBuscado = filtroCliente.getText() != null ? filtroCliente.getText().trim().toLowerCase() : "";

            Predicate<Venta> filtro = v ->
                    (fecha == null || v.getFechaVenta().isEqual(fecha)) &&
                            (clienteBuscado.isEmpty() || v.getCliente().getNombre().toLowerCase().contains(clienteBuscado));

            List<Venta> lista = ventasDB.getListaVentas().values().stream()
                    .filter(filtro)
                    .sorted(Comparator.comparing(Venta::getFechaVenta).reversed().thenComparing(Venta::getIdVenta).reversed())
                    .collect(Collectors.toList());

            ventasMostradas.setAll(lista);
            tablaRecibos.setItems(ventasMostradas);

        } catch (Exception e) {
            mostrarAlertaError("Error al filtrar recibos: " + e.getMessage());
        }
    }

    // -- PANEL "VER" DETALLE DEL RECIBO/VENTA
    private void mostrarDetalleRecibo(Venta venta) {
        try {
            // Validar que la venta exista
            Verificador.verificarExiste(venta, "Venta", venta != null ? venta.getIdVenta() : "null");

            Dialog<Void> dialogo = new Dialog<>();
            dialogo.setTitle("Detalle de venta/recibo");
            VBox content = new VBox(12);
            content.setPadding(new Insets(18));

            StringBuilder sb = new StringBuilder();
            sb.append("ID Venta: ").append(venta.getIdVenta()).append("\n");
            sb.append("Fecha: ").append(venta.getFechaVenta()).append("\n");
            sb.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
            sb.append("Teléfono: ").append(venta.getCliente().getNumeroContacto()).append("\n");
            sb.append("Dirección: ").append(venta.getCliente().getDireccion()).append("\n");
            sb.append("Tipo de cliente: ").append(venta.getCliente().getTipoCliente()).append("\n\n");
            sb.append("Productos vendidos:\n");
            sb.append(String.format("%-15s | %-10s | %-8s | %-12s\n", "Nombre", "Cantidad", "Precio", "Categoría"));
            sb.append("------------------------------------------------------\n");

            var conteo = venta.getVenta().stream()
                    .collect(Collectors.groupingBy(Producto::getNombre, Collectors.counting()));
            var categorias = venta.getVenta().stream()
                    .collect(Collectors.toMap(Producto::getNombre, p -> p.getCategoria().stream().map(Categoria::getCategoriaNombre).collect(Collectors.joining(", ")), (a, b) -> a));
            var precios = venta.getVenta().stream()
                    .collect(Collectors.toMap(Producto::getNombre, Producto::getPrecioVenta, (a, b) -> a));

            for (String nombre : conteo.keySet()) {
                sb.append(String.format("%-15s | %-10d | $%-7.2f | %-12s\n",
                        nombre, conteo.get(nombre), precios.get(nombre), categorias.get(nombre)));
            }

            sb.append("------------------------------------------------------\n");
            sb.append(String.format("Total sin descuento: $%.2f\n", venta.getTotalSinDescuento()));
            sb.append(String.format("Descuento aplicado: $%.2f\n", venta.getDescuentoAplicado()));
            sb.append(String.format("TOTAL COBRADO:      $%.2f\n", venta.getTotal()));
            content.getChildren().add(new Label(sb.toString()));

            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setOnAction(e -> dialogo.close());
            content.getChildren().add(btnCerrar);
            dialogo.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialogo.getDialogPane().setContent(content);
            dialogo.showAndWait();

        } catch (NoEncontradoException e) {
            mostrarAlertaError(e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error al mostrar detalle: " + e.getMessage());
        }
    }

    // -- PANEL DEVOLUCIÓN
    private void mostrarDialogoDevolucion(Venta venta) {
        try {
            // Validar que la venta exista
            Verificador.verificarExiste(venta, "Venta", venta != null ? venta.getIdVenta() : "null");

            // Validar que la venta no esté muy antigua (ejemplo: máximo 30 días)
            boolean dentroDelPeriodo = venta.getFechaVenta().isAfter(LocalDate.now().minusDays(30));
            if (!dentroDelPeriodo) {
                mostrarAdvertencia("Esta venta tiene más de 30 días y no puede ser devuelta.");
                return;
            }

            Dialog<ButtonType> dialogo = new Dialog<>();
            dialogo.setTitle("Confirmar devolución");
            VBox content = new VBox(14);
            content.setPadding(new Insets(14));

            StringBuilder sb = new StringBuilder();
            sb.append("¿Seguro que deseas devolver/eliminar esta venta?\n\n");
            sb.append("Cliente: ").append(venta.getCliente().getNombre())
                    .append(" | Tel: ").append(venta.getCliente().getNumeroContacto())
                    .append("\n\nProductos:\n");

            var conteo = venta.getVenta().stream()
                    .collect(Collectors.groupingBy(Producto::getNombre, Collectors.counting()));

            for (var n : conteo.keySet()) {
                sb.append(String.format("  - %-16s x%-4d\n", n, conteo.get(n)));
            }
            content.getChildren().add(new Label(sb.toString()));

            ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = ButtonType.CANCEL;
            dialogo.getDialogPane().getButtonTypes().addAll(btnAceptar, btnCancelar);
            dialogo.getDialogPane().setContent(content);
            dialogo.setResultConverter(bt -> bt);

            dialogo.showAndWait().ifPresent(bt -> {
                if (bt == btnAceptar) {
                    procesarDevolucion(venta);
                }
            });

        } catch (NoEncontradoException e) {
            mostrarAlertaError(e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error al procesar devolución: " + e.getMessage());
        }
    }

    private void procesarDevolucion(Venta venta) {
        try {
            // Validar que la venta exista en la BD
            boolean ventaExiste = ventasDB.getListaVentas().containsKey(venta.getIdVenta());
            Verificador.verificarRelacionCompatible(ventaExiste, "Devolución", "Venta", venta.getIdVenta());

            // Devuelve productos al stock
            for (Producto vendido : venta.getVenta()) {
                Producto stock = productosDB.getListaProductos().get(vendido.getCodigo());
                if (stock != null) {
                    stock.setUnidadesExi(stock.getUnidadesExi() + 1);
                }
            }

            // Elimina de historial del cliente
            Cliente cli = clientesDB.getListaClientes().get(venta.getCliente().getIdCliente());
            if (cli != null && cli.getHistorialCompras() != null) {
                cli.getHistorialCompras().remove(venta);
            }

            // Elimina de ventas
            ventasDB.eliminarVentas(venta.getIdVenta());

            mostrarExito("¡Devolución exitosa! Venta removida y productos restituidos.");
            filtrarRecibos();

        } catch (RelacionNoCompatibleException e) {
            mostrarAlertaError(e.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("No se pudo devolver: " + e.getMessage());
        }
    }
}