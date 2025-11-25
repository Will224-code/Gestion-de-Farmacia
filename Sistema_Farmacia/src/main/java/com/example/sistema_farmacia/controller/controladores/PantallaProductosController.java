package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.CampoVacioException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DatoNoNumericoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DatoNegativoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesPrincipales.DuplicadoException;
import com.example.sistema_farmacia.controller.Excepciones.excepcionesProductos.FechaCaducidadException;
import com.example.sistema_farmacia.controller.verificacionDatos.Verificador;
import com.example.sistema_farmacia.model.clasesdata.CategoriasDB;
import com.example.sistema_farmacia.model.clasesdata.ProductosDB;
import com.example.sistema_farmacia.model.clasesplantillas.Categoria;
import com.example.sistema_farmacia.model.clasesplantillas.Producto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PantallaProductosController extends ControladorBase {

    @FXML private Button btnAgregar, btnListar;
    @FXML private VBox areaSubpagina;

    private ProductosDB productosDB;
    private CategoriasDB categoriasDB;

    // Referencias para "Agregar"
    private GridPane formularioAgregarProducto;
    private TextField txtNombre, txtDescripcion, txtCodigo, txtPrecio, txtPrecioVenta, txtUnidades;
    private DatePicker dpCaducidad;
    private VBox boxCheckCategorias;
    private Button btnAceptar;

    // Referencias para "Listar"
    private VBox boxListado;
    private TableView<Producto> tablaProductos;
    private TextField txtBuscarCodigo;

    public void setProductosDB(ProductosDB productosDB) { this.productosDB = productosDB; }
    public void setCategoriasDB(CategoriasDB categoriasDB) { this.categoriasDB = categoriasDB; }

    @FXML
    public void initialize() {
        btnAgregar.setOnAction(e -> mostrarAgregar());
        btnListar.setOnAction(e -> mostrarListar());
        mostrarAgregar();
    }

    //------------ SUBPÁGINA AGREGAR --------------//
    private void mostrarAgregar() {
        areaSubpagina.getChildren().clear();

        if (formularioAgregarProducto == null) {
            formularioAgregarProducto = new GridPane();
            formularioAgregarProducto.setHgap(12); formularioAgregarProducto.setVgap(11);

            txtNombre = new TextField();
            txtDescripcion = new TextField();
            txtCodigo = new TextField();
            dpCaducidad = new DatePicker();
            txtPrecio = new TextField();
            txtPrecioVenta = new TextField();
            txtUnidades = new TextField();
            boxCheckCategorias = new VBox(5);
            btnAceptar = new Button("Aceptar");

            formularioAgregarProducto.addRow(0, new Label("Nombre:"), txtNombre);
            formularioAgregarProducto.addRow(1, new Label("Descripción:"), txtDescripcion);
            formularioAgregarProducto.addRow(2, new Label("Código:"), txtCodigo);
            formularioAgregarProducto.addRow(3, new Label("Fecha Caducidad:"), dpCaducidad);
            formularioAgregarProducto.addRow(4, new Label("Precio normal:"), txtPrecio);
            formularioAgregarProducto.addRow(5, new Label("Precio venta especial:"), txtPrecioVenta);
            formularioAgregarProducto.addRow(6, new Label("Unidades disponibles:"), txtUnidades);
            formularioAgregarProducto.addRow(7, new Label("Categorías:"), boxCheckCategorias);
            formularioAgregarProducto.addRow(8, new Label(), btnAceptar);
        }

        cargarCategoriasCheck();

        btnAceptar.setOnAction(e -> agregarProductoCheck());
        areaSubpagina.getChildren().setAll(formularioAgregarProducto);
    }

    private void cargarCategoriasCheck() {
        if (categoriasDB != null && boxCheckCategorias != null) {
            boxCheckCategorias.getChildren().clear();
            for (Categoria categoria : categoriasDB.getCategoriasParaProductos()) {
                CheckBox check = new CheckBox(categoria.getCategoriaNombre());
                check.setUserData(categoria);
                boxCheckCategorias.getChildren().add(check);
            }
        }
    }

    /**
     * Agrega el producto validando todos los campos
     */
    private void agregarProductoCheck() {
        try {
            // 1. Obtener datos
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String codigo = txtCodigo.getText().trim();
            LocalDate caducidad = dpCaducidad.getValue();
            String precioTexto = txtPrecio.getText().trim();
            String precioVentaTexto = txtPrecioVenta.getText().trim();
            String unidadesTexto = txtUnidades.getText().trim();

            // 2. Validaciones básicas
            Verificador.verificarNoVacio(nombre, "nombre");
            Verificador.verificarNoVacio(descripcion, "descripción");
            Verificador.verificarNoVacio(codigo, "código");

            if (caducidad == null) {
                throw new CampoVacioException("La fecha de caducidad es requerida");
            }

            Verificador.verificarNoVacio(precioTexto, "precio normal");
            Verificador.verificarNoVacio(precioVentaTexto, "precio venta especial");
            Verificador.verificarNoVacio(unidadesTexto, "unidades disponibles");

            // 3. Validar tipos numéricos
            Verificador.verificarNumerico(precioTexto, "precio normal");
            Verificador.verificarNumerico(precioVentaTexto, "precio venta especial");
            Verificador.verificarEntero(unidadesTexto, "unidades disponibles");

            double precio = Double.parseDouble(precioTexto);
            double precioVenta = Double.parseDouble(precioVentaTexto);
            int unidades = Integer.parseInt(unidadesTexto);

            // 4. Validar valores positivos
            Verificador.verificarPositivo(precio, "precio normal");
            Verificador.verificarPositivo(precioVenta, "precio venta especial");
            Verificador.verificarPositivo(unidades, "unidades disponibles");

            // 5. Validar fecha de caducidad
            Verificador.verificarFechaCaducidad(caducidad);

            // 6. Verificar duplicado de código
            boolean existe = productosDB.getListaProductos().containsKey(codigo);
            Verificador.verificarDuplicado(existe, "código de producto", codigo);

            // 7. Obtener categorías seleccionadas
            ArrayList<Categoria> categoriasProducto = new ArrayList<>();
            for (javafx.scene.Node nodo : boxCheckCategorias.getChildren()) {
                if (nodo instanceof CheckBox cb && cb.isSelected()) {
                    categoriasProducto.add((Categoria) cb.getUserData());
                }
            }
            if (categoriasProducto.isEmpty() && categoriasDB != null) {
                categoriasProducto.add(categoriasDB.getCategoriaDefault());
            }

            // 8. Crear y agregar producto
            Producto nuevo = new Producto(nombre, descripcion, caducidad, precio, precioVenta, unidades, codigo);
            nuevo.setCategoria(categoriasProducto);
            productosDB.agregarProducto(nuevo);

            mostrarExito("Producto agregado correctamente");
            limpiarFormularioCheck();
            cargarCategoriasCheck();

        } catch (CampoVacioException | DatoNoNumericoException | DatoNegativoException |
                 FechaCaducidadException | DuplicadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    private void limpiarFormularioCheck() {
        txtNombre.clear(); txtDescripcion.clear(); txtCodigo.clear(); dpCaducidad.setValue(null);
        txtPrecio.clear(); txtPrecioVenta.clear(); txtUnidades.clear();
        for (javafx.scene.Node nodo : boxCheckCategorias.getChildren()) {
            if (nodo instanceof CheckBox cb) cb.setSelected(false);
        }
    }

    //------------ SUBPÁGINA LISTAR --------------//
    private void mostrarListar() {
        areaSubpagina.getChildren().clear();

        if (boxListado == null) {
            boxListado = new VBox(12);

            HBox buscador = new HBox(8);
            txtBuscarCodigo = new TextField();
            txtBuscarCodigo.setPromptText("Código");
            Button btnBuscar = new Button("Buscar");
            buscador.getChildren().addAll(new Label("Buscar:"), txtBuscarCodigo, btnBuscar);

            tablaProductos = new TableView<>();
            tablaProductos.setPrefHeight(220);

            TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
            colCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigo()));

            TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

            TableColumn<Producto, String> colDescripcion = new TableColumn<>("Descripción");
            colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));

            TableColumn<Producto, String> colCategorias = new TableColumn<>("Categorías");
            colCategorias.setCellValueFactory(data -> {
                ArrayList<Categoria> cats = data.getValue().getCategoria();
                String s = cats.stream().map(Categoria::getCategoriaNombre).collect(Collectors.joining(", "));
                return new SimpleStringProperty(s);
            });

            TableColumn<Producto, String> colPrecioVenta = new TableColumn<>("Precio Venta");
            colPrecioVenta.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPrecioVenta())));

            TableColumn<Producto, String> colStock = new TableColumn<>("Stock");
            colStock.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getUnidadesExi())));

            TableColumn<Producto, Void> colOpciones = new TableColumn<>("Opciones");
            colOpciones.setCellFactory(tc -> new TableCell<>() {
                private final Button btnModificar = new Button("Modificar");
                private final Button btnEliminar = new Button("Eliminar");
                {
                    btnModificar.setOnAction(e -> mostrarModificar(getTableView().getItems().get(getIndex())));
                    btnEliminar.setOnAction(e -> mostrarEliminar(getTableView().getItems().get(getIndex())));
                    btnModificar.setPadding(new Insets(2,8,2,8));
                    btnEliminar.setPadding(new Insets(2,8,2,8));
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : new HBox(6, btnModificar, btnEliminar));
                }
            });

            tablaProductos.getColumns().addAll(
                    colCodigo, colNombre, colDescripcion, colCategorias,
                    colPrecioVenta, colStock, colOpciones
            );

            btnBuscar.setOnAction(e -> buscarProducto());
            txtBuscarCodigo.setOnAction(e -> buscarProducto());

            boxListado.getChildren().addAll(buscador, tablaProductos);
        }

        refrescarTabla();
        areaSubpagina.getChildren().setAll(boxListado);
    }

    private void buscarProducto() {
        if (productosDB == null) return;
        String filtro = txtBuscarCodigo.getText().trim().toLowerCase();
        ObservableList<Producto> todos = FXCollections.observableArrayList(productosDB.getListaProductos().values());
        ObservableList<Producto> filtrados = FXCollections.observableArrayList();
        if (!filtro.isEmpty()) {
            for (Producto pr : todos) {
                if (pr.getCodigo().toLowerCase().contains(filtro))
                    filtrados.add(pr);
            }
        }
        tablaProductos.setItems(filtro.isEmpty() ? todos : filtrados);
    }

    private void refrescarTabla() {
        if (productosDB != null && tablaProductos != null)
            tablaProductos.setItems(FXCollections.observableArrayList(productosDB.getListaProductos().values()));
    }

    private void mostrarModificar(Producto producto) {
        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Modificar Producto");

        GridPane grid = new GridPane();
        grid.setVgap(8); grid.setHgap(10); grid.setPadding(new Insets(15));

        TextField campoNombre = new TextField(producto.getNombre());
        TextField campoDescripcion = new TextField(producto.getDescripcion());
        TextField campoPrecio = new TextField(String.valueOf(producto.getPrecio()));
        TextField campoPrecioVenta = new TextField(String.valueOf(producto.getPrecioVenta()));
        TextField campoCodigo = new TextField(producto.getCodigo());
        DatePicker campoCaducidad = new DatePicker(producto.getFechaCaducidad());
        TextField campoUnidades = new TextField(String.valueOf(producto.getUnidadesExi()));

        Label labelCategorias = new Label(
                producto.getCategoria().stream().map(Categoria::getCategoriaNombre).collect(Collectors.joining(", "))
        );

        grid.add(new Label("Nombre:"), 0, 0); grid.add(campoNombre, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1); grid.add(campoDescripcion, 1, 1);
        grid.add(new Label("Código:"), 0, 2); grid.add(campoCodigo, 1, 2);
        grid.add(new Label("Fecha Caducidad:"), 0, 3); grid.add(campoCaducidad, 1, 3);
        grid.add(new Label("Precio normal:"), 0, 4); grid.add(campoPrecio, 1, 4);
        grid.add(new Label("Precio venta:"), 0, 5); grid.add(campoPrecioVenta, 1, 5);
        grid.add(new Label("Unidades:"), 0, 6); grid.add(campoUnidades, 1, 6);
        grid.add(new Label("Categorías actuales:"), 0, 7); grid.add(labelCategorias, 1, 7);

        dialogo.getDialogPane().setContent(grid);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                modificarProducto(producto, campoNombre.getText(), campoDescripcion.getText(),
                        campoCodigo.getText(), campoPrecio.getText(), campoPrecioVenta.getText(),
                        campoUnidades.getText(), campoCaducidad.getValue());
            }
        });
    }

    /**
     * Modifica un producto validando los nuevos datos
     */
    private void modificarProducto(Producto producto, String nuevoNombre, String nuevaDescripcion,
                                   String nuevoCodigo, String nuevoPrecio, String nuevoPrecioVenta,
                                   String nuevasUnidades, LocalDate nuevaCaducidad) {
        try {
            boolean cambiado = false;

            // Validar y actualizar nombre
            if (!nuevoNombre.trim().equals(producto.getNombre())) {
                Verificador.verificarNoVacio(nuevoNombre, "nombre");
                producto.setNombre(nuevoNombre.trim());
                cambiado = true;
            }

            // Validar y actualizar descripción
            if (!nuevaDescripcion.trim().equals(producto.getDescripcion())) {
                Verificador.verificarNoVacio(nuevaDescripcion, "descripción");
                producto.setDescripcion(nuevaDescripcion.trim());
                cambiado = true;
            }

            // Validar y actualizar código (eliminar viejo si cambia)
            if (!nuevoCodigo.trim().equals(producto.getCodigo())) {
                Verificador.verificarNoVacio(nuevoCodigo, "código");
                boolean existe = productosDB.getListaProductos().containsKey(nuevoCodigo.trim());
                Verificador.verificarDuplicado(existe, "código de producto", nuevoCodigo);
                productosDB.eliminarProducto(producto.getCodigo());
                producto.setCodigo(nuevoCodigo.trim());
                cambiado = true;
            }

            // Validar y actualizar precio
            if (!nuevoPrecio.trim().isEmpty()) {
                Verificador.verificarNumerico(nuevoPrecio, "precio normal");
                double precio = Double.parseDouble(nuevoPrecio.trim());
                Verificador.verificarPositivo(precio, "precio normal");
                if (precio != producto.getPrecio()) {
                    producto.setPrecio(precio);
                    cambiado = true;
                }
            }

            // Validar y actualizar precio venta
            if (!nuevoPrecioVenta.trim().isEmpty()) {
                Verificador.verificarNumerico(nuevoPrecioVenta, "precio venta");
                double precioVenta = Double.parseDouble(nuevoPrecioVenta.trim());
                Verificador.verificarPositivo(precioVenta, "precio venta");
                if (precioVenta != producto.getPrecioVenta()) {
                    producto.setPrecioVenta(precioVenta);
                    cambiado = true;
                }
            }

            // Validar y actualizar unidades
            if (!nuevasUnidades.trim().isEmpty()) {
                Verificador.verificarEntero(nuevasUnidades, "unidades");
                int unidades = Integer.parseInt(nuevasUnidades.trim());
                Verificador.verificarPositivo(unidades, "unidades");
                if (unidades != producto.getUnidadesExi()) {
                    producto.setUnidadesExi(unidades);
                    cambiado = true;
                }
            }

            // Validar y actualizar fecha de caducidad
            if (nuevaCaducidad != null && !nuevaCaducidad.equals(producto.getFechaCaducidad())) {
                Verificador.verificarFechaCaducidad(nuevaCaducidad);
                producto.setFechaCaducidad(nuevaCaducidad);
                cambiado = true;
            }

            if (cambiado) {
                productosDB.agregarProducto(producto);
                mostrarExito("Producto modificado exitosamente");
                refrescarTabla();
            } else {
                mostrarMensaje("No se realizaron cambios");
            }

        } catch (CampoVacioException | DatoNoNumericoException | DatoNegativoException |
                 FechaCaducidadException | DuplicadoException e) {
            mostrarAlertaError(e.getMessage());
        }
    }

    private void mostrarEliminar(Producto producto) {
        boolean confirmar = mostrarConfirmacion(
                "¿Seguro que quieres eliminar este producto?\n\n" +
                        "Producto: " + producto.getNombre()
        );

        if (confirmar) {
            eliminarProducto(producto);
        }
    }

    /**
     * Elimina un producto
     */
    private void eliminarProducto(Producto producto) {
        try {
            productosDB.eliminarProducto(producto.getCodigo());
            mostrarExito("Producto eliminado exitosamente");
            refrescarTabla();
        } catch (Exception e) {
            mostrarAlertaError("Error al eliminar: " + e.getMessage());
        }
    }
}