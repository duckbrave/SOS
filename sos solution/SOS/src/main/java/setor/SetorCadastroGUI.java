package setor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetorCadastroGUI extends JFrame {

    private JTextField cnpjField;
    private JTextField nomeSetorField;
    private JTextField funcaoField;
    private JButton cadastrarButton;
    private SetorService setorService;

    public SetorCadastroGUI() {
        // Inicializa o SetorService
        setorService = new SetorService();

        // Configurações da interface
        setTitle("Cadastro de Setor");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Alterado para DISPOSE_ON_CLOSE
        setLayout(new GridLayout(5, 2));

        // Criação dos componentes
        JLabel cnpjLabel = new JLabel("CNPJ da Empresa:");
        cnpjField = new JTextField();

        JLabel nomeSetorLabel = new JLabel("Nome do Setor:");
        nomeSetorField = new JTextField();

        JLabel funcaoLabel = new JLabel("Função do Setor:");
        funcaoField = new JTextField();

        cadastrarButton = new JButton("Cadastrar Setor");

        // Adiciona os componentes à janela
        add(cnpjLabel);
        add(cnpjField);
        add(nomeSetorLabel);
        add(nomeSetorField);
        add(funcaoLabel);
        add(funcaoField);
        add(new JLabel()); // Espaço vazio
        add(cadastrarButton);

        // Define a ação do botão de cadastro
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método de cadastro de setor
                cadastrarSetor();
            }
        });
    }

    private void cadastrarSetor() {
        String cnpj = cnpjField.getText();
        String nomeSetor = nomeSetorField.getText();
        String funcao = funcaoField.getText();

        // Validação de campos
        if (cnpj.isEmpty() || nomeSetor.isEmpty() || funcao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Chama o método da classe SetorService
        if (setorService.verificarSetorExistente(cnpj, nomeSetor)) {
            setorService.cadastrarFuncaoSetor(cnpj, nomeSetor, funcao);
            JOptionPane.showMessageDialog(this, "Função cadastrada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Setor não encontrado para a empresa com CNPJ fornecido.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
