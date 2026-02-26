package br.com.dbank.repository;

import br.com.dbank.model.Agencia;
import br.com.dbank.model.Cliente;
import br.com.dbank.util.ConexaoDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlClienteRepository implements ClienteRepository {
    public int insert(Cliente cliente, String senha) {
        String sql = "INSERT INTO cliente (nome, cpf, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement smt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            smt.setString(1, cliente.getNome());
            smt.setString(2, cliente.getCpf());
            smt.setString(3, senha);
            smt.executeUpdate();

            try (ResultSet rs = smt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void update(Cliente cliente) {
        String sql = "UPDATE cliente set cpf = ?, nome = ?, endereco = ?, telefone= ?, email = ?, data_nascimento = ?, senha = ? WHERE cliente_id = ?";

        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
        ) {
            smt.setString(1, cliente.getCpf());
            smt.setString(2, cliente.getNome());
            smt.setString(3, cliente.getEndereco());
            smt.setString(4, cliente.getTelefone());
            smt.setString(5, cliente.getEmail());
            smt.setDate(6, Date.valueOf(cliente.getDataNascimento()));
            smt.setString(7, cliente.getSenha());
            smt.setInt(8, cliente.getIdCliente());
            smt.executeUpdate();

            System.out.println("Cliente atualizado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o cliente",e);
        }

    }

    @Override
    public void delete(int id) {
        String sql = "DELETE from cliente WHERE cliente_id = ?";
        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
        )
        {
            smt.setInt(1,id);

            smt.executeUpdate();
            System.out.println("Cliente excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao exlcuir Cliente",e);
        }

    }

    @Override
    public Cliente selectById(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";

        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {

            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setNome(rs.getString("nome"));
                    c.setSenha(rs.getString("senha"));
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Cliente> select() {
        String sql = "SELECT * from cliente";
        List<Cliente> clientes = new ArrayList<>();

        try(
                Connection conexao = ConexaoDB.getConexao();
                PreparedStatement smt = conexao.prepareStatement(sql);
                ResultSet select = smt.executeQuery();
        ){

            while (select.next()){
                Cliente c = new Cliente();

                c.setIdCliente(select.getInt("id_cliente"));
                c.setNome(select.getString("nome"));
                c.setEndereco(select.getString("endereco"));
                c.setTelefone(select.getString("telefone"));
                c.setEmail(select.getString("email"));
                c.setSenha(select.getString("senha"));
                clientes.add(c);

            }
            return clientes;

        } catch (SQLException e) {
            System.out.println("Erro ao acessar dados");
            return null;
        }
    }

}
