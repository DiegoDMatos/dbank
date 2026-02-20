package br.com.dbank.repository;

import br.com.dbank.model.Conta;

import java.util.List;

public interface ContaRepository {
    void insert (Conta conta);
    void update (Conta conta);
    void delete (String numero_conta);
    Conta Select (String numero_conta);
    List<Conta> selectContas();
}
