package br.com.dbank.controller;

import br.com.dbank.service.ContaService;
import br.com.dbank.model.Conta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtAgencia;
    @FXML private TextField txtConta;

    private ContaService contaService = new ContaService();

    @FXML
    public void handleLogin() {
        try {
            if (txtAgencia.getText().isEmpty() || txtConta.getText().isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.ERROR);
                return;
            }

            int ag = Integer.parseInt(txtAgencia.getText());
            String num = txtConta.getText();

            Conta contaLogada = contaService.login(num, ag);

            if (contaLogada != null) {
                irParaMenuPrincipal(contaLogada);
            } else {
                mostrarAlerta("Erro", "Dados incorretos ou conta inativa.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "A agência deve ser um número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao processar login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    void irParaMenuPrincipal(Conta conta) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuPrincipal.fxml"));
        Parent root = loader.load();

        MenuController menuCtrl = loader.getController();
        menuCtrl.setConta(conta);

        Stage stage = (Stage) txtAgencia.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("DBank - Menu Principal");
        stage.centerOnScreen();
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}