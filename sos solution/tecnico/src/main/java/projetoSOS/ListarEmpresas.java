package projetoSOS;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ListarEmpresas {

    public static void main(String[] args) {
        // Definir as configurações diretamente no código
        String url = "jdbc:postgresql://localhost:5432/SOS"; // Altere para o seu banco de dados
        String user = "postgres"; // Altere para o seu usuário
        String password = "1234"; // Altere para a sua senha

        // Criar a interface gráfica com JFrame
        JFrame frame = new JFrame("Lista de Empresas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Painel para input de CNPJ
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel cnpjLabel = new JLabel("Informe o CNPJ:");
        JTextField cnpjField = new JTextField(15);
        JButton buscarButton = new JButton("Buscar Setores");

        // Adiciona os componentes ao painel
        panel.add(cnpjLabel);
        panel.add(cnpjField);
        panel.add(buscarButton);
        frame.add(panel, BorderLayout.NORTH);

        // Criar a tabela para mostrar as empresas
        String[] columnsEmpresas = {"CNPJ", "Nome", "Rua", "Cidade", "CEP"};
        DefaultTableModel modelEmpresas = new DefaultTableModel(columnsEmpresas, 0);
        JTable tableEmpresas = new JTable(modelEmpresas);
        JScrollPane scrollPaneEmpresas = new JScrollPane(tableEmpresas);
        frame.add(scrollPaneEmpresas, BorderLayout.CENTER);

        // Criar a tabela para mostrar os setores
        String[] columnsSetores = {"Nome Setor"};
        DefaultTableModel modelSetores = new DefaultTableModel(columnsSetores, 0);
        JTable tableSetores = new JTable(modelSetores);
        JScrollPane scrollPaneSetores = new JScrollPane(tableSetores);
        frame.add(scrollPaneSetores, BorderLayout.SOUTH);

        // Criar a tabela para mostrar as funções
        String[] columnsFuncoes = {"Função"};
        DefaultTableModel modelFuncoes = new DefaultTableModel(columnsFuncoes, 0);
        JTable tableFuncoes = new JTable(modelFuncoes);
        JScrollPane scrollPaneFuncoes = new JScrollPane(tableFuncoes);
        frame.add(scrollPaneFuncoes, BorderLayout.EAST); // Adiciona à direita

        // Adicionar campo para observação
        JLabel observacaoLabel = new JLabel("Observação:");
        JTextArea observacaoTextArea = new JTextArea(5, 40); // Aumentando o campo para 5 linhas e 40 colunas
        observacaoTextArea.setLineWrap(true);  // Quebra de linha automática
        observacaoTextArea.setWrapStyleWord(true);  // Quebra de linha entre as palavras
        JScrollPane scrollPaneObservacao = new JScrollPane(observacaoTextArea);
        JPanel observacaoPanel = new JPanel();
        observacaoPanel.setLayout(new BorderLayout());
        observacaoPanel.add(observacaoLabel, BorderLayout.NORTH);
        observacaoPanel.add(scrollPaneObservacao, BorderLayout.CENTER);
        frame.add(observacaoPanel, BorderLayout.WEST); // Adiciona à esquerda

        // Botão para salvar a observação
        JButton salvarObservacaoButton = new JButton("Salvar Observação");
        observacaoPanel.add(salvarObservacaoButton, BorderLayout.SOUTH);

        // Ação do botão "Buscar Setores"
        buscarButton.addActionListener(e -> {
            // Limpar os modelos de dados antes de buscar
            modelEmpresas.setRowCount(0);
            modelSetores.setRowCount(0);
            modelFuncoes.setRowCount(0);

            String cnpj = cnpjField.getText().trim();
            if (!cnpj.isEmpty()) {
                // Conectar ao banco de dados e listar empresas
                try (Connection conn = DriverManager.getConnection(url, user, password);
                     PreparedStatement pstmtEmpresas = conn.prepareStatement(
                             "SELECT cnpj, nome, rua, cidade, cep FROM empresa WHERE cnpj = ?")) {
                    pstmtEmpresas.setString(1, cnpj);
                    try (ResultSet rsEmpresas = pstmtEmpresas.executeQuery()) {
                        // Adicionar os dados das empresas na tabela
                        while (rsEmpresas.next()) {
                            String cnpjResult = rsEmpresas.getString("cnpj");
                            String nome = rsEmpresas.getString("nome");
                            String rua = rsEmpresas.getString("rua");
                            String cidade = rsEmpresas.getString("cidade");
                            String cep = rsEmpresas.getString("cep");
                            modelEmpresas.addRow(new Object[]{cnpjResult, nome, rua, cidade, cep});
                        }
                    }

                    // Consulta SQL para selecionar setores
                    try (PreparedStatement pstmtSetores = conn.prepareStatement(
                            "SELECT nome_setor FROM setor WHERE cnpj_empresa = ?")) {
                        pstmtSetores.setString(1, cnpj);
                        try (ResultSet rsSetores = pstmtSetores.executeQuery()) {
                            while (rsSetores.next()) {
                                String nomeSetor = rsSetores.getString("nome_setor");
                                modelSetores.addRow(new Object[]{nomeSetor});
                            }
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Informe o CNPJ para buscar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        tableSetores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Verifique se algum índice válido foi selecionado
                int selectedRow = tableSetores.getSelectedRow();
                if (selectedRow != -1) {
                    // Agora podemos acessar os dados da primeira linha selecionada
                    String setor = (String) tableSetores.getValueAt(selectedRow, 0);

                    // Limpar a tabela de funções antes de adicionar novas
                    modelFuncoes.setRowCount(0);

                    // Consultar as funções para o setor selecionado
                    try (Connection conn = DriverManager.getConnection(url, user, password);
                         PreparedStatement pstmt = conn.prepareStatement(
                                 "SELECT funcao FROM setor_funcao WHERE nome_setor = ?")) {
                        pstmt.setString(1, setor);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            while (rs.next()) {
                                String funcao = rs.getString("funcao");
                                modelFuncoes.addRow(new Object[]{funcao});
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Nenhuma linha foi selecionada
                    JOptionPane.showMessageDialog(frame, "Selecione um setor para continuar.");
                }
            }
        });

        // Ação do botão "Salvar Observação"
        salvarObservacaoButton.addActionListener(e -> {
            System.out.println("Botão 'Salvar Observação' foi clicado.");
            String observacao = observacaoTextArea.getText().trim();
            if (!observacao.isEmpty()) {
                // Verificar se há uma linha selecionada nas tabelas
                int selectedRowEmpresas = tableEmpresas.getSelectedRow();
                int selectedRowSetores = tableSetores.getSelectedRow();
                int selectedRowFuncoes = tableFuncoes.getSelectedRow();

                if (selectedRowEmpresas != -1 && selectedRowSetores != -1 && selectedRowFuncoes != -1) {
                    // Obter os dados das linhas selecionadas
                    String cnpj = (String) modelEmpresas.getValueAt(selectedRowEmpresas, 0);
                    String setor = (String) modelSetores.getValueAt(selectedRowSetores, 0);
                    String funcao = (String) modelFuncoes.getValueAt(selectedRowFuncoes, 0);
                    String nomeEmpresa = (String) modelEmpresas.getValueAt(selectedRowEmpresas, 1);
                    String rua = (String) modelEmpresas.getValueAt(selectedRowEmpresas, 2);
                    String cidade = (String) modelEmpresas.getValueAt(selectedRowEmpresas, 3);
                    String cep = (String) modelEmpresas.getValueAt(selectedRowEmpresas, 4);

                    // Salvar a observação no arquivo de texto
                    String filePath = "C:\\Users\\Ivan\\Desktop\\observacoes.txt"; 
                    try (FileWriter writer = new FileWriter(filePath, true)) {
                        writer.write("CNPJ: " + cnpj + "\n");
                        writer.write("Nome da Empresa: " + nomeEmpresa + "\n");
                        writer.write("Setor: " + setor + "\n");
                        writer.write("Função: " + funcao + "\n");
                        writer.write("Endereço: " + rua + ", " + cidade + ", " + cep + "\n");
                        writer.write("Observação: " + observacao + "\n\n");

                        JOptionPane.showMessageDialog(frame, "Observação salva com sucesso!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, selecione uma empresa, setor e função.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Digite uma observação.");
            }
        });


        // Exibir a interface gráfica
        frame.setVisible(true);
    }
}
