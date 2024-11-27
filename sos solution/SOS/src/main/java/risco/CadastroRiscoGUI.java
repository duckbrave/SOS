package risco;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CadastroRiscoGUI extends JFrame {

    private JTextField txtCNPJ;
    private JTextField txtSetor;
    private JTextField txtFuncao;
    private JTextArea txtDescricaoRisco;
    private JButton btnCadastrar;

    public CadastroRiscoGUI() {
        // Configurações da janela
        setTitle("Cadastro de Risco");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas a janela atual
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Painel de entrada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Adicionando componentes
        panel.add(new JLabel("CNPJ da Empresa:"));
        txtCNPJ = new JTextField();
        panel.add(txtCNPJ);

        panel.add(new JLabel("Nome do Setor:"));
        txtSetor = new JTextField();
        panel.add(txtSetor);

        panel.add(new JLabel("Função no Setor:"));
        txtFuncao = new JTextField();
        panel.add(txtFuncao);

        panel.add(new JLabel("Descrição do Risco:"));
        txtDescricaoRisco = new JTextArea();
        txtDescricaoRisco.setLineWrap(true);
        txtDescricaoRisco.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtDescricaoRisco);
        panel.add(scrollPane);

        btnCadastrar = new JButton("Cadastrar Risco");
        panel.add(btnCadastrar);

        add(panel, BorderLayout.CENTER);

        // Ação do botão de cadastrar risco
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarRisco();
            }
        });
    }

    // Método para realizar o cadastro de risco
    private void cadastrarRisco() {
        String cnpj = txtCNPJ.getText();
        String setor = txtSetor.getText();
        String funcao = txtFuncao.getText();
        String descricaoRisco = txtDescricaoRisco.getText();

        // Validação simples
        if (cnpj.isEmpty() || setor.isEmpty() || funcao.isEmpty() || descricaoRisco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Aqui, você deve chamar a classe que faz o cadastro no banco de dados ou realizar qualquer outra lógica necessária
            // Substitua "CadastroRisco" com a lógica real de cadastro
            CadastroRisco cadastro = new CadastroRisco(); // Supondo que você tenha essa classe
            cadastro.cadastrarRisco(cnpj, setor, funcao, descricaoRisco);

            JOptionPane.showMessageDialog(this, "Risco cadastrado com sucesso.", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar risco: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpar os campos
    private void limparCampos() {
        txtCNPJ.setText("");
        txtSetor.setText("");
        txtFuncao.setText("");
        txtDescricaoRisco.setText("");
    }
}
