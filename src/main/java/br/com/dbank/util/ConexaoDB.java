package br.com.dbank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String usuario = "root";
    private static final String senha = "Dieghost*1";
    private static final String URL = "jdbc:mysql://localhost:3306/dbank";

    public static Connection getConexao(){
        try {
            return DriverManager.getConnection(URL, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco dbank: " + e.getMessage());
        }
    }
}
