package setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import conexao.ConexaoSQL;

public class VisualizarSetor {

    // Método para visualizar os setores cadastrados para uma empresa
    public List<String> visualizar(String cnpj) {
        List<String> setores = new ArrayList<>();  // Lista para armazenar os setores

        try {
            // Consulta para buscar os setores cadastrados para a empresa com base no CNPJ
            String setorSQL = "SELECT nome_setor FROM setor WHERE cnpj_empresa = ?";
            try (Connection conn = ConexaoSQL.obterConexao();
                 PreparedStatement setorStmt = conn.prepareStatement(setorSQL)) {

                setorStmt.setString(1, cnpj);  // Passa o CNPJ como parâmetro para a consulta
                try (ResultSet setorRs = setorStmt.executeQuery()) {
                    if (!setorRs.next()) {
                        System.out.println("Nenhum setor encontrado para essa empresa.");
                    } else {
                        System.out.println("Setores cadastrados para a empresa:");
                        do {
                            // Adiciona os setores encontrados na lista
                            String nomeSetor = setorRs.getString("nome_setor");
                            setores.add(nomeSetor);
                            System.out.println("- " + nomeSetor);  // Exibe cada setor encontrado
                        } while (setorRs.next());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro ao visualizar setores: " + e.getMessage());
        }

        return setores;  // Retorna a lista de setores
    }
}
