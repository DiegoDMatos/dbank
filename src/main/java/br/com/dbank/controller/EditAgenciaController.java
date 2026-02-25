package br.com.dbank.controller;

import br.com.dbank.model.Agencia;
import br.com.dbank.repository.SqlAdminRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import br.com.dbank.repository.SqlAgenciaRepository;
import br.com.dbank.model.Agencia;

public class EditAgenciaController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtEndereco;

    private Agencia agencia;
    private SqlAdminRepository adminRepo = new SqlAdminRepository();
    private SqlAgenciaRepository agenciaRepo = new SqlAgenciaRepository();
    private boolean isEdicao = false;

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
        this.isEdicao = true;
        lblTitulo.setText("Editar Dados da Agência");
        txtNome.setText(agencia.getNomeAgencia());
        txtEndereco.setText(agencia.getEndereco());
    }

    @FXML
    private void handleSalvar() {
        String nome = txtNome.getText();
        String endereco = txtEndereco.getText();

        if (nome.isEmpty() || endereco.isEmpty()) {
            mostrarAlerta("Aviso", "Preencha todos os campos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (isEdicao) {
                agencia.setNomeAgencia(nome);
                agencia.setEndereco(endereco);

                adminRepo.updateAgencia(agencia);
            } else {
                adminRepo.inserirAgencia(nome, endereco);
            }
            fecharJanela();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao processar: " + e.getMessage(), Alert.AlertType.ERROR);
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