package br.com.dbank.controller;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.repository.SqlAdminRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML private TableColumn<Cliente, String> colNome, colCpf, colTelefone;
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TextField txtNomeAgencia, txtEnderecoAgencia;
    @FXML private TextField txtNome, txtCpf, txtTelefone;
    @FXML private TableView<Agencia> tblAgencias;
    @FXML private TableColumn<Agencia, Integer> colIdAgencia;
    @FXML private TableColumn<Agencia, String> colNomeAgencia, colEnderecoAgencia;


    private SqlAdminRepository adminRepo = new SqlAdminRepository();


    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        colIdAgencia.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeAgencia.setCellValueFactory(new PropertyValueFactory<>("nomeAgencia"));
        colEnderecoAgencia.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        atualizarTabela();
        atualizarTabelaAgencias();
    }

    private void atualizarTabela() {
        tblClientes.getItems().setAll(adminRepo.listarTodosClientes());
    }

    @FXML
    public void handleSalvarAgencia() {
        String nome = txtNomeAgencia.getText();
        String endereco = txtEnderecoAgencia.getText();

        if (nome.isEmpty() || endereco.isEmpty()) {
            mostrarAlerta("Aviso", "Preencha todos os campos da agência.", Alert.AlertType.WARNING);
            return;
        }

        try {
            adminRepo.inserirAgencia(nome, endereco);
            mostrarAlerta("Sucesso", "Agência cadastrada com sucesso!", Alert.AlertType.INFORMATION);
            txtNomeAgencia.clear();
            txtEnderecoAgencia.clear();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao salvar agência: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void handleNovoCliente() {
        try {
            Cliente novo = new Cliente();
            novo.setNome(txtNome.getText());
            novo.setCpf(txtCpf.getText());
            novo.setTelefone(txtTelefone.getText());

            adminRepo.cadastrarClienteComConta(novo, "1234");

            mostrarAlerta("Sucesso", "Cliente cadastrado! Conta gerada automaticamente.", Alert.AlertType.INFORMATION);

            txtNome.clear();
            txtCpf.clear();
            txtTelefone.clear();
            atualizarTabela();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao cadastrar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleEditarCliente() {
        Cliente selecionado = tblClientes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditCliente.fxml"));
                Parent root = loader.load();

                EditClienteController controller = loader.getController();
                controller.setCliente(selecionado);

                Stage stage = new Stage();
                stage.setTitle("Editar Cliente - " + selecionado.getNome());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                atualizarTabela();
            } catch (IOException e) {
                mostrarAlerta("Erro", "Erro ao abrir tela de edição", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Selecione um cliente para editar!", Alert.AlertType.WARNING);
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void atualizarTabelaAgencias() {

        tblAgencias.getItems().setAll(adminRepo.listarTodasAgencias());
    }

    @FXML
    public void handleNovaAgencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditAgencia.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            atualizarTabelaAgencias();
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível abrir a tela de nova agência.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleEditarAgencia() {
        Agencia selecionada = tblAgencias.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditAgencia.fxml"));
                Parent root = loader.load();

                EditAgenciaController controller = loader.getController();
                controller.setAgencia(selecionada);

                Stage stage = new Stage();
                stage.setTitle("Editar Agência - " + selecionada.getNomeAgencia());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                atualizarTabelaAgencias();

            } catch (IOException e) {
                mostrarAlerta("Erro", "Erro ao abrir a tela de edição de agência: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Por favor, selecione uma agência na tabela para editar.", Alert.AlertType.WARNING);
        }
    }
}