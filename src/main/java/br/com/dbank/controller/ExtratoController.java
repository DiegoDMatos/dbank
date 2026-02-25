package br.com.dbank.controller;

import br.com.dbank.model.Transacao;
import br.com.dbank.repository.SqlTransacaoRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExtratoController {

    @FXML private TableView<Transacao> tblExtrato;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private TableColumn<Transacao, BigDecimal> colValor;
    @FXML private TableColumn<Transacao, LocalDateTime> colData;

    private SqlTransacaoRepository transacaoRepo = new SqlTransacaoRepository();

    public void carregarDados(String numeroConta) {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoTransacao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        List<Transacao> lista = transacaoRepo.buscarExtratoPorConta(numeroConta);

        tblExtrato.getItems().setAll(lista);
    }

    @FXML
    private void Fechar(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}