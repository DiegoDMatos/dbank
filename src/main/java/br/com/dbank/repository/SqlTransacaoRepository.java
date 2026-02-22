package br.com.dbank.repository;

import br.com.dbank.model.Transacao;
import br.com.dbank.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlTransacaoRepository implements TransacaoRepository {
    @Override
    public void insert(Transacao transacao) {
        String sql = "INSERT INTO transacao (tipo, valor, data_hora, numero_conta) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement smt = conexao.prepareStatement(sql)) {

            smt.setString(1, transacao.getTipoTransacao());

            smt.setBigDecimal(2, transacao.getValor());

            smt.setTimestamp(3, Timestamp.valueOf(transacao.getDataHora()));

            smt.setString(4, transacao.getConta().getNumeroConta());

            smt.executeUpdate();
            System.out.println("Transação de " + transacao.getTipoTransacao() + " registrada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar transação no banco dbank", e);
        }
    }

    @Override
    public List<Transacao> buscarExtratoPorConta(String numeroConta) {
            String sql = "SELECT * FROM transacao WHERE numero_conta = ? ORDER BY data_hora DESC";
            List<Transacao> extrato = new ArrayList<>();

            try (Connection conexao = ConexaoDB.getConexao();
                 PreparedStatement smt = conexao.prepareStatement(sql)) {

                smt.setString(1, numeroConta);

                try (ResultSet select = smt.executeQuery()) {
                    while (select.next()) {
                        Transacao t = new Transacao();
                        t.setIdTransacao(select.getInt("id_transacao"));
                        t.setTipoTransacao(select.getString("tipo"));
                        t.setValor(select.getBigDecimal("valor"));

                        t.setDataHora(select.getTimestamp("data_hora").toLocalDateTime());

                        extrato.add(t);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao gerar extrato da conta: " + numeroConta, e);
            }
            return extrato;
    }
}
