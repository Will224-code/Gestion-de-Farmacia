package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.CampoVacioException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DatoNoNumericoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DatoNegativoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DuplicadoException;
import com.example.sistema_farmacia.controller.verificacionDatos.Verificador;
import com.example.sistema_farmacia.model.clasesdata.ClientesDB;
import com.example.sistema_farmacia.model.clasesplantillas.Cliente;
import com.example.sistema_farmacia.model.clasesplantillas.TipoCliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class PantallaClientesController extends ControladorBase {

    @FXML private Button btnAgregar;
    @FXML private Button btnListar;
    @FXML private VBox areaSubpagina;
    @FXML private GridPane formularioAgregar;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtNumeroContacto;
    @FXML private CheckBox chkClienteAmigo;
    @FXML private ComboBox<TipoCliente> cmbTipoCliente;
    @FXML private TextField txtDescuento;
    @FXML private Button btnAceptar;

    private TableView<Cliente> tablaClientes;
    private TextField txtBuscarCliente;
    private Button btnBuscarCliente;

    private ClientesDB clientesDB;

    public void setClientesDB(ClientesDB clientesDB) {
        this.clientesDB = clientesDB;
    }

    @FXML
    public void initialize() {
        cmbTipoCliente.getItems().setAll(TipoCliente.values());
        btnAceptar.setOnAction(e -> agregarCliente());
        btnAgregar.setOnAction(e -> mostrarAgregar());
        btnListar.setOnAction(e -> mostrarListar());
        mostrarAgregar();
    }

    // --- Agregar Cliente ---
    private void mostrarAgregar() {
        areaSubpagina.getChildren().clear();
        areaSubpagina.getChildren().add(formularioAgregar);
    }

    /**
     * Agrega un nuevo cliente validando todos los campos
     */
    private void agregarCliente() {
        try {
            // 1. Obtener datos del formulario
            String nombre = txtNombre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String contacto = txtNumeroContacto.getText().trim();
            boolean esAmigo = chkClienteAmigo.isSelected();
            TipoCliente tipo = cmbTipoCliente.getValue();
            String descuentoTexto = txtDescuento.getText().trim();

            // 2. Validar campos obligatorios
            Verificador.verificarNoVacio(nombre, "nombre");
            Verificador.verificarNoVacio(direccion, "dirección");
            Verificador.verificarNoVacio(contacto, "número de contacto");

            if (tipo == null) {
                throw new CampoVacioException("Debes seleccionar un tipo de cliente");
            }

            // 3. Validar descuento (si se ingresó)
            double descuento = 0;
            if (!descuentoTexto.isEmpty()) {
                Verificador.verificarNumerico(descuentoTexto, "descuento");
                descuento = Double.parseDouble(descuentoTexto);
                Verificador.verificarPositivo(descuento, "descuento");
                Verificador.verificarRango(descuento, 0, 100, "descuento");
            }

            // 4. Verificar duplicado por número de contacto
            boolean existeContacto = clientesDB.getListaClientes().values().stream()
                    .anyMatch(c -> c.getNumeroContacto().equals(contacto));
            Verificador.verificarDuplicado(existeContacto, "número de contacto", contacto);

            // 5. Crear y agregar cliente
            Cliente nuevo = new Cliente(nombre, direccion, contacto, esAmigo);
            nuevo.setTipoCliente(tipo);
            nuevo.setPorcentajeDescuento(descuento);
            clientesDB.agregarCliente(nuevo);

            mostrarExito("Cliente agregado correctamente");
            limpiarFormulario();

        } catch (CampoVacioException | DatoNoNumericoException |
                 DatoNegativoException | DuplicadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtNombre.clear();
        txtDireccion.clear();
        txtNumeroContacto.clear();
        chkClienteAmigo.setSelected(false);
        cmbTipoCliente.setValue(null);
        txtDescuento.clear();
    }

    // --- Listar Clientes ---
    private void mostrarListar() {
        areaSubpagina.getChildren().clear();

        HBox buscador = new HBox(8);
        txtBuscarCliente = new TextField();
        txtBuscarCliente.setPromptText("Nombre (buscar insensible a mayúsculas)");
        btnBuscarCliente = new Button("Buscar");
        buscador.getChildren().addAll(new Label("Buscar:"), txtBuscarCliente, btnBuscarCliente);

        tablaClientes = new TableView<>();
        tablaClientes.setPrefHeight(220);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<Cliente, String> colNumero = new TableColumn<>("Número de contacto");
        colNumero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroContacto()));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo de cliente");
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTipoCliente() != null
                        ? data.getValue().getTipoCliente().getTipo()
                        : ""
        ));

        TableColumn<Cliente, Void> colOpciones = new TableColumn<>("Opciones");
        colOpciones.setCellFactory(tc -> new TableCell<>() {
            private final Button btnModificar = new Button("Modificar");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnModificar.setOnAction(e -> mostrarModificar(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> mostrarEliminar(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(6, btnModificar, btnEliminar));
            }
        });

        tablaClientes.getColumns().addAll(colNombre, colNumero, colTipo, colOpciones);

        btnBuscarCliente.setOnAction(e -> buscarCliente());
        txtBuscarCliente.setOnAction(e -> buscarCliente());

        refrescarTabla();

        VBox listado = new VBox(12, buscador, tablaClientes);
        listado.setPadding(new Insets(10));
        areaSubpagina.getChildren().add(listado);
    }

    private void refrescarTabla() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(
                clientesDB.getListaClientes().values()
        );
        tablaClientes.setItems(clientes);
    }

    private void buscarCliente() {
        String filtro = txtBuscarCliente.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            refrescarTabla();
            return;
        }

        ArrayList<Cliente> filtrados = new ArrayList<>();
        for (Cliente cli : clientesDB.getListaClientes().values()) {
            if (cli.getNombre().toLowerCase().contains(filtro)) {
                filtrados.add(cli);
            }
        }
        tablaClientes.setItems(FXCollections.observableArrayList(filtrados));
    }

    private void mostrarModificar(Cliente cliente) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modificar Cliente");

        GridPane grid = new GridPane();
        grid.setVgap(8); grid.setHgap(10); grid.setPadding(new Insets(15));

        TextField campoNombre = new TextField(cliente.getNombre());
        TextField campoDir = new TextField(cliente.getDireccion());
        TextField campoNum = new TextField(cliente.getNumeroContacto());
        ComboBox<TipoCliente> comboTipo = new ComboBox<>(
                FXCollections.observableArrayList(TipoCliente.values())
        );
        comboTipo.setValue(cliente.getTipoCliente());
        CheckBox chkAmigo = new CheckBox("Cliente amigo");
        chkAmigo.setSelected(cliente.getEsClienteAmigo());
        TextField campoDesc = new TextField(String.valueOf(cliente.getPorcentajeDescuento()));

        grid.add(new Label("Nombre:"), 0, 0); grid.add(campoNombre, 1, 0);
        grid.add(new Label("Dirección:"), 0, 1); grid.add(campoDir, 1, 1);
        grid.add(new Label("Número de contacto:"), 0, 2); grid.add(campoNum, 1, 2);
        grid.add(new Label("Tipo de cliente:"), 0, 3); grid.add(comboTipo, 1, 3);
        grid.add(chkAmigo, 1, 4);
        grid.add(new Label("Descuento:"), 0, 5); grid.add(campoDesc, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> bt);
        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                modificarCliente(cliente, campoNombre.getText(), campoDir.getText(),
                        campoNum.getText(), comboTipo.getValue(),
                        chkAmigo.isSelected(), campoDesc.getText());
            }
        });
    }

    /**
     * Modifica un cliente validando los nuevos datos
     */
    private void modificarCliente(Cliente cliente, String nuevoNombre, String nuevaDireccion,
                                  String nuevoContacto, TipoCliente nuevoTipo,
                                  boolean esAmigo, String nuevoDescuentoTexto) {
        try {
            boolean cambiado = false;

            // Validar y actualizar nombre
            if (!nuevoNombre.trim().equals(cliente.getNombre())) {
                Verificador.verificarNoVacio(nuevoNombre, "nombre");
                cliente.setNombre(nuevoNombre.trim());
                cambiado = true;
            }

            // Validar y actualizar dirección
            if (!nuevaDireccion.trim().equals(cliente.getDireccion())) {
                Verificador.verificarNoVacio(nuevaDireccion, "dirección");
                cliente.setDireccion(nuevaDireccion.trim());
                cambiado = true;
            }

            // Validar y actualizar contacto
            if (!nuevoContacto.trim().equals(cliente.getNumeroContacto())) {
                Verificador.verificarNoVacio(nuevoContacto, "contacto");
                // Verificar que no exista otro cliente con ese contacto
                boolean existeContacto = clientesDB.getListaClientes().values().stream()
                        .anyMatch(c -> !c.getNombre().equals(cliente.getNombre()) &&
                                c.getNumeroContacto().equals(nuevoContacto.trim()));
                Verificador.verificarDuplicado(existeContacto, "número de contacto", nuevoContacto);
                cliente.setNumeroContacto(nuevoContacto.trim());
                cambiado = true;
            }

            // Actualizar tipo
            if (nuevoTipo != cliente.getTipoCliente()) {
                cliente.setTipoCliente(nuevoTipo);
                cambiado = true;
            }

            // Actualizar cliente amigo
            if (esAmigo != cliente.getEsClienteAmigo()) {
                cliente.setEsClienteAmigo(esAmigo);
                cambiado = true;
            }

            // Validar y actualizar descuento
            if (!nuevoDescuentoTexto.trim().isEmpty()) {
                Verificador.verificarNumerico(nuevoDescuentoTexto, "descuento");
                double descuento = Double.parseDouble(nuevoDescuentoTexto.trim());
                Verificador.verificarPositivo(descuento, "descuento");
                Verificador.verificarRango(descuento, 0, 100, "descuento");

                if (descuento != cliente.getPorcentajeDescuento()) {
                    cliente.setPorcentajeDescuento(descuento);
                    cambiado = true;
                }
            }

            if (cambiado) {
                clientesDB.modificarCliente(cliente.getNombre(), cliente);
                mostrarExito("Cliente modificado exitosamente");
            } else {
                mostrarMensaje("No se realizaron cambios");
            }

            refrescarTabla();

        } catch (CampoVacioException | DatoNoNumericoException |
                 DatoNegativoException | DuplicadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    private void mostrarEliminar(Cliente cliente) {
        boolean confirmar = mostrarConfirmacion(
                "¿Seguro que quieres eliminar este cliente?\n\n" +
                        "Cliente: " + cliente.getNombre()
        );

        if (confirmar) {
            eliminarCliente(cliente);
        }
    }

    /**
     * Elimina un cliente
     */
    private void eliminarCliente(Cliente cliente) {
        try {
            clientesDB.eliminarCliente(cliente.getNombre());
            mostrarExito("Cliente eliminado exitosamente");
            refrescarTabla();
        } catch (Exception e) {
            mostrarAlertaError("Error al eliminar: " + e.getMessage());
        }
    }
}