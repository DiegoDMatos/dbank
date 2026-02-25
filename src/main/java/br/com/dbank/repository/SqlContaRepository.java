package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.model.Conta;
import br.com.dbank.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlContaRepository implements ContaRepository {

        @Override
        public void insert(Conta conta) {
            String sql = "INSERT INTO conta (numero_conta, saldo, data_abertura, status, id_cliente, codigo_agencia) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conexao = ConexaoDB.getConexao();
                 PreparedStatement smt = conexao.prepareStatement(sql)) {

                smt.setString(1, conta.getNumeroConta());
                smt.setBigDecimal(2, conta.getSaldo());
                smt.setDate(3, Date.valueOf(conta.getDataAbertura()));
                smt.setString(4, conta.getStatus());
                smt.setInt(5, conta.getCliente().getIdCliente());
                smt.setInt(6, conta.getAgencia().getCodigoAgencia());

                smt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao abrir conta", e);
            }
        }

        @Override
        public void update(Conta conta) {
            String sql = "UPDATE conta SET saldo = ?, status = ?, codigo_agencia = ? WHERE numero_conta = ?";

            try (Connection conexao = ConexaoDB.getConexao();
                 PreparedStatement smt = conexao.prepareStatement(sql)) {

                smt.setBigDecimal(1, conta.getSaldo());
                smt.setString(2, conta.getStatus());
                smt.setInt(3, conta.getAgencia().getCodigoAgencia());
                smt.setString(4, conta.getNumeroConta());

                smt.executeUpdate();
                System.out.println("Dados da conta atualizados.");
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao atualizar conta", e);
            }
        }

    @Override
    public void delete(String numero_conta) {
            String sql = "DELETE from conta WHERE numero_conta = ?";
            try (Connection conexao = ConexaoDB.getConexao();
                 PreparedStatement smt = conexao.prepareStatement(sql)) {

                smt.setString(1, numero_conta);
                smt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao apagar conta", e);
            }
    }

    @Override
    public Conta Select(String numero_conta) {
        String sql = "SELECT c.*, cl.nome FROM conta c " +
                "INNER JOIN cliente cl ON c.id_cliente = cl.id_cliente " +
                "WHERE c.numero_conta = ?";

        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {

            smt.setString(1, numero_conta);
            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    Conta conta = new Conta();
                    Cliente cliente = new Cliente();

                    cliente.setNome(rs.getString("nome"));
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    conta.setCliente(cliente);

                    conta.setNumeroConta(rs.getString("numero_conta"));
                    conta.setSaldo(rs.getBigDecimal("saldo"));
                    conta.setDataAbertura(rs.getDate("data_abertura").toLocalDate());
                    conta.setStatus(rs.getString("status"));

                    Agencia agencia = new Agencia();
                    agencia.setCodigoAgencia(rs.getInt("codigo_agencia"));
                    conta.setAgencia(agencia);

                    return conta;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar conta no MySQL", e);
        }
        return null;
    }
        @Override
        public List<Conta> selectContas() {
            String sql = "SELECT * FROM conta";
            List<Conta> contas = new ArrayList<>();

            try(
                    Connection conexao = ConexaoDB.getConexao();
                    PreparedStatement smt = conexao.prepareStatement(sql);
                    ResultSet select = smt.executeQuery();
                    )
            {
                while(select.next())
                {
                    Conta conta = new Conta();

                    conta.setNumeroConta(select.getString("numero_conta"));
                    conta.setSaldo(select.getBigDecimal("saldo"));
                    conta.setDataAbertura(select.getDate("data_abertura").toLocalDate());
                    conta.setStatus(select.getString("status"));

                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(select.getInt("id_cliente"));
                    conta.setCliente(cliente);

                    Agencia agencia = new Agencia();
                    agencia.setCodigoAgencia(select.getInt("codigo_agencia"));
                    conta.setAgencia(agencia);

                     contas.add(conta);
                }

            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            return contas;
        }

    public Conta validarLogin(String numeroConta, String senha) {
        String sql = "SELECT co.*, cl.nome FROM conta co " +
                "JOIN cliente cl ON co.id_cliente = cl.id_cliente " +
                "WHERE co.numero_conta = ? AND cl.senha = ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement smt = conn.prepareStatement(sql)) {

            smt.setString(1, numeroConta);
            smt.setString(2, senha);
            ResultSet rs = smt.executeQuery();

            if (rs.next()) {
                Conta conta = new Conta();
                conta.setNumeroConta(rs.getString("numero_conta"));
                conta.setSaldo(rs.getBigDecimal("saldo"));

                Cliente c = new Cliente();
                c.setNome(rs.getString("nome"));
                conta.setCliente(c);

                return conta;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
        }
        return null;
    }

    public Conta buscarPorContaESenha(String numeroConta, String senha) {
        String sql = "SELECT co.*, cl.nome FROM conta co " +
                "JOIN cliente cl ON co.id_cliente = cl.id_cliente " +
                "WHERE TRIM(co.numero_conta) = ? AND cl.senha = ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement smt = conn.prepareStatement(sql)) {

            smt.setString(1, numeroConta.trim());
            smt.setString(2, senha);

            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    Conta conta = new Conta();
                    conta.setNumeroConta(rs.getString("numero_conta"));
                    conta.setSaldo(rs.getBigDecimal("saldo"));

                    br.com.dbank.model.Agencia ag = new br.com.dbank.model.Agencia();

                    ag.setCodigoAgencia(rs.getInt("codigo_agencia"));
                    conta.setAgencia(ag);

                    br.com.dbank.model.Cliente c = new br.com.dbank.model.Cliente();
                    c.setNome(rs.getString("nome"));
                    conta.setCliente(c);

                    return conta;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro técnico no login: " + e.getMessage());
        }
        return null;
    }
}
