package br.com.dbank.service;

import br.com.dbank.model.Cliente;
import br.com.dbank.repository.SqlClienteRepository;
import java.util.List;

public class ClienteService {
    private SqlClienteRepository clienteRepo = new SqlClienteRepository();

    public boolean cadastrarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            System.out.println("Erro: O nome do cliente é obrigatório.");
            return false;
        }

        if (cliente.getCpf() == null || cliente.getCpf().length() < 11) {
            System.out.println("Erro: CPF inválido.");
            return false;
        }

        try {
            clienteRepo.insert(cliente);
            System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
            return true;
        } catch (RuntimeException e) {
            // Captura erros como CPF duplicado
            System.out.println("Erro ao salvar no banco: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listarTodos() {
        return clienteRepo.select();
    }
}