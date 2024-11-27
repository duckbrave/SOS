package CadastroEmpresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import conexao.ConexaoSQL;

public class ListarEmpresas {

    public String obterEmpresasCadastradas() {
        StringBuilder listaEmpresas = new StringBuilder();
        String sql = "SELECT * FROM empresa";

        try (Connection conn = ConexaoSQL.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listaEmpresas.append("Código: ").append(rs.getInt("codigo")).append("\n")
                             .append("Nome: ").append(rs.getString("nome")).append("\n")
                             .append("Razão Social: ").append(rs.getString("razao_social")).append("\n")
                             .append("CNPJ: ").append(rs.getString("cnpj")).append("\n")
                             .append("email: ").append(rs.getString("email")).append("\n")
                             .append("Rua: ").append(rs.getString("rua")).append("\n")
                             .append("Cidade: ").append(rs.getString("cidade")).append("\n")
                             .append("CEP: ").append(rs.getString("cep")).append("\n")
                             .append("Número: ").append(rs.getString("numero")).append("\n")
                             .append("telefone: ").append(rs.getString("telefone")).append("\n")
                             .append("----------------------------\n");
            }

        } catch (Exception e) {
            listaEmpresas.append("Erro ao listar empresas do banco de dados: ").append(e.getMessage());
        }

        return listaEmpresas.toString();
    }
}
