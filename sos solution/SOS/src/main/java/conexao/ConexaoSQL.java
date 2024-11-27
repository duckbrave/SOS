package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQL {

    private static final String URL = "jdbc:postgresql://localhost:5432/SOS";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    // Método de conexão reutilizado
    public static Connection obterConexao() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    // Método para fechar a conexão
    public static void desconectar(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
