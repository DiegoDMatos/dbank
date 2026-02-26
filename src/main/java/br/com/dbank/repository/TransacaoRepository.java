package br.com.dbank.repository;

import br.com.dbank.model.Transacao;

import java.util.List;

public interface  TransacaoRepository {
    void insert(Transacao transacao);
    List<Transacao> buscarExtratoPorConta(String numeroConta);

}
