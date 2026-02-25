package br.com.dbank.controller;

import br.com.dbank.model.Conta;
import br.com.dbank.service.ContaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField; // Importante para a senha
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtNumeroConta;
    @FXML private PasswordField txtSenha;

    private ContaService contaService = new ContaService();

    @FXML
    public void Login() {
        try {
            String num = txtNumeroConta.getText();
            String senha = txtSenha.getText();

            if (num.isEmpty() || senha.isEmpty()) {
                mostrarAlerta("Erro", "Por favor, informe a conta e a senha!", Alert.AlertType.WARNING);
                return;
            }

            Conta contaLogada = contaService.loginComSenha(num, senha);

            if (contaLogada != null) {
                irParaMenuPrincipal(contaLogada);
            } else {
                mostrarAlerta("Erro", "Conta ou senha incorretos.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao processar login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    void irParaMenuPrincipal(Conta conta) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuPrincipal.fxml"));
        Parent root = loader.load();

        MenuController menuCtrl = loader.getController();
        menuCtrl.setConta(conta);

        Stage stage = (Stage) txtNumeroConta.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("DBank - Menu Principal");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void AbrirCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CadastroCliente.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("DBank - Cadastro de Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro", "Erro ao abrir cadastro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}