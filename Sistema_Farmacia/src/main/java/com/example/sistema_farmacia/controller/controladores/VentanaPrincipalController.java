package com.example.sistema_farmacia.controller.controladores;

import com.example.sistema_farmacia.model.clasesdata.CategoriasDB;
import com.example.sistema_farmacia.model.clasesdata.ClientesDB;
import com.example.sistema_farmacia.model.clasesdata.ProductosDB;
import com.example.sistema_farmacia.model.clasesdata.VentasDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class VentanaPrincipalController {

    @FXML private BorderPane rootPane;
    @FXML private AnchorPane contenidoCentral;

    private final CategoriasDB categoriasDB = new CategoriasDB();
    private final ClientesDB clientesDB = new ClientesDB();
    private final ProductosDB productosDB = new ProductosDB();
    private final VentasDB ventasDB = new VentasDB(); // NUEVO

    @FXML
    public void initialize() {
        try {
            FXMLLoader loaderMenu = new FXMLLoader(getClass().getResource(
                    "/com/example/sistema_farmacia/view/navegacion/MenuPrincipal.fxml"));
            Parent menu = loaderMenu.load();
            MenuPrincipalController menuController = loaderMenu.getController();
            menuController.setVentanaPrincipalController(this);
            rootPane.setTop(menu);

            // Ahora muestra ventas como pantalla principal
            mostrarPantalla("/com/example/sistema_farmacia/view/Ventas/PantallaVentas.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarPantalla(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent pantalla = loader.load();

            if (rutaFXML.endsWith("PantallaCategorias.fxml")) {
                PantallaCategoriasController controller = loader.getController();
                controller.setCategoriasDB(categoriasDB);
            }
            if (rutaFXML.endsWith("PantallaClientes.fxml")) {
                PantallaClientesController controller = loader.getController();
                controller.setClientesDB(clientesDB);
            }
            if (rutaFXML.endsWith("PantallaProductos.fxml")) {
                PantallaProductosController controller = loader.getController();
                controller.setProductosDB(productosDB);
                controller.setCategoriasDB(categoriasDB);
            }
            if (rutaFXML.endsWith("PantallaVentas.fxml")) {
                PantallaVentasController controller = loader.getController();
                controller.setProductosDB(productosDB);
                controller.setClientesDB(clientesDB);
                controller.setVentasDB(ventasDB);
                controller.setCategoriasDB(categoriasDB);
            }
            contenidoCentral.getChildren().setAll(pantalla);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

