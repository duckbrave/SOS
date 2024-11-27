package CadastroEmpresa;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONObject;

import conexao.ConexaoSQL;
import empresa.Empresa;

public class CadastroEmpresa extends JFrame {
    private JTextField txtCnpj;
    private JTextArea txtAreaResultado;
    private JButton btnBuscar;
    private JButton btnCadastrar;
    private JButton btnCancelar;
    private JButton btnVoltar;  // Novo botão para voltar

    public CadastroEmpresa() {
        setTitle("Cadastro de Empresa");
        setLayout(new BorderLayout());

        // Painel de CNPJ
        JPanel panelCnpj = new JPanel();
        panelCnpj.setLayout(new GridLayout(3, 2));  // Alterado para incluir 3 linhas (adicionando o botão de voltar)

        panelCnpj.add(new JLabel("CNPJ:"));
        txtCnpj = new JTextField();
        panelCnpj.add(txtCnpj);

        btnBuscar = new JButton("Buscar");
        panelCnpj.add(btnBuscar);

        btnCancelar = new JButton("Cancelar");
        panelCnpj.add(btnCancelar);

        // Painel de Resultado
        txtAreaResultado = new JTextArea(10, 40);
        txtAreaResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultado);

        // Painel de Cadastro
        JPanel panelCadastro = new JPanel();
        panelCadastro.setLayout(new BorderLayout()); // Usando BorderLayout para posicionar o botão "Cadastrar" à esquerda

        // Novo painel para o botão Cadastrar, posicionado à esquerda
        JPanel panelCadastrar = new JPanel();
        panelCadastrar.setLayout(new BorderLayout()); // Usando BorderLayout para o botão "Cadastrar" ficar à esquerda
        btnCadastrar = new JButton("Cadastrar");
        panelCadastrar.add(btnCadastrar, BorderLayout.WEST);  // Alinha o botão à esquerda

        // Novo painel para o botão Voltar, posicionado à direita
        JPanel panelVoltar = new JPanel();
        panelVoltar.setLayout(new BorderLayout()); // Usando BorderLayout para o botão "Voltar" ficar à direita
        btnVoltar = new JButton("Voltar");
        panelVoltar.add(btnVoltar, BorderLayout.EAST);  // Alinha o botão à direita

        // Adiciona os componentes à tela
        add(panelCnpj, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelCadastro, BorderLayout.SOUTH);
        panelCadastro.add(panelCadastrar, BorderLayout.WEST);  // Adiciona o painel "Cadastrar" à esquerda
        panelCadastro.add(panelVoltar, BorderLayout.EAST);  // Adiciona o painel "Voltar" à direita

        // Ações
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEmpresa();
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarEmpresa();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        // Ação para o botão Voltar
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarTelaAnterior();
            }
        });

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas a janela, não a aplicação
        setLocationRelativeTo(null);
    }

    // Método para consultar a API da Receita Federal
    public Empresa consultarCnpjApiReceita(String cnpj) {
        // Remove todos os caracteres que não são números do CNPJ
        String cnpjFormatado = cnpj.replaceAll("[^\\d]", "");

        String apiUrl = "https://www.receitaws.com.br/v1/cnpj/" + cnpjFormatado;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Verifique o código de resposta da conexão
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON da resposta
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("status") && jsonResponse.getString("status").equals("ERROR")) {
                    return null; // CNPJ inválido
                }

                // Criação do objeto Empresa a partir do JSON
                Empresa empresa = new Empresa();
                empresa.setNome(jsonResponse.getString("nome"));
                empresa.setCnpj(jsonResponse.getString("cnpj"));
                empresa.setTelefone(jsonResponse.optString("telefone", "Não informado"));
                empresa.setEmail(jsonResponse.optString("email", "Não informado"));

                // Adicionando endereço (rua, cidade, cep)
                empresa.setRua(jsonResponse.optString("logradouro", "Não informado"));
                empresa.setCidade(jsonResponse.optString("municipio", "Não informado"));
                empresa.setCep(jsonResponse.optString("cep", "Não informado"));

                return empresa;
            } else {
                System.out.println("Erro na requisição: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Exibe os dados da empresa na área de resultados
    private void exibirDadosEmpresa(Empresa empresa) {
        txtAreaResultado.setText("Nome: " + empresa.getNome() + "\n");
        txtAreaResultado.append("CNPJ: " + empresa.getCnpj() + "\n");
        txtAreaResultado.append("Telefone: " + empresa.getTelefone() + "\n");
        txtAreaResultado.append("Email: " + empresa.getEmail() + "\n");
        txtAreaResultado.append("Rua: " + empresa.getRua() + "\n");
        txtAreaResultado.append("Cidade: " + empresa.getCidade() + "\n");
        txtAreaResultado.append("CEP: " + empresa.getCep() + "\n");
    }

    // Limpa os campos
    private void limparCampos() {
        txtCnpj.setText("");
        txtAreaResultado.setText("");
    }

    // Cadastra a empresa no banco de dados
    private void cadastrarEmpresa() {
        String cnpj = txtCnpj.getText().trim().replaceAll("\\D", "");
        if (cnpj.isEmpty() || cnpj.length() != 14) {
            JOptionPane.showMessageDialog(this, "CNPJ inválido. Faça uma nova busca.");
            return;
        }

        // Consulta à API
        Empresa empresa = consultarCnpjApiReceita(cnpj);
        if (empresa == null) {
            JOptionPane.showMessageDialog(this, "CNPJ não encontrado ou inválido.");
            return;
        }

        // Verifica se a empresa já está cadastrada
        try (Connection conn = ConexaoSQL.obterConexao()) {
            String queryVerificaCnpj = "SELECT COUNT(*) FROM empresa WHERE cnpj = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryVerificaCnpj)) {
                stmt.setString(1, cnpj);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Empresa já cadastrada.");
                    return;
                }
            }

            // Insere os dados no banco com rua, cidade e cep
            String insertQuery = "INSERT INTO empresa (nome, cnpj, telefone, email, rua, cidade, cep, razao_social) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, empresa.getNome());
                stmt.setString(2, empresa.getCnpj());
                stmt.setString(3, empresa.getTelefone());
                stmt.setString(4, empresa.getEmail());
                stmt.setString(5, empresa.getRua());
                stmt.setString(6, empresa.getCidade());
                stmt.setString(7, empresa.getCep());
                stmt.setString(8, empresa.getNome()); // Ou outro valor para "razao_social"
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Empresa cadastrada com sucesso.");
            } catch (SQLException e) {
                // Verifica se o erro é causado por duplicidade de CNPJ
                if (e.getMessage().contains("empresa_cnpj_key")) {
                    JOptionPane.showMessageDialog(this, "Este CNPJ já está cadastrado.");
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar empresa no banco.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.");
        }
    }

    // Método para voltar à tela anterior
    private void voltarTelaAnterior() {
        // Fechar a tela atual ou voltar para a tela anterior
        this.dispose();
    }

    private void buscarEmpresa() {
        String cnpj = txtCnpj.getText().trim();
        if (cnpj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um CNPJ.");
            return;
        }

        // Consulta à API
        Empresa empresa = consultarCnpjApiReceita(cnpj);
        if (empresa == null) {
            JOptionPane.showMessageDialog(this, "CNPJ não encontrado.");
        } else {
            exibirDadosEmpresa(empresa);
        }
    }

    public static void main(String[] args) {
        new CadastroEmpresa().setVisible(true);
    }
}
