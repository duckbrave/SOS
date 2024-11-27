package setor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import conexao.ConexaoSQL;

public class CadastrarSetor extends JFrame {

    private JTextField cnpjField;
    private JTextField setorField;
    private JButton buscarEmpresaButton;
    private JButton cadastrarSetorButton;
    private JButton voltarButton;
    private JTextArea empresaInfoArea;

    public CadastrarSetor() {
        setTitle("Cadastro de Setor");
        setSize(400, 350);  // Ajuste de tamanho para acomodar o botão "Voltar"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // Campo de CNPJ
        JPanel cnpjPanel = new JPanel();
        cnpjPanel.add(new JLabel("CNPJ da empresa:"));
        cnpjField = new JTextField(20);
        cnpjPanel.add(cnpjField);
        buscarEmpresaButton = new JButton("Buscar Empresa");
        cnpjPanel.add(buscarEmpresaButton);
        add(cnpjPanel);

        // Área para mostrar informações da empresa
        empresaInfoArea = new JTextArea(3, 30);
        empresaInfoArea.setEditable(false);
        add(new JScrollPane(empresaInfoArea));

        // Campo de nome do setor
        JPanel setorPanel = new JPanel();
        setorPanel.add(new JLabel("Nome do setor:"));
        setorField = new JTextField(20);
        setorPanel.add(setorField);
        add(setorPanel);

        // Botão para cadastrar o setor
        cadastrarSetorButton = new JButton("Cadastrar Setor");
        cadastrarSetorButton.setEnabled(false);  // Só ativa após buscar a empresa
        add(cadastrarSetorButton);

        // Botão para voltar
        voltarButton = new JButton("Voltar");
        add(voltarButton);

        // Configuração dos botões
        buscarEmpresaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEmpresa();
            }
        });

        cadastrarSetorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarSetor();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Fecha a janela atual
            }
        });

        setVisible(true);
    }

    private void buscarEmpresa() {
        String cnpj = cnpjField.getText().trim();
        if (cnpj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o CNPJ.");
            return;
        }

        String sql = "SELECT nome, rua FROM empresa WHERE cnpj = ?";
        try (Connection conn = ConexaoSQL.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Conexão com o banco de dados falhou.");
                return;
            }

            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Exibe as informações da empresa
                    String nomeEmpresa = rs.getString("nome");
                    String ruaEmpresa = rs.getString("rua");
                    empresaInfoArea.setText("Empresa encontrada:\nNome: " + nomeEmpresa + "\nRua: " + ruaEmpresa);
                    cadastrarSetorButton.setEnabled(true);
                } else {
                    empresaInfoArea.setText("Empresa não encontrada com o CNPJ fornecido.");
                    cadastrarSetorButton.setEnabled(false);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    public void cadastrarSetor() {
        String cnpj = cnpjField.getText().trim();
        String nomeSetor = setorField.getText().trim().toLowerCase();

        if (nomeSetor.isEmpty()) {
            return;
        }

        String checkSQL = "SELECT nome_setor FROM setor WHERE cnpj_empresa = ? AND LOWER(nome_setor) = LOWER(?)";
        try (Connection conn = ConexaoSQL.obterConexao();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Conexão com o banco de dados falhou.");
                return;
            }

            conn.setAutoCommit(false);  // Inicia a transação
            checkStmt.setString(1, cnpj);
            checkStmt.setString(2, nomeSetor);

            try (ResultSet checkRs = checkStmt.executeQuery()) {
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(this, "Setor '" + nomeSetor + "' já cadastrado para esta empresa.");
                } else {
                    // Insere o setor na tabela 'setor'
                    String insertSQL = "INSERT INTO setor (nome_setor, cnpj_empresa) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setString(1, nomeSetor);
                        insertStmt.setString(2, cnpj);

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            conn.commit();
                            JOptionPane.showMessageDialog(this, "Setor '" + nomeSetor + "' cadastrado com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Falha ao cadastrar o setor. Nenhuma linha foi afetada.");
                        }
                    } catch (Exception e) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Erro ao inserir setor no banco de dados: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CadastrarSetor();
    }
}
