package com.example.sistema_farmacia.controller.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuPrincipalController {

    @FXML private Button btnVentas;
    @FXML private Button btnCategorias;
    @FXML private Button btnClientes;
    @FXML private Button btnProductos;
    @FXML private Button btnRecibos;
    @FXML private Button btnReportes;
    @FXML private Button btnUsuariosSesion;

    private VentanaPrincipalController ventanaPrincipalController;

    public void setVentanaPrincipalController(VentanaPrincipalController ref) {
        this.ventanaPrincipalController = ref;
    }

    @FXML
    public void initialize() {
        btnVentas.setOnAction(e -> navegarAVentas());
        btnCategorias.setOnAction(e -> navegarACategorias());
        btnClientes.setOnAction(e -> navegarAClientes());
        btnProductos.setOnAction(e -> navegarAProductos());
        btnRecibos.setOnAction(e -> navegarARecibos());
        btnReportes.setOnAction(e -> navegarAReportes());
        btnUsuariosSesion.setOnAction(e -> navegarAUsuariosSesion());
    }

    private void navegarAVentas() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/Ventas/PantallaVentas.fxml");
    }
    private void navegarACategorias() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/categorias/PantallaCategorias.fxml");
    }
    private void navegarAClientes() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/clientes/PantallaClientes.fxml");
    }
    private void navegarAProductos() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/productos/PantallaProductos.fxml");
    }
    private void navegarARecibos() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/recibos/PantallaRecibos.fxml");
    }
    private void navegarAReportes() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/reportes/PantallaReportes.fxml");
    }
    private void navegarAUsuariosSesion() {
        if (ventanaPrincipalController != null)
            ventanaPrincipalController.mostrarPantalla("/com/example/sistema_farmacia/view/usuarios/PantallaUsuariosSesion.fxml");
    }
}
