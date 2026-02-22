package br.com.dbank.controller;

import br.com.dbank.model.Conta;
import br.com.dbank.service.ContaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.math.BigDecimal;

public class MenuController {

    @FXML private Label lblBemVindo;
    @FXML private Label lblSaldo;
    @FXML private Label lblNumeroConta;

    private Conta contaAtiva;
    private ContaService contaService = new ContaService();

    public void setConta(Conta conta) {
        this.contaAtiva = conta;
        atualizarInterface();
    }

    private void atualizarInterface() {
        if (contaAtiva != null) {
            lblBemVindo.setText("Olá, " + contaAtiva.getCliente().getNome());
            lblNumeroConta.setText("Conta: " + contaAtiva.getNumeroConta());
            lblSaldo.setText(String.format("Saldo: R$ %.2f", contaAtiva.getSaldo()));
        }
    }

    @FXML
    public void Sair() {
        System.exit(0);
    }
    @FXML
    public void Deposito() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setHeaderText("Realizar Depósito");
        dialog.setContentText("Digite o valor do depósito:");

        dialog.showAndWait().ifPresent(valorStr -> {
            try {
                 BigDecimal valor = new BigDecimal(valorStr);

                contaService.depositar(contaAtiva, valor);

                atualizarInterface();
                mostrarAlerta("Sucesso", "Depósito de R$ " + valor + " realizado!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Por favor, digite um valor numérico válido.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao realizar depósito: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void Saque() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Saque");
        dialog.setHeaderText("Realizar Saque");
        dialog.setContentText("Digite o valor do saque:");

        dialog.showAndWait().ifPresent(valorStr -> {
            try {
                BigDecimal valor = new BigDecimal(valorStr);

                contaService.sacar(contaAtiva, valor);

                atualizarInterface();
                mostrarAlerta("Sucesso", "Saque de R$ " + valor + " realizado!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void handleTransferir() {
        TextInputDialog dialogConta = new TextInputDialog();
        dialogConta.setTitle("Transferência");
        dialogConta.setHeaderText("Dados do Destinatário");
        dialogConta.setContentText("Número da conta de destino:");

        dialogConta.showAndWait().ifPresent(numDestino -> {
            TextInputDialog dialogValor = new TextInputDialog();
            dialogValor.setTitle("Transferência");
            dialogValor.setHeaderText("Valor da Transferência");
            dialogValor.setContentText("Digite quanto deseja enviar:");

            dialogValor.showAndWait().ifPresent(valorStr -> {
                try {
                    BigDecimal valor = new BigDecimal(valorStr);

                    contaService.transferir(contaAtiva, numDestino, valor);

                    atualizarInterface();
                    mostrarAlerta("Sucesso", "Transferência enviada para a conta " + numDestino, Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        });
    }

    // Futuramente adicionaremos aqui os métodos para Saque e Depósito
}