package br.com.dbank.controller;

import br.com.dbank.exception.SenhaIncorretaException;
import br.com.dbank.model.Conta;
import br.com.dbank.service.ContaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;

public class MenuController {
    @FXML private Label lblBemVindo;
    @FXML private Label lblSaldo;
    @FXML private Label lblNumeroConta;

    @FXML private Button Deposito;
    @FXML private Button Transferir;
    @FXML private Button AbrirExtrato;
    @FXML private Button Sair;

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
    public void Depositar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setHeaderText("Realizar Depósito");
        dialog.setContentText("Digite o valor do depósito:");

        dialog.showAndWait().ifPresent(valorStr -> {
            try {
                BigDecimal valor = new BigDecimal(valorStr);
                if(validarSenhaComConfirmacao())
                contaService.depositar(contaAtiva, valor);
                atualizarInterface();
                mostrarAlerta("Sucesso", "Depósito de R$ " + valor + " realizado!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Por favor, digite um valor numérico válido.", Alert.AlertType.ERROR);
            } catch (SenhaIncorretaException e) {
                e.getMessage();
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

                if (validarSenhaComConfirmacao()) {
                    contaService.sacar(contaAtiva, valor);
                    atualizarInterface();
                    mostrarAlerta("Sucesso", "Saque de R$ " + valor + " realizado!", Alert.AlertType.INFORMATION);
                } else {
                    throw new SenhaIncorretaException();
                }

            } catch (Exception e) {
                mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void Transferir() {
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

                    if (validarSenhaComConfirmacao()) {
                        contaService.Transferir(contaAtiva, numDestino, valor);
                        atualizarInterface();
                        mostrarAlerta("Sucesso", "Transferência enviada para a conta " + numDestino, Alert.AlertType.INFORMATION);
                    } else {
                        throw new SenhaIncorretaException();
                    }

                } catch (Exception e) {
                    mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        });
    }
    private boolean validarSenhaComConfirmacao() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Segurança DBank");
        dialog.setHeaderText("Autorização do Cliente");

        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Digite sua senha de cliente");
        dialog.getDialogPane().setContent(pwdField);

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmar) return pwdField.getText();
            return null;
        });

        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String digitada = result.get().trim();
            String salvaNoBanco = contaAtiva.getCliente().getSenha();

            if (salvaNoBanco == null) {
                System.out.println("ERRO: A senha não foi carregada do banco de dados!");
                return false;
            }

            return digitada.equals(salvaNoBanco.trim());
        }
        return false;
    }

    @FXML
    public void AbrirExtrato() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Extrato.fxml"));
            Parent root = loader.load();
            ExtratoController extratoCtrl = loader.getController();
            extratoCtrl.carregarDados(this.contaAtiva.getNumeroConta());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("DBank - Extrato Detalhado");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar o extrato: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean confirmarComSenha() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmação de Segurança");
        dialog.setHeaderText("Operação Restrita: Confirme sua identidade");

        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Sua senha de cliente");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(pwdField);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnConfirmar) return pwdField.getText();
            return null;
        });

        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String senhaDigitada = result.get();
            return senhaDigitada.equals(contaAtiva.getCliente().getSenha());
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}