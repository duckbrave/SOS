package projetoSOS;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GerarRelatorioObservacaoRisco {
    private JFrame frame;
    private JTable tableSetores, tableFuncoes, tableRiscos;
    private JTextArea observacaoArea;
    private JButton gerarRelatorioButton;
    private String cnpjSelecionado, nomeSetorSelecionado, riscoDesc;
    private DefaultTableModel modelSetores, modelFuncoes, modelRiscos;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GerarRelatorioObservacaoRisco window = new GerarRelatorioObservacaoRisco();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GerarRelatorioObservacaoRisco() {
        initialize();
    }

    private void initialize() {
        // Configuração da janela principal
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout do painel principal
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Tabela de Setores
        modelSetores = new DefaultTableModel(new Object[][]{}, new String[]{"CNPJ", "Nome do Setor"});
        tableSetores = new JTable(modelSetores);
        JScrollPane scrollSetores = new JScrollPane(tableSetores);
        panel.add(scrollSetores);

        // Tabela de Funções
        modelFuncoes = new DefaultTableModel(new Object[][]{}, new String[]{"Função"});
        tableFuncoes = new JTable(modelFuncoes);
        JScrollPane scrollFuncoes = new JScrollPane(tableFuncoes);
        panel.add(scrollFuncoes);

        // Tabela de Riscos
        modelRiscos = new DefaultTableModel(new Object[][]{}, new String[]{"Descrição do Risco"});
        tableRiscos = new JTable(modelRiscos);
        JScrollPane scrollRiscos = new JScrollPane(tableRiscos);
        panel.add(scrollRiscos);

        // Área de texto para observação
        observacaoArea = new JTextArea(5, 40);
        JScrollPane scrollObservacao = new JScrollPane(observacaoArea);
        panel.add(scrollObservacao);

        // Botão de gerar relatório
        gerarRelatorioButton = new JButton("Gerar Relatório");
        panel.add(gerarRelatorioButton);

        // Ação de seleção de setor
        tableSetores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableSetores.getSelectedRow();
                    if (selectedRow != -1) {
                        cnpjSelecionado = (String) modelSetores.getValueAt(selectedRow, 0);
                        nomeSetorSelecionado = (String) modelSetores.getValueAt(selectedRow, 1);
                        modelFuncoes.setRowCount(0);
                        modelRiscos.setRowCount(0);
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seubanco", "usuario", "senha")) {
                            // Preencher a tabela de funções
                            String queryFuncoes = "SELECT funcao FROM setor_funcao WHERE cnpj_empresa = ? AND nome_setor = ?";
                            try (PreparedStatement stmtFuncoes = conn.prepareStatement(queryFuncoes)) {
                                stmtFuncoes.setString(1, cnpjSelecionado);
                                stmtFuncoes.setString(2, nomeSetorSelecionado);
                                ResultSet rsFuncoes = stmtFuncoes.executeQuery();
                                while (rsFuncoes.next()) {
                                    modelFuncoes.addRow(new Object[]{rsFuncoes.getString("funcao")});
                                }
                            }

                            // Preencher a tabela de riscos
                            String queryRiscos = "SELECT risco_desc FROM risco WHERE cnpj_empresa = ? AND nome_setor = ?";
                            try (PreparedStatement stmtRiscos = conn.prepareStatement(queryRiscos)) {
                                stmtRiscos.setString(1, cnpjSelecionado);
                                stmtRiscos.setString(2, nomeSetorSelecionado);
                                ResultSet rsRiscos = stmtRiscos.executeQuery();
                                while (rsRiscos.next()) {
                                    modelRiscos.addRow(new Object[]{rsRiscos.getString("risco_desc")});
                                }
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Ação do botão "Gerar Relatório"
        gerarRelatorioButton.addActionListener(e -> {
            String observacaoTexto = observacaoArea.getText().trim();
            if (!observacaoTexto.isEmpty() && cnpjSelecionado != null && nomeSetorSelecionado != null) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seubanco", "usuario", "senha")) {
                    // Selecionar o riscoDesc da linha selecionada na tabela de riscos
                    int selectedRiscoRow = tableRiscos.getSelectedRow();
                    if (selectedRiscoRow != -1) {
                        riscoDesc = (String) modelRiscos.getValueAt(selectedRiscoRow, 0); // Captura a descrição do risco

                        // Inserir a observação na tabela observacao_risco
                        String insertObservacao = "INSERT INTO observacao_risco (cnpj_empresa, nome_setor, risco_desc, observacao) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement stmtInsert = conn.prepareStatement(insertObservacao)) {
                            stmtInsert.setString(1, cnpjSelecionado); // CNPJ da empresa
                            stmtInsert.setString(2, nomeSetorSelecionado); // Nome do setor
                            stmtInsert.setString(3, riscoDesc); // Descrição do risco
                            stmtInsert.setString(4, observacaoTexto); // Observação digitada
                            stmtInsert.executeUpdate();
                            JOptionPane.showMessageDialog(frame, "Observação salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecione um risco para salvar a observação!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Erro ao salvar a observação.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Informe uma observação e selecione um risco antes de gerar o relatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
