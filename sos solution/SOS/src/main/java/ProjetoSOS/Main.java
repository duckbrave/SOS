package ProjetoSOS;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import login.CadastroUsuario;
import login.TelaAutenticacao;
import login.Usuario;

public class Main {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField nomeUsuarioField;
    private JPasswordField senhaField;
    private JTextField emailField;
    private JTextField nomeField;
    private JPasswordField senhaFieldCadastro;
    private JPasswordField confirmarSenhaField;
    private JButton loginButton;
    private JButton cadastrarButton;
    private JButton cadastrarUsuarioButton;
    private JButton voltarButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.getContentPane().add(cardPanel);

        // Tela de Login
        JPanel loginPanel = new JPanel();
        cardPanel.add(loginPanel, "login");
        loginPanel.setLayout(new GridLayout(4, 2));

        JLabel nomeUsuarioLabel = new JLabel("Nome de Usuário:");
        nomeUsuarioField = new JTextField();
        loginPanel.add(nomeUsuarioLabel);
        loginPanel.add(nomeUsuarioField);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaField = new JPasswordField();
        loginPanel.add(senhaLabel);
        loginPanel.add(senhaField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        loginPanel.add(loginButton);

        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "cadastro");
            }
        });
        loginPanel.add(cadastrarButton);

        // Tela de Cadastro
        JPanel cadastroPanel = new JPanel();
        cardPanel.add(cadastroPanel, "cadastro");
        cadastroPanel.setLayout(new GridLayout(6, 2));

        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField();
        cadastroPanel.add(nomeLabel);
        cadastroPanel.add(nomeField);

        JLabel emailLabel = new JLabel("E-mail:");
        emailField = new JTextField();
        cadastroPanel.add(emailLabel);
        cadastroPanel.add(emailField);

        JLabel senhaLabelCadastro = new JLabel("Senha:");
        senhaFieldCadastro = new JPasswordField();
        cadastroPanel.add(senhaLabelCadastro);
        cadastroPanel.add(senhaFieldCadastro);

        JLabel confirmarSenhaLabel = new JLabel("Confirmar Senha:");
        confirmarSenhaField = new JPasswordField();
        cadastroPanel.add(confirmarSenhaLabel);
        cadastroPanel.add(confirmarSenhaField);

        cadastrarUsuarioButton = new JButton("Cadastrar Usuário");
        cadastrarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });
        cadastroPanel.add(cadastrarUsuarioButton);

        voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });
        cadastroPanel.add(voltarButton);
    }

    private void realizarLogin() {
        String nomeUsuario = nomeUsuarioField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nome de usuário ou senha não podem estar vazios.");
            return;
        }

        TelaAutenticacao telaAutenticacao = new TelaAutenticacao();
        Usuario usuarioEncontrado = telaAutenticacao.buscarUsuario(nomeUsuario);

        if (usuarioEncontrado != null) {
            boolean isAdmin = telaAutenticacao.realizarAutenticacao(nomeUsuario, senha);  // Verifica se o usuário é admin ou comum

            // Verificação se é admin ou usuário comum
            if (isAdmin) {
                JOptionPane.showMessageDialog(frame, "Autenticação como administrador bem-sucedida.");
                frame.dispose(); // Fecha a tela de login
                InterfaceAdministrador administrador = new InterfaceAdministrador(); // Interface do administrador
                administrador.setVisible(true); // Mostra a interface do administrador
            } else {
                // Caso o usuário seja comum
                JOptionPane.showMessageDialog(frame, "Autenticação como usuário comum bem-sucedida.");
                frame.dispose(); // Fecha a tela de login
                UsuarioComumGUI usuarioComum = new UsuarioComumGUI(); // Interface do usuário comum
                usuarioComum.setVisible(true); // Mostra a interface do usuário comum
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Nome de usuário não encontrado. Tente novamente.");
        }
    }
    
    private void cadastrarUsuario() {
        String nome = nomeField.getText().trim();   
        String email = emailField.getText().trim();
        String senha = new String(senhaFieldCadastro.getPassword()).trim();
        String confirmarSenha = new String(confirmarSenhaField.getPassword()).trim();

        // Validação do e-mail
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(frame, "E-mail inválido.");
            return;
        }

        // Validação das senhas
        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(frame, "As senhas não coincidem.");
            return;
        }

        // Salvar usuário no banco de dados
        CadastroUsuario cadastroUsuario = new CadastroUsuario();
        boolean usuarioCadastrado = cadastroUsuario.cadastrar();

        if (usuarioCadastrado) {
            JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso.");
            cardLayout.show(cardPanel, "login"); // Voltar para a tela de login após o cadastro
        } else {
            JOptionPane.showMessageDialog(frame, "Erro ao cadastrar usuário.");
        }
    }
}
