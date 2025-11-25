package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.CampoVacioException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DuplicadoException;
import com.example.sistema_farmacia.controller.verificacionDatos.Verificador;
import com.example.sistema_farmacia.model.clasesdata.CategoriasDB;
import com.example.sistema_farmacia.model.clasesplantillas.Categoria;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class PantallaCategoriasController extends ControladorBase {

    @FXML private Button btnAgregar;
    @FXML private Button btnListar;
    @FXML private VBox areaSubpagina;
    @FXML private GridPane formularioAgregar;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnAceptar;

    private CategoriasDB categoriasDB;

    public void setCategoriasDB(CategoriasDB categoriasDB) {
        this.categoriasDB = categoriasDB;
    }

    @FXML
    public void initialize() {
        btnAgregar.setOnAction(e -> mostrarAgregar());
        btnListar.setOnAction(e -> mostrarListar());
        btnAceptar.setOnAction(e -> agregarCategoria());
        mostrarAgregar();
    }

    /** --- AGREGAR --- **/
    private void mostrarAgregar() {
        areaSubpagina.getChildren().clear();
        areaSubpagina.getChildren().add(formularioAgregar);
    }

    /**
     * Agrega una nueva categoría validando los datos con el Verificador
     */
    private void agregarCategoria() {
        try {
            // 1. Obtener datos del formulario
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            // 2. Validar usando el Verificador
            Verificador.verificarNoVacio(nombre, "nombre de la categoría");

            // 3. Verificar que no exista duplicado
            boolean existe = categoriasDB.getListaCategorias().containsKey(nombre);
            Verificador.verificarDuplicado(existe, "categoría", nombre);

            // 4. Si todo está bien, crear y agregar
            Categoria nueva = new Categoria(nombre, descripcion);
            categoriasDB.agregarCategoria(nueva);

            // 5. Mostrar mensaje de éxito y limpiar
            mostrarExito("Categoría agregada exitosamente:\n" + nombre);
            limpiarFormulario();

        } catch (CampoVacioException | DuplicadoException e) {
            // Capturar excepciones y mostrar error
            mostrarAlertaError(e.getMessage());
        }
    }

    /**
     * Limpia los campos del formulario
     */
    private void limpiarFormulario() {
        txtNombre.clear();
        txtDescripcion.clear();
    }

    /** --- LISTAR Y BUSCAR --- **/
    private void mostrarListar() {
        areaSubpagina.getChildren().clear();

        // Buscador
        HBox buscador = new HBox(12);
        Label lblBuscar = new Label("Buscar:");
        TextField txtBuscar = new TextField();
        Button btnBuscar = new Button("Buscar");
        buscador.getChildren().addAll(lblBuscar, txtBuscar, btnBuscar);

        TableView<Categoria> tabla = new TableView<>();
        tabla.setPrefHeight(220);

        TableColumn<Categoria, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoriaNombre()));

        TableColumn<Categoria, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));

        TableColumn<Categoria, Void> colOpciones = new TableColumn<>("Opciones");
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

        tabla.getColumns().addAll(colNombre, colDescripcion, colOpciones);

        // Cargar datos
        List<Categoria> todas = new ArrayList<>(categoriasDB.getListaCategorias().values());
        tabla.setItems(FXCollections.observableArrayList(todas));

        // Filtro por nombre
        btnBuscar.setOnAction(e -> filtrarCategorias(txtBuscar.getText(), tabla));

        VBox listado = new VBox(12, buscador, tabla);
        listado.setPadding(new Insets(10));
        areaSubpagina.getChildren().add(listado);
    }

    /**
     * Filtra las categorías según el texto de búsqueda
     */
    private void filtrarCategorias(String textoBusqueda, TableView<Categoria> tabla) {
        String filtro = textoBusqueda.trim().toLowerCase();

        if (filtro.isEmpty()) {
            // Mostrar todas si no hay filtro
            tabla.setItems(FXCollections.observableArrayList(categoriasDB.getListaCategorias().values()));
        } else {
            // Filtrar por nombre
            ObservableList<Categoria> filtradas = FXCollections.observableArrayList();
            for (Categoria cat : categoriasDB.getListaCategorias().values()) {
                if (cat.getCategoriaNombre().toLowerCase().contains(filtro)) {
                    filtradas.add(cat);
                }
            }
            tabla.setItems(filtradas);
        }
    }

    /** --- MODIFICAR --- **/
    private void mostrarModificar(Categoria cat) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modificar Categoría");

        GridPane grid = new GridPane();
        grid.setVgap(10); grid.setHgap(12); grid.setPadding(new Insets(20));

        TextField nuevoNombre = new TextField(cat.getCategoriaNombre());
        TextArea nuevaDescripcion = new TextArea(cat.getDescripcion());

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nuevoNombre, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(nuevaDescripcion, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> bt);
        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                modificarCategoria(cat, nuevoNombre.getText(), nuevaDescripcion.getText());
            }
        });
    }

    /**
     * Modifica una categoría validando los datos
     */
    private void modificarCategoria(Categoria categoria, String nuevoNombre, String nuevaDescripcion) {
        try {
            // Validar nuevo nombre
            Verificador.verificarNoVacio(nuevoNombre, "nombre");

            String nombreViejo = categoria.getCategoriaNombre();
            String nombreNuevo = nuevoNombre.trim();

            // Si cambió el nombre, verificar que no exista otro con ese nombre
            if (!nombreViejo.equalsIgnoreCase(nombreNuevo)) {
                boolean existe = categoriasDB.getListaCategorias().containsKey(nombreNuevo);
                Verificador.verificarDuplicado(existe, "categoría", nombreNuevo);
                categoria.setCategoriaNombre(nombreNuevo);
            }

            // Actualizar descripción
            categoria.setDescripcion(nuevaDescripcion.trim());

            // Guardar cambios
            categoriasDB.modificarCategoria(nombreViejo, categoria);

            mostrarExito("Categoría modificada exitosamente");
            mostrarListar();

        } catch (CampoVacioException | DuplicadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    /** --- ELIMINAR --- **/
    private void mostrarEliminar(Categoria cat) {
        boolean confirmar = mostrarConfirmacion(
                "¿Seguro que quieres eliminar la categoría?\n\n" +
                        "Categoría: " + cat.getCategoriaNombre()
        );

        if (confirmar) {
            eliminarCategoria(cat);
        }
    }

    /**
     * Elimina una categoría
     */
    private void eliminarCategoria(Categoria categoria) {
        try {
            categoriasDB.eliminarCategoria(categoria.getCategoriaNombre());
            mostrarExito("Categoría eliminada exitosamente");
            mostrarListar();
        } catch (Exception e) {
            mostrarAlertaError("Error al eliminar: " + e.getMessage());
        }
    }
}