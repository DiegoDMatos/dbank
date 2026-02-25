package br.com.dbank.service;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.model.Conta;
import br.com.dbank.repository.SqlClienteRepository;
import br.com.dbank.repository.SqlContaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ClienteService {
    private SqlClienteRepository clienteRepo = new SqlClienteRepository();
    private SqlContaRepository contaRepo = new SqlContaRepository();

    public String cadastrarCliente(Cliente cliente, String senha) {
        if (cliente == null || cliente.getNome().isEmpty() || cliente.getCpf().isEmpty() || senha.isEmpty()) {
            throw new RuntimeException("Nome, CPF e Senha são obrigatórios!");
        }

        try {
            int idGerado = clienteRepo.insert(cliente, senha);
            cliente.setIdCliente(idGerado);

            String numeroGerado = gerarNumeroContaAleatorio();

            Conta novaConta = new Conta();
            novaConta.setNumeroConta(numeroGerado);
            novaConta.setSaldo(java.math.BigDecimal.ZERO);
            novaConta.setDataAbertura(java.time.LocalDate.now());
            novaConta.setStatus("ATIVA");
            novaConta.setCliente(cliente);

            br.com.dbank.model.Agencia ag = new br.com.dbank.model.Agencia();
            ag.setCodigoAgencia(1);
            novaConta.setAgencia(ag);

            contaRepo.insert(novaConta);

            return numeroGerado;
        } catch (Exception e) {
            throw new RuntimeException("Falha no cadastro: " + e.getMessage());
        }
    }

    private String gerarNumeroContaAleatorio() {
        int numero = (int) (Math.random() * 900) + 100;
        return String.valueOf(numero);
    }

}