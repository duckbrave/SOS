package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.regex.Pattern;
import conexao.ConexaoSQL;

public class CadastroUsuario {

    // Método para validar o e-mail
    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(regex, email);
    }

    // Método para validar a senha e a confirmação de senha
    private boolean validarSenhas(String senha, String confirmacaoSenha) {
        return senha.equals(confirmacaoSenha);
    }

    // Método para validar a palavra-passe de administrador
    private boolean validarPalavraPasse(String palavraPasse) {
        String palavraCorreta = "SOS"; // Defina uma palavra-passe para admin
        return palavraPasse.equals(palavraCorreta);
    }

    // Método para verificar se o usuário já existe
    private boolean usuarioExistente(String nome, String email) {
        String sql = "SELECT 1 FROM usuario WHERE nome = ? OR email = ? LIMIT 1";

        try (Connection conn = ConexaoSQL.obterConexao(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);

            if (stmt.executeQuery().next()) {
                return true; // Já existe um usuário com esse nome ou e-mail
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar se o usuário existe: " + e.getMessage());
        }
        return false; // O usuário não existe
    }

    // Método para cadastrar um usuário
    public void cadastrarUsuario(String nome2, String email2, String senha2) {
        Scanner scanner = new Scanner(System.in);

        // Solicita os dados do usuário
        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o e-mail do usuário: ");
        String email = scanner.nextLine();

        // Verifica se o nome ou e-mail já existem no banco
        if (usuarioExistente(nome, email)) {
            System.out.println("Já existe um usuário com esse nome ou e-mail. Por favor, escolha outro.");
            return; // Encerra o cadastro
        }

        // Valida o e-mail
        if (!validarEmail(email)) {
            System.out.println("E-mail inválido.");
            return;
        }

        System.out.print("Digite a senha do usuário: ");
        String senha = scanner.nextLine();

        System.out.print("Confirme a senha do usuário: ");
        String confirmacaoSenha = scanner.nextLine();

        // Valida as senhas
        if (!validarSenhas(senha, confirmacaoSenha)) {
            System.out.println("As senhas não coincidem.");
            return;
        }

        System.out.print("Digite a palavra-passe para administrador: ");
        String palavraPasse = scanner.nextLine();

        // Define o tipo de usuário com base na palavra-passe (true = admin, false = tecnico)
        boolean isAdmin = validarPalavraPasse(palavraPasse);

        // Cria o objeto de usuário
        Usuario usuario = new Usuario(null, nome, email, senha, isAdmin);

        // Salva o usuário no banco de dados
        salvarUsuarioNoBanco(usuario);
    }

    // Método para salvar um usuário no banco de dados
    private void salvarUsuarioNoBanco(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, is_admin) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoSQL.obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.out.println("Falha ao obter conexão com o banco de dados.");
                return;
            }

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setBoolean(4, usuario.getIsAdmin());  // Modificado para setBoolean

            stmt.executeUpdate();
            System.out.println("Usuário inserido no banco de dados com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao inserir usuário no banco de dados: " + e.getMessage());
        }
    }

	public boolean cadastrar() {
		// TODO Auto-generated method stub
		return false;
	}
}
