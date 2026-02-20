package br.com.dbank.repository;

import br.com.dbank.model.Cliente;

import java.util.List;

public interface ClienteRepository {

    void insert(Cliente cliente);
    void update(Cliente cliente);
    void delete(int id);
    Cliente selectById(int id);
    List<Cliente> select();
}
