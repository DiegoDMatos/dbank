package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import br.com.dbank.util.ConexaoDB;
import com.mysql.cj.log.Log;
import com.mysql.cj.xdevapi.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlAgenciaRepository implements AgenciaRepository{


    @Override
    public void insertAgencia(Agencia agencia) {
        String sql = "INSERT INTO agencia (nome_agencia, endereco, telefone) VALUES (?, ?, ?)";
        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
                ){
            smt.setString(1, agencia.getNomeAgencia());
            smt.setString(2, agencia.getEndereco());
            smt.setString(3, agencia.getTelefone());

            smt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível inserir a agência",e);
        }

    }

    @Override
    public void updateAgencia(Agencia agencia) {
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
    public void deleteAgencia(int id) {
        String sql = "DELETE from agencia WHERE codigo_agencia = ?";
        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
                )
        {
            smt.setInt(1,id);

            smt.executeUpdate();
            System.out.println("Agência excluída com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao apagar agência",e);
        }

    }

    @Override
    public Agencia selectByIdAgencia(int id) {
        String sql = "SELECT * FROM agencia WHERE codigo_agencia = ?";
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {

            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    Agencia a = new Agencia();
                    a.setCodigoAgencia(rs.getInt("codigo_agencia"));
                    a.setNomeAgencia(rs.getString("nome_agencia"));
                    return a;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar agência por ID", e);
        }
        System.out.println("Agencia não Encontrada");
        return null;
    }

    @Override
    public List<Agencia> listarAgencias() {
        String sql = "SELECT * FROM agencia";
        List<Agencia> lista = new ArrayList<>();

        try (Connection conn = br.com.dbank.util.ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agencia agencia = new Agencia();
                agencia.setCodigoAgencia(rs.getInt("id_agencia"));
                agencia.setNomeAgencia(rs.getString("nome_agencia"));
                agencia.setEndereco(rs.getString("endereco"));
                lista.add(agencia);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar agências: " + e.getMessage());
        }
        return lista;
    }

}
