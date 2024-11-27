package setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

import conexao.ConexaoSQL;

public class SetorService {

    private Connection conn;
    VisualizarSetor visualizarSetor = new VisualizarSetor();

    public SetorService() {
        try {
            this.conn = ConexaoSQL.obterConexao();
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Método para verificar se o setor já existe para uma empresa
    public boolean verificarSetorExistente(String cnpj, String nomeSetor) {
        String checkSQL = "SELECT nome_setor FROM setor WHERE cnpj_empresa = ? AND LOWER(nome_setor) = LOWER(?)";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
            checkStmt.setString(1, cnpj);
            checkStmt.setString(2, nomeSetor);
            
            try (ResultSet checkRs = checkStmt.executeQuery()) {
                return checkRs.next();
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar setor: " + e.getMessage());
            return false;
        }
    }

    // Método para cadastrar a função do setor na tabela setor_funcao
    public void cadastrarFuncaoSetor(String cnpj, String nomeSetor, String funcao) {
        try {
            funcao = funcao.toLowerCase();
            String insertFuncaoSQL = "INSERT INTO setor_funcao (cnpj_empresa, nome_setor, funcao) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertFuncaoSQL)) {
                insertStmt.setString(1, cnpj);
                insertStmt.setString(2, nomeSetor);
                insertStmt.setString(3, funcao);
                insertStmt.executeUpdate();
                System.out.println("Função '" + funcao + "' cadastrada com sucesso para o setor '" + nomeSetor + "'!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar função do setor: " + e.getMessage());
        }
    }

    // Método para cadastrar um novo setor para a empresa
    public void cadastrarSetor() {
        Scanner scanner = new Scanner(System.in);
        try {
            // Solicita o CNPJ da empresa
            System.out.print("Digite o CNPJ da empresa: ");
            String cnpj = scanner.nextLine();

            if (!verificarEmpresaExiste(cnpj)) {
                System.out.println("Erro: Empresa não encontrada.");
                return;
            }

            // Exibe os setores cadastrados para a empresa
            List<String> setores = visualizarSetor.visualizar(cnpj);
            if (setores.isEmpty()) {
                System.out.println("Nenhum setor encontrado para essa empresa.");
            } else {
                System.out.println("Setores cadastrados:");
                for (String setor : setores) {
                    System.out.println("- " + setor);
                }

                // Solicita o nome do setor a ser cadastrado
                System.out.print("Digite o nome do setor a ser cadastrado: ");
                String nomeSetor = scanner.nextLine();

                // Verifica se o setor já está cadastrado
                if (verificarSetorExistente(cnpj, nomeSetor)) {
                    System.out.print("Setor encontrado! Agora, insira a função: ");
                    String funcao = scanner.nextLine();
                    cadastrarFuncaoSetor(cnpj, nomeSetor, funcao);
                } else {
                    System.out.println("Erro: O setor '" + nomeSetor + "' não está cadastrado para a empresa com CNPJ " + cnpj);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar setor: " + e.getMessage());
        } finally {
            scanner.close(); // Fechando o scanner
        }
    }

    // Método auxiliar para verificar se a empresa existe
    private boolean verificarEmpresaExiste(String cnpj) {
        String sql = "SELECT nome FROM empresa WHERE cnpj = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna verdadeiro se a empresa for encontrada
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar empresa: " + e.getMessage());
            return false;
        }
    }
}
