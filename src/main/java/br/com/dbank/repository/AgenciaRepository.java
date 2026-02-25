package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import java.util.List;

public interface AgenciaRepository {

    void insertAgencia(Agencia agencia);
    void updateAgencia(Agencia agencia);
    void deleteAgencia(int id);
    Agencia selectByIdAgencia(int id);
    List<Agencia> listarAgencias();

}
