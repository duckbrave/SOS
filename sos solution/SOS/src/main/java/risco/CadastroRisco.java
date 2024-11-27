package risco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import conexao.ConexaoSQL;

public class CadastroRisco {

    private Connection conn;

    public CadastroRisco() {
        try {
            this.conn = ConexaoSQL.obterConexao();
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Modificando o método para aceitar parâmetros
    public void cadastrarRisco(String cnpj, String setor, String funcao, String descricaoRisco) {
        try {
            // Consulta para verificar se a empresa existe
            String sqlEmpresa = "SELECT nome FROM empresa WHERE cnpj = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEmpresa)) {
                stmt.setString(1, cnpj);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Recupera o nome da empresa
                        String nomeEmpresa = rs.getString("nome");
                        System.out.println("Empresa encontrada: " + nomeEmpresa);

                        // Consulta para verificar se o setor e a função existem
                        String sqlFuncao = "SELECT 1 FROM setor_funcao WHERE cnpj_empresa = ? AND nome_setor = ? AND funcao = ?";
                        try (PreparedStatement stmtFuncao = conn.prepareStatement(sqlFuncao)) {
                            stmtFuncao.setString(1, cnpj);
                            stmtFuncao.setString(2, setor);
                            stmtFuncao.setString(3, funcao);
                            try (ResultSet rsFuncao = stmtFuncao.executeQuery()) {
                                if (rsFuncao.next()) {
                                    // Se a função e setor existirem, insere o risco
                                    String insertRiscoSQL = "INSERT INTO risco (cnpj_empresa, nome_setor, funcao, risco_desc) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement insertStmt = conn.prepareStatement(insertRiscoSQL)) {
                                        insertStmt.setString(1, cnpj);
                                        insertStmt.setString(2, setor);
                                        insertStmt.setString(3, funcao);
                                        insertStmt.setString(4, descricaoRisco);
                                        insertStmt.executeUpdate();
                                        System.out.println("Risco cadastrado com sucesso.");
                                    }
                                } else {
                                    System.out.println("Erro: O setor e a função informados não foram encontrados.");
                                }
                            }
                        }
                    } else {
                        System.out.println("Erro: Empresa não encontrada com o CNPJ fornecido.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar risco: " + e.getMessage());
        }
    }
}
