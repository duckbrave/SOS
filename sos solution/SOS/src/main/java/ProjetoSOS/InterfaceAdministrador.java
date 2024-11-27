package ProjetoSOS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import CadastroEmpresa.CadastroEmpresa;
import administrador.Administrador;
import risco.CadastroRiscoGUI;
import setor.SetorCadastroGUI;

public class InterfaceAdministrador extends JFrame {

    private JButton btnCadastrarEmpresa;
    private JButton btnCadastrarSetor;
    private JButton btnCadastrarFuncaoSetor;
    private JButton btnListarEmpresas;
    private JButton btnListarSetores;
    private JButton btnListarFuncoes;
    private JButton btnCadastrarRisco;
    private JButton btnSair;
    private Administrador administrador;
    private CadastroEmpresa cadastroEmpresa;

    public InterfaceAdministrador() {
        // Configurações da janela
        setTitle("Menu Administrador");
        setSize(2000, 1500); // Tamanho ajustado da janela
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializando objetos
        administrador = new Administrador();
        cadastroEmpresa = new CadastroEmpresa();

        // Painel de botões no topo
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        // Botões com texto em duas linhas
        btnCadastrarEmpresa = createCustomButton("<html>Cadastrar<br>Empresa</html>");
        btnCadastrarSetor = createCustomButton("<html>Cadastrar<br>Setor</html>");
        btnCadastrarFuncaoSetor = createCustomButton("<html>Cadastrar<br>Função de Setor</html>");
        btnListarEmpresas = createCustomButton("<html>Listar<br>Empresas</html>");
        btnListarSetores = createCustomButton("<html>Listar<br>Setores</html>");
        btnListarFuncoes = createCustomButton("<html>Listar<br>Funções</html>");
        btnCadastrarRisco = createCustomButton("<html>Cadastrar<br>Risco</html>");
        btnSair = createCustomButton("<html>Sair</html>");

        // Adicionando os botões ao painel
        buttonPanel.add(btnCadastrarEmpresa);
        buttonPanel.add(btnCadastrarSetor);
        buttonPanel.add(btnCadastrarFuncaoSetor);
        buttonPanel.add(btnListarEmpresas);
        buttonPanel.add(btnListarSetores);
        buttonPanel.add(btnListarFuncoes);
        buttonPanel.add(btnCadastrarRisco);
        buttonPanel.add(btnSair);

        // Painel principal com a imagem centralizada
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Verificação do caminho da imagem
        String imagePath = "C:/Users/Ivan/Documents/sos solution/SOS/src/main/java/logo.jpeg";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(1024, 720, Image.SCALE_SMOOTH); // Redimensionamento
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(imageLabel, BorderLayout.CENTER);
        } else {
            JOptionPane.showMessageDialog(this, "Logo não encontrada no caminho especificado.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Adiciona o painel principal ao frame
        add(mainPanel);

        // Ações dos botões
        btnCadastrarEmpresa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para cadastro de empresa
                // Abre a janela de cadastro de empresa
                CadastroEmpresa cadastroEmpresaGUI = new CadastroEmpresa();
                cadastroEmpresaGUI.setVisible(true);
            }
        });

        btnCadastrarSetor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abre a janela de cadastro de setor
                SetorCadastroGUI setorCadastroGUI = new SetorCadastroGUI();
                setorCadastroGUI.setVisible(true);
            }
        });

        btnCadastrarFuncaoSetor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abre a janela de cadastro de função de setor
                SetorCadastroGUI setorCadastroGUI = new SetorCadastroGUI();
                setorCadastroGUI.setVisible(true);
            }
        });

        // Listagem de empresas, setores e funções
        btnListarEmpresas.addActionListener(e -> administrador.listarEmpresas());
        btnListarSetores.addActionListener(e -> administrador.listarSetores());
        btnListarFuncoes.addActionListener(e -> administrador.listarFuncoes());

        // Cadastro de risco
        btnCadastrarRisco.addActionListener(e -> {
            // Abre a janela de cadastro de risco
            CadastroRiscoGUI cadastroRiscoGUI = new CadastroRiscoGUI();
            cadastroRiscoGUI.setVisible(true);
        });

        // Ação para o botão sair
        btnSair.addActionListener(e -> dispose());
    }

    // Método para criar botões personalizados
    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 80)); // Tamanho maior dos botões
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public static void main(String[] args) {
        // Executa a interface do administrador na thread de eventos Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfaceAdministrador().setVisible(true);
            }
        });
    }
}
