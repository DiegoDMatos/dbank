package br.com.dbank.controller;

import br.com.dbank.model.Cliente;
import br.com.dbank.repository.SqlAdminRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditClienteController {

    @FXML private TextField txtCpf;
    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;

    private Cliente cliente;
    private SqlAdminRepository adminRepo = new SqlAdminRepository();

    // Método para receber os dados da tela principal
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        txtCpf.setText(cliente.getCpf());
        txtNome.setText(cliente.getNome());
        txtTelefone.setText(cliente.getTelefone());
    }

    @FXML
    private void handleSalvar() {
        if (txtNome.getText().isEmpty() || txtTelefone.getText().isEmpty()) {
            mostrarAlerta("Erro", "Campos obrigatórios vazios.", Alert.AlertType.ERROR);
            return;
        }

        // Atualiza o objeto com os novos dados
        cliente.setNome(txtNome.getText());
        cliente.setTelefone(txtTelefone.getText());

        try {
            adminRepo.atualizarCliente(cliente); // Executa o UPDATE no banco
            fecharJanela();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao atualizar banco: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}