package com.example.sistema_farmacia.controller.controladores;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

//Clase base para todos los controladores del sistema.
public abstract class ControladorBase {

    /**
     * Muestra un mensaje informativo al usuario
     * @param mensaje El mensaje a mostrar
     */
    protected void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Mensaje del sistema");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error al usuario
     * @param mensajeError El mensaje de error a mostrar
     */
    protected void mostrarAlertaError(String mensajeError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensajeError);
        alert.showAndWait();
    }

    /**
     * Muestra una advertencia al usuario
     * @param mensajeAdvertencia El mensaje de advertencia
     */
    protected void mostrarAdvertencia(String mensajeAdvertencia) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText("Atención");
        alert.setContentText(mensajeAdvertencia);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación y devuelve la respuesta
     * @param mensaje El mensaje de confirmación
     * @return true si el usuario aceptó, false si canceló
     */
    protected boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText(mensaje);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    /**
     * Muestra un mensaje de éxito (información con estilo positivo)
     * @param mensaje El mensaje de éxito
     */
    protected void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText("Operación exitosa");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}