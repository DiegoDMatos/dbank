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
    public void insert(Agencia agencia) {
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
    public Agencia selectById(int id) {
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
        String sql = "SELECT * from agencia";
        List<Agencia> agencias = new ArrayList<>();

        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
                ResultSet select = smt.executeQuery();
                ){

            while (select.next()){
                Agencia agencia = new Agencia();

                agencia.setCodigoAgencia(select.getInt("codigo_agencia"));
                agencia.setNomeAgencia(select.getString("nome_agencia"));
                agencia.setEndereco(select.getString("endereco"));
                agencia.setTelefone(select.getString("telefone"));
                agencias.add(agencia);
            }
            return agencias;

        } catch (SQLException e) {
            System.out.println("Erro ao acessar dados");
            return null;
        }
    }
}
