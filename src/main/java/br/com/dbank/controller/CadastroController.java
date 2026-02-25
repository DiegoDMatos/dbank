package br.com.dbank.controller;

import br.com.dbank.service.ClienteService;
import br.com.dbank.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class CadastroController{
    @FXML private TextField txtNome;
    @FXML private TextField txtCPF;
    @FXML private PasswordField txtSenha;

    private ClienteService clienteService = new ClienteService();

    @FXML
    public void Salvar() {
        try {
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(txtNome.getText());
            novoCliente.setCpf(txtCPF.getText());

            String numeroDaConta = clienteService.cadastrarCliente(novoCliente, txtSenha.getText());

            mostrarAlerta("Sucesso",
                    "Cadastro realizado!\nAnote seu número para Login: " + numeroDaConta,
                    Alert.AlertType.INFORMATION);

            Voltar();
        } catch (Exception e) {
            mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void Voltar() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}