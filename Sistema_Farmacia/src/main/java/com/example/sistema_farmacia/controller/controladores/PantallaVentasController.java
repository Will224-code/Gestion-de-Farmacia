package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.*;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.ProductoAgotadoException;
import com.example.sistema_farmacia.controller.verificacionDatos.Verificador;
import com.example.sistema_farmacia.model.clasesdata.*;
import com.example.sistema_farmacia.model.clasesplantillas.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.stream.Collectors;

public class PantallaVentasController extends ControladorBase {

    @FXML private TextField txtCodigoBuscar;
    @FXML private Button btnAgregarProducto, btnVender, btnHistorial;
    @FXML private TableView<ProductoEnVenta> tablaCarrito;
    @FXML private TableColumn<ProductoEnVenta, String> colId, colNombre, colDescripcion, colCategorias,
            colCantidad, colStock, colPrecioUnitario, colPrecioTotal;
    @FXML private TableColumn<ProductoEnVenta, Void> colAcciones;
    @FXML private Label lblTotal;

    private ProductosDB productosDB;
    private ClientesDB clientesDB;
    private VentasDB ventasDB;
    private CategoriasDB categoriasDB;

    private ObservableList<ProductoEnVenta> carrito = FXCollections.observableArrayList();

    public void setProductosDB(ProductosDB productosDB) { this.productosDB = productosDB; }
    public void setClientesDB(ClientesDB clientesDB) { this.clientesDB = clientesDB; }
    public void setVentasDB(VentasDB ventasDB) { this.ventasDB = ventasDB; }
    public void setCategoriasDB(CategoriasDB categoriasDB) { this.categoriasDB = categoriasDB; }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getCodigo()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getNombre()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getDescripcion()));
        colCategorias.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getProducto().getCategoria().stream().map(Categoria::getCategoriaNombre).collect(Collectors.joining(", "))
        ));
        colCantidad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCantidad())));
        colStock.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().stockVisual())));
        colPrecioUnitario.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getProducto().getPrecioVenta())));
        colPrecioTotal.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrecioTotal())));

        colAcciones.setCellFactory(tc -> new TableCell<>() {
            final Button btnSumar = new Button("+");
            final Button btnRestar = new Button("-");
            {
                btnSumar.setOnAction(e -> actualizarCantidad(getTableRow().getItem(), true));
                btnRestar.setOnAction(e -> actualizarCantidad(getTableRow().getItem(), false));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) setGraphic(null);
                else setGraphic(new HBox(5, btnRestar, btnSumar));
            }
        });

        tablaCarrito.setItems(carrito);

        btnAgregarProducto.setOnAction(e -> buscarYAgregar());
        btnVender.setOnAction(e -> iniciarVenta());
        btnHistorial.setOnAction(e -> abrirHistorial());
        recalcularTotal();
    }

    // --- AGREGAR AL CARRITO ---
    /**
     * Busca y agrega un producto al carrito validando su existencia y stock
     */
    private void buscarYAgregar() {
        try {
            // 1. Validar código
            String codigo = txtCodigoBuscar.getText().trim();
            Verificador.verificarNoVacio(codigo, "código del producto");

            // 2. Buscar producto
            Producto producto = productosDB.getListaProductos().get(codigo);
            Verificador.verificarExiste(producto, "Producto", codigo);

            // 3. Verificar stock disponible
            Verificador.verificarStockDisponible(producto);

            // 4. Verificar si ya está en el carrito
            for (ProductoEnVenta pe : carrito) {
                if (pe.getProducto().getCodigo().equalsIgnoreCase(codigo)) {
                    // Ya existe, intentar sumar
                    if (pe.getCantidad() < producto.getUnidadesExi()) {
                        pe.setCantidad(pe.getCantidad() + 1);
                        mostrarMensaje("Producto sumado (total: " + pe.getCantidad() + ")");
                    } else {
                        mostrarAdvertencia("No puedes agregar más, alcanzaste el stock máximo disponible.");
                    }
                    recalcularTotal();
                    tablaCarrito.refresh();
                    return;
                }
            }

            // 5. Agregar nuevo al carrito
            carrito.add(new ProductoEnVenta(producto, 1));
            recalcularTotal();
            mostrarMensaje("Producto agregado al carrito");

        } catch (CampoVacioException | NoEncontradoException | ProductoAgotadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    private void actualizarCantidad(ProductoEnVenta producto, boolean sumar) {
        if (producto == null) return;

        try {
            int actual = producto.getCantidad();
            int maxStock = producto.getProducto().getUnidadesExi();

            if (sumar) {
                if (actual < maxStock) {
                    producto.setCantidad(actual + 1);
                } else {
                    mostrarAdvertencia("No puedes agregar más, alcanzaste el stock máximo disponible.");
                }
            } else {
                if (actual > 1) {
                    producto.setCantidad(actual - 1);
                } else {
                    carrito.remove(producto);
                }
            }

            recalcularTotal();
            tablaCarrito.refresh();

        } catch (Exception e) {
            mostrarAlertaError("Error al actualizar cantidad: " + e.getMessage());
        }
    }

    private void recalcularTotal() {
        double total = carrito.stream().mapToDouble(ProductoEnVenta::getPrecioTotal).sum();
        lblTotal.setText(String.format("$ %.2f", total));
    }

    // ----------------- FLUJO DE VENTA COMPLETO ----------------//
    private void iniciarVenta() {
        try {
            if (carrito.isEmpty()) {
                throw new CampoVacioException("¡Agrega productos al carrito antes de vender!");
            }

            // 1. Seleccionar cliente
            Dialog<Cliente> dialogoCliente = construirDialogoSeleccionCliente();
            dialogoCliente.showAndWait().ifPresent(cliente -> {
                if (cliente == null) {
                    mostrarAdvertencia("Debes seleccionar un cliente válido.");
                    return;
                }
                // 2. Confirmar venta / recibo
                Dialog<Boolean> dialogoConfirmar = construirDialogoConfirmarVenta(cliente);
                dialogoConfirmar.showAndWait().ifPresent(requiereRecibo -> {
                    finalizarVenta(cliente, Boolean.TRUE.equals(requiereRecibo));
                });
            });

        } catch (CampoVacioException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    private void finalizarVenta(Cliente cliente, boolean requiereRecibo) {
        try {
            // Crear venta
            Venta nuevaVenta = new Venta(cliente);
            for (ProductoEnVenta pe : carrito) {
                for (int i = 0; i < pe.getCantidad(); i++)
                    nuevaVenta.agregarProducto(pe.getProducto());
            }

            double totalSinDescuento = nuevaVenta.getTotal();
            double descuentoAplicado = (cliente.getPorcentajeDescuento()/100.0) * totalSinDescuento;
            double totalFinal = totalSinDescuento - descuentoAplicado;

            ventasDB.agregarVentas(nuevaVenta);

            // Actualizar stock
            for (ProductoEnVenta pe : carrito) {
                pe.getProducto().setUnidadesExi(pe.getProducto().getUnidadesExi() - pe.getCantidad());
            }

            carrito.clear();
            recalcularTotal();

            Recibo recibo = null;
            if (requiereRecibo) {
                recibo = new Recibo(nuevaVenta);
            }

            mostrarResumenVenta(nuevaVenta, recibo, descuentoAplicado, totalFinal, totalSinDescuento);

        } catch (Exception e) {
            mostrarAlertaError("Error al finalizar venta: " + e.getMessage());
        }
    }

    // --- DIALOGO Selección de Cliente ---
    private Dialog<Cliente> construirDialogoSeleccionCliente() {
        Dialog<Cliente> dialogo = new Dialog<>();
        dialogo.setTitle("Asignar venta a cliente");
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        TextField buscar = new TextField();
        buscar.setPromptText("Buscar cliente...");
        TableView<Cliente> tabla = new TableView<>();
        TableColumn<Cliente, String> colNom = new TableColumn<>("Nombre");
        TableColumn<Cliente, String> colTel = new TableColumn<>("Teléfono");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colTel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroContacto()));
        tabla.getColumns().addAll(colNom, colTel);

        ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(clientesDB.getListaClientes().values());
        tabla.setItems(listaClientes);

        buscar.textProperty().addListener((obs, old, val) -> {
            tabla.setItems(listaClientes.filtered(c -> c.getNombre().toLowerCase().contains(val.toLowerCase())));
        });

        Button btnNuevo = new Button("Agregar Cliente nuevo");
        btnNuevo.setOnAction(e -> {
            Cliente agregado = mostrarDialogoAgregarCliente();
            if (agregado != null) {
                clientesDB.agregarCliente(agregado);
                listaClientes.add(agregado);
                mostrarMensaje("¡Cliente agregado con éxito!");
            }
        });

        ButtonType btnAsignar = new ButtonType("Asignar Venta", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = ButtonType.CANCEL;
        dialogo.getDialogPane().getButtonTypes().addAll(btnAsignar, btnCancelar);

        HBox acciones = new HBox(10, btnNuevo, new Label("Selecciona y haz click en 'Asignar Venta'"));
        content.getChildren().addAll(new Label("Buscar cliente:"), buscar, tabla, acciones);
        dialogo.getDialogPane().setContent(content);

        dialogo.setResultConverter(bt -> bt == btnAsignar ? tabla.getSelectionModel().getSelectedItem() : null);
        return dialogo;
    }

    // --- DIALOGO Confirmar venta y requerir recibo ---
    private Dialog<Boolean> construirDialogoConfirmarVenta(Cliente cliente) {
        Dialog<Boolean> dialogo = new Dialog<>();
        dialogo.setTitle("Confirmar venta");
        VBox content = new VBox(16);
        content.setPadding(new Insets(18));
        ToggleGroup tgRecibo = new ToggleGroup();
        RadioButton rbSi = new RadioButton("Sí"), rbNo = new RadioButton("No");
        rbSi.setToggleGroup(tgRecibo); rbNo.setToggleGroup(tgRecibo);
        rbNo.setSelected(true);

        double totalSinDescuento = carrito.stream().mapToDouble(ProductoEnVenta::getPrecioTotal).sum();

        String resumen = "Cliente: " + cliente.getNombre() +
                "\nTipo: " + cliente.getTipoCliente() +
                "\nTotal Productos: " + carrito.stream().mapToInt(ProductoEnVenta::getCantidad).sum() +
                String.format("\nTotal sin descuento: $ %.2f", totalSinDescuento);

        content.getChildren().addAll(
                new Label("¿Requiere recibo?:"), new HBox(10, rbSi, rbNo),
                new Label("Venta al cliente:"), new Label(cliente.getNombre()),
                new Label("Resumen actual:"), new Label(resumen)
        );

        ButtonType btnOk = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancel = ButtonType.CANCEL;
        dialogo.getDialogPane().getButtonTypes().addAll(btnOk, btnCancel);
        dialogo.getDialogPane().setContent(content);

        dialogo.setResultConverter(bt -> bt == btnOk ? rbSi.isSelected() : null);
        return dialogo;
    }

    // --- Ventana final (emergente) estilo ticket ---
    private void mostrarResumenVenta(Venta venta, Recibo recibo, double descuentoAplicado, double totalFinal, double totalSinDescuento) {
        Dialog<Void> dialogo = new Dialog<>();
        dialogo.setTitle("Resumen y recibo");
        VBox content = new VBox(10);
        content.setPadding(new Insets(18));
        StringBuilder sb = new StringBuilder();

        sb.append("========== FARMACIA SALUD Y VIDA ==========\n");
        sb.append("Fecha: ").append(venta.getFechaVenta()).append("\n");
        sb.append("Venta No.: ").append(venta.getIdVenta()).append("\n");
        sb.append("-------------------------------------------\n");
        sb.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
        sb.append("Teléfono: ").append(venta.getCliente().getNumeroContacto()).append("\n");
        sb.append("Tipo cliente: ").append(venta.getCliente().getTipoCliente()).append("\n");
        sb.append("-------------------------------------------\n\n");
        sb.append(String.format("%-15s %7s %10s\n", "Producto", "Cantidad", "Precio"));
        sb.append("-------------------------------------------\n");

        var conteoProductos = venta.getVenta().stream()
                .collect(Collectors.groupingBy(Producto::getNombre, Collectors.counting()));
        var preciosProductos = venta.getVenta().stream()
                .collect(Collectors.toMap(Producto::getNombre, Producto::getPrecioVenta, (a,b)->a));
        for (var nombreProd : conteoProductos.keySet()) {
            long cantidad = conteoProductos.get(nombreProd);
            double precio = preciosProductos.get(nombreProd);
            sb.append(String.format("%-15s x%-6d $%.2f\n", nombreProd, cantidad, precio));
        }

        sb.append("-------------------------------------------\n");
        sb.append(String.format("Total sin descuento: $%.2f\n", venta.getTotal()));
        sb.append(String.format("Descuento aplicado:  $%.2f\n", venta.getDescuentoAplicado()));
        sb.append(String.format("TOTAL A COBRAR:     $%.2f\n", venta.getTotal() - descuentoAplicado));
        sb.append("===========================================\n");

        Label resumenLbl = new Label(sb.toString());
        resumenLbl.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 13px;");

        Button btnDescargar = new Button("Descargar");
        btnDescargar.setOnAction(e -> {
            if (recibo != null)
                mostrarMensaje(recibo.generarContenido());
            else
                mostrarMensaje(sb.toString());
        });

        Button btnAceptar = new Button("Aceptar");
        btnAceptar.setOnAction(e -> dialogo.close());

        HBox acciones = new HBox(30, btnDescargar, btnAceptar);
        content.getChildren().addAll(resumenLbl, acciones);
        dialogo.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialogo.getDialogPane().setContent(content);
        dialogo.showAndWait();
    }

    private void abrirHistorial() {
        mostrarMensaje("¡Aquí abrirás el historial de ventas!");
    }

    // ---- Clase aux para el carrito ----
    public static class ProductoEnVenta {
        private final Producto producto;
        private int cantidad;
        public ProductoEnVenta(Producto p, int cantidad) {
            this.producto = p;
            this.cantidad = cantidad;
        }
        public Producto getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public int stockVisual() { return producto.getUnidadesExi() - cantidad; }
        public double getPrecioTotal() { return cantidad * producto.getPrecioVenta(); }
    }

    // --- Diálogo para agregar cliente NUEVO desde ventas ---
    /**
     * Muestra diálogo para agregar un cliente nuevo validando todos los campos
     */
    private Cliente mostrarDialogoAgregarCliente() {
        Dialog<Cliente> dialogo = new Dialog<>();
        dialogo.setTitle("Nuevo Cliente");
        VBox content = new VBox(12);
        content.setPadding(new Insets(15));

        TextField txtNombre = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtTelefono = new TextField();
        ComboBox<TipoCliente> cbTipo = new ComboBox<>();
        cbTipo.getItems().setAll(TipoCliente.values());
        cbTipo.getSelectionModel().selectFirst();
        CheckBox cbAmigo = new CheckBox("¿Cliente Amigo?");
        TextField txtDescuento = new TextField("0");

        GridPane grid = new GridPane();
        grid.setVgap(7); grid.setHgap(7);
        grid.add(new Label("Nombre:"), 0, 0);     grid.add(txtNombre, 1, 0);
        grid.add(new Label("Dirección:"), 0, 1);  grid.add(txtDireccion, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);   grid.add(txtTelefono, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3);       grid.add(cbTipo, 1, 3);
        grid.add(new Label("Descuento %:"), 0, 4);grid.add(txtDescuento, 1, 4);
        grid.add(cbAmigo, 1, 5);

        content.getChildren().add(grid);

        dialogo.getDialogPane().setContent(content);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    // Validaciones
                    Verificador.verificarNoVacio(txtNombre.getText(), "nombre");
                    Verificador.verificarNoVacio(txtTelefono.getText(), "teléfono");

                    double descuento = 0;
                    if (!txtDescuento.getText().trim().isEmpty()) {
                        Verificador.verificarNumerico(txtDescuento.getText(), "descuento");
                        descuento = Double.parseDouble(txtDescuento.getText().trim());
                        Verificador.verificarPositivo(descuento, "descuento");
                        Verificador.verificarRango(descuento, 0, 100, "descuento");
                    }

                    // Crear cliente
                    Cliente nuevo = new Cliente(
                            txtNombre.getText().trim(),
                            txtDireccion.getText().trim(),
                            txtTelefono.getText().trim(),
                            cbAmigo.isSelected()
                    );
                    nuevo.setTipoCliente(cbTipo.getValue());
                    nuevo.setPorcentajeDescuento(descuento);
                    nuevo.generarID();
                    return nuevo;

                } catch (CampoVacioException | DatoNoNumericoException | DatoNegativoException e) {
                    mostrarAlertaError(e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialogo.showAndWait().orElse(null);
    }
}