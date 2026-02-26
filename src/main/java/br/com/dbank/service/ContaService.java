package br.com.dbank.service;

import br.com.dbank.exception.ContaNaoEncontradaException;
import br.com.dbank.exception.SaldoInsuficienteException;
import br.com.dbank.model.*;
import br.com.dbank.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContaService {
    private SqlContaRepository contaRepo = new SqlContaRepository();
    private SqlTransacaoRepository transacaoRepo = new SqlTransacaoRepository();

    private Conta buscarContaValidada(String numeroConta, int codigoAgencia) {
        Conta conta = contaRepo.Select(numeroConta);

        if (conta != null && conta.getAgencia().getCodigoAgencia() == codigoAgencia) {
            if ("ATIVA".equalsIgnoreCase(conta.getStatus())) {
                return conta;
            } else {
                System.out.println("Conta " + numeroConta + " está " + conta.getStatus());
            }
        }
        return null;
    }

    public void encerrarConta(String numero, int agencia) {
        Conta conta = buscarContaValidada(numero, agencia);
        if (conta != null) {
            conta.setStatus("ENCERRADA");
            contaRepo.update(conta);
            System.out.println("Conta encerrada com sucesso.");
        }
    }

    public void sacar(Conta conta, BigDecimal valor) {
        if (conta.getSaldo().compareTo(valor) >= 0) {
            conta.setSaldo(conta.getSaldo().subtract(valor));
            contaRepo.update(conta);
            registrarMovimentacao(conta, "SAQUE", valor);
        } else {
            throw new RuntimeException("Saldo insuficiente para realizar o saque.");
        }
    }

    public void depositar(Conta conta, BigDecimal valor) {
        if (conta != null && valor.compareTo(BigDecimal.ZERO) > 0) {
            conta.setSaldo(conta.getSaldo().add(valor));

            contaRepo.update(conta);

            registrarMovimentacao(conta, "DEPOSITO", valor);

            System.out.println("Depósito concluído.");
        } else {
            throw new IllegalArgumentException("Conta inválida ou valor de depósito deve ser maior que zero.");
        }
    }

    private void registrarMovimentacao(Conta conta, String tipo, BigDecimal valor) {
        Transacao t = new Transacao();
        t.setTipoTransacao(tipo);
        t.setValor(valor);
        t.setDataHora(LocalDateTime.now());
        t.setConta(conta);
        transacaoRepo.insert(t);
    }

    public void Transferir(Conta origem, String numDestino, BigDecimal valor) {
        if (origem.getNumeroConta().equals(numDestino)) {
            throw new RuntimeException("Você não pode transferir para a própria conta.");
        }

        Conta destino = contaRepo.Select(numDestino);
        if (destino == null) {
            throw new ContaNaoEncontradaException (numDestino);
        }

        if (origem.getSaldo().compareTo(valor) < 0) {
        throw new SaldoInsuficienteException("Saldo insuficiente.");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepo.update(origem);
        contaRepo.update(destino);

        Transacao transacaoOrigem = new Transacao();
        transacaoOrigem.setTipoTransacao("TRANSFERENCIA ENVIADA");
        transacaoOrigem.setValor(valor);
        transacaoOrigem.setDataHora(java.time.LocalDateTime.now());
        transacaoOrigem.setConta(origem);
        transacaoRepo.insert(transacaoOrigem);

        Transacao transacaoDestino = new Transacao();
        transacaoDestino.setTipoTransacao("TRANSFERENCIA RECEBIDA");
        transacaoDestino.setValor(valor);
        transacaoDestino.setDataHora(java.time.LocalDateTime.now());
        transacaoDestino.setConta(destino);
        transacaoRepo.insert(transacaoDestino);
    }

    public Conta login(String numero, int agencia) {
        Conta conta = buscarContaValidada(numero, agencia);

        if (conta != null) {
            System.out.println("Login autorizado para: " + conta.getCliente().getNome());
            return conta;
        }

        return null;
    }

    public Conta loginComSenha(String numeroConta, String senha) {
        Conta conta = contaRepo.buscarPorContaESenha(numeroConta, senha);

        if (conta != null && conta.getCliente() != null) {
            SqlClienteRepository clienteRepo = new SqlClienteRepository();

            int idDoCliente = conta.getCliente().getIdCliente();
            Cliente clienteCompleto = clienteRepo.selectById(idDoCliente);

            conta.setCliente(clienteCompleto);

            return conta;
        }

        return null;
    }
}