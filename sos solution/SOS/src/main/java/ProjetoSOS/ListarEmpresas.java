package ProjetoSOS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ListarEmpresas {
    public static void main(String[] args) {
        // URL de conexão com o banco, substitua com os seus dados
        String url = "jdbc:postgresql://localhost:5432/seubanco"; // Altere para seu banco de dados
        String user = "seuusuario"; // Altere para seu usuário
        String password = "suasenha"; // Altere para sua senha

        // Conectar ao banco e listar empresas
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Criar o Statement para consulta
            Statement stmt = conn.createStatement();
            
            // Consulta SQL para selecionar os dados
            String query = "SELECT cnpj, nome, rua, cidade FROM empresa";
            ResultSet rs = stmt.executeQuery(query);

            // Exibir os resultados
            System.out.println("CNPJ\t\tNome\t\tRua\t\tCidade");
            while (rs.next()) {
                String cnpj = rs.getString("cnpj");
                String nome = rs.getString("nome");
                String rua = rs.getString("rua");
                String cidade = rs.getString("cidade");
                System.out.println(cnpj + "\t" + nome + "\t" + rua + "\t" + cidade);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
