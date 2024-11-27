package setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import conexao.ConexaoSQL;

public class VisualizarFuncaoSetor {

    private Connection conn;

    public VisualizarFuncaoSetor() {
        try {
            this.conn = ConexaoSQL.obterConexao();
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Método para visualizar as funções de um setor
    public List<String> visualizar(String cnpj) {
        List<String> funcoesSetor = new ArrayList<>();  // Inicializa a lista para evitar NullPointerException

        try {
            // Verifica se o CNPJ foi fornecido corretamente
            if (cnpj == null || cnpj.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "CNPJ não pode ser vazio.");
                return funcoesSetor; // Retorna lista vazia, nunca null
            }

            // Consulta para buscar o nome da empresa com base no CNPJ
            String sql = "SELECT nome FROM empresa WHERE cnpj = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cnpj);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Exibe o nome da empresa
                        String nomeEmpresa = rs.getString("nome");

                        // Solicita o nome do setor
                        String nomeSetor = JOptionPane.showInputDialog("Digite o nome do setor para visualizar as funções:");

                        if (nomeSetor == null || nomeSetor.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nome do setor não pode ser vazio.");
                            return funcoesSetor;  // Retorna lista vazia, nunca null
                        }

                        // Consulta para buscar as funções do setor
                        String funcaoSQL = "SELECT funcao FROM setor_funcao WHERE cnpj_empresa = ? AND nome_setor = ?";
                        try (PreparedStatement funcaoStmt = conn.prepareStatement(funcaoSQL)) {
                            funcaoStmt.setString(1, cnpj);
                            funcaoStmt.setString(2, nomeSetor);
                            try (ResultSet funcaoRs = funcaoStmt.executeQuery()) {
                                while (funcaoRs.next()) {
                                    String funcao = funcaoRs.getString("funcao");
                                    funcoesSetor.add(funcao); // Adiciona a função à lista
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Empresa não encontrada com o CNPJ fornecido.");
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao visualizar funções do setor: " + e.getMessage());
        }
        return funcoesSetor;  // Retorna a lista de funções, que pode estar vazia
    }
}
