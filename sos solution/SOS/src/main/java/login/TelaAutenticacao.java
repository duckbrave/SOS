package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.ConexaoSQL;

public class TelaAutenticacao {

    // Método para buscar usuário por nome no banco de dados
    public Usuario buscarUsuario(String nomeUsuario) {
        Usuario usuario = null;
        String query = "SELECT nome, senha, is_admin FROM usuario WHERE nome = ?";
        
        try (Connection conn = ConexaoSQL.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, nomeUsuario);  // Define o nome do usuário na consulta
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                boolean isAdmin = rs.getBoolean("is_admin");
                usuario = new Usuario(nome, senha, isAdmin);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return usuario;
    }

    // Método para realizar autenticação
    public boolean realizarAutenticacao(String nomeUsuario, String senha) {
        Usuario usuario = buscarUsuario(nomeUsuario);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario.isAdmin();
        }
        return false;
    }
}
