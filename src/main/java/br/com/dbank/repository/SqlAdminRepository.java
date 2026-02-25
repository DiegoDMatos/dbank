package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SqlAdminRepository implements AdminRepository {

    @Override
    public void inserirAgencia(String nome, String endereco) {
        String sql = "INSERT INTO agencia (nome, endereco) VALUES (?, ?)";
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {
            smt.setString(1, nome);
            smt.setString(2, endereco);
            smt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir agência: " + e.getMessage(), e);
        }
    }

    @Override
    public void cadastrarClienteComConta(Cliente cliente, String senhaInicial) {
        String sqlCliente = "INSERT INTO cliente (cpf, nome, telefone) VALUES (?, ?, ?)";
        String sqlConta = "INSERT INTO conta (numero_conta, saldo, senha, cpf_cliente) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoDB.getConexao()) {
            conexao.setAutoCommit(false);

            try (PreparedStatement smtCli = conexao.prepareStatement(sqlCliente);
                 PreparedStatement smtCon = conexao.prepareStatement(sqlConta)) {

                smtCli.setString(1, cliente.getCpf());
                smtCli.setString(2, cliente.getNome());
                smtCli.setString(3, cliente.getTelefone());
                smtCli.executeUpdate();

                String numeroGerado = String.format("%03d", new Random().nextInt(999));

                smtCon.setString(1, numeroGerado);
                smtCon.setDouble(2, 0.0);
                smtCon.setString(3, senhaInicial);
                smtCon.setString(4, cliente.getCpf());
                smtCon.executeUpdate();

                conexao.commit();
                System.out.println("Cliente e conta " + numeroGerado + " criados com sucesso!");

            } catch (SQLException e) {
                conexao.rollback(); // Desfaz tudo se der erro
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de cadastro: " + e.getMessage());
        }
    }

    @Override
    public void inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, telefone) VALUES (?, ?, ?)";
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {
            smt.setString(1, cliente.getCpf());
            smt.setString(2, cliente.getNome());
            smt.setString(3, cliente.getTelefone());
            smt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ? WHERE cpf = ?";
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {
            smt.setString(1, cliente.getNome());
            smt.setString(2, cliente.getTelefone());
            smt.setString(3, cliente.getCpf());
            smt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> listarTodosClientes() {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        List<Cliente> lista = new ArrayList<>();
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql);
             ResultSet rs = smt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setCpf(rs.getString("cpf"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public void updateAgencia(Agencia agencia) {
        String sql = "UPDATE agencia SET nome_agencia = ?, endereco = ?, telefone = ? WHERE codigo_agencia = ?";

        try (Connection conn = br.com.dbank.util.ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, agencia.getNomeAgencia());
            stmt.setString(2, agencia.getEndereco());
            stmt.setString(3, agencia.getTelefone());
            stmt.setInt(4, agencia.getCodigoAgencia());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Agência atualizada com sucesso no banco de dados.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar agência no repositório: " + e.getMessage());
        }
    }

    public List<Agencia> listarTodasAgencias() {
        String sql = "SELECT * FROM agencia";
        List<Agencia> lista = new ArrayList<>();

        try (Connection conn = br.com.dbank.util.ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agencia agencia = new Agencia();
                agencia.setCodigoAgencia(rs.getInt("codigo_agencia"));
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