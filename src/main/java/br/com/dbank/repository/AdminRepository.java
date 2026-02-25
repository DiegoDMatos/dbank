package br.com.dbank.repository;

import br.com.dbank.model.Cliente;
import java.util.List;

public interface AdminRepository {
    void inserirAgencia(String nome, String endereco);

    void inserirCliente(Cliente cliente);
    void atualizarCliente(Cliente cliente);
    List<Cliente> listarTodosClientes();
    void cadastrarClienteComConta(Cliente cliente, String senhaInicial);

}