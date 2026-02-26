package br.com.dbank.App;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.model.Conta;
import br.com.dbank.repository.SqlContaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TesteConta {
    public static void main(String[] args) {
        SqlContaRepository repo = new SqlContaRepository();

        Conta conta = new Conta();
        conta.setNumeroConta("001");
        conta.setSaldo(new BigDecimal("500.00"));
        conta.setDataAbertura(LocalDate.now());
        conta.setStatus("ATIVA");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setIdCliente(1);
        conta.setCliente(clienteExistente);

        Agencia agenciaExistente = new Agencia();
        agenciaExistente.setCodigoAgencia(1);
        conta.setAgencia(agenciaExistente);

        repo.insert(conta);
        System.out.println("Conta 001" +
                " criada com sucesso!");
    }


}