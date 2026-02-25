package br.com.dbank.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminLoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    @FXML
    public void handleLogin() {
        if (txtUsuario.getText().equals("admin") && txtSenha.getText().equals("admin123")) {
            abrirPainelAdmin();
        } else {
            new Alert(Alert.AlertType.ERROR, "Acesso Negado!").show();
        }
    }

    private void abrirPainelAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("DBank - Gestão de Dados");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}