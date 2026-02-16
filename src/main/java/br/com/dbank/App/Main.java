package br.com.dbank.App;

import br.com.dbank.util.ConexaoDB;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            Connection con = ConexaoDB.getConexao();
            if (con != null) {
                System.out.println("✅ Conexão estabelecida com sucesso ao banco dbank!");
                con.close(); // Sempre feche a conexão após o teste
            }
        } catch (SQLException e) {
            System.err.println("❌ Falha ao conectar!");
            e.printStackTrace();
        }
    }

}
