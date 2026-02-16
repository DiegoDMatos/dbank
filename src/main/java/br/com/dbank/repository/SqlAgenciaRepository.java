package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import br.com.dbank.util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlAgenciaRepository implements AgenciaRepository{


    @Override
    public void insert(Agencia agencia) {

    }

    @Override
    public void update(Agencia agencia) {
        String sql = "UPDATE agencia set nome_agencia = ?, endereco = ?, telefone = ? WHERE codigo_agencia = ?";

        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
        ){
            smt.setString(1, agencia.getNomeAgencia());
            smt.setString(2, agencia.getEndereco());
            smt.setString(3, agencia.getTelefone());
            smt.setInt(4, agencia.getCodigoAgencia());

            smt.executeUpdate();

            System.out.println("Agência atualizada com sucesso!");

        }catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar agência", e);
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Agencia selectById(int id) {
        return null;
    }

    @Override
    public List<Agencia> listarAgencias() {
        return List.of();
    }
}
