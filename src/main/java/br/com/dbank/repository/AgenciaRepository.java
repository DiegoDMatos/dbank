package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import java.util.List;

public interface AgenciaRepository {

    void insert(Agencia agencia);
    void update(Agencia agencia);
    void delete(int id);
    Agencia selectById(int id);
    List<Agencia> listarAgencias();

}
