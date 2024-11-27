package administrador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import CadastroEmpresa.ListarEmpresas;
import setor.CadastrarSetor;
import setor.VisualizarFuncaoSetor;
import setor.VisualizarSetor;

public class Administrador {

    // Listas para armazenar dados simulados
    private List<String> setores;
    private List<String> funcoesSetor;
    private List<String> riscos;

    public Administrador() {
        setores = new ArrayList<>();
        funcoesSetor = new ArrayList<>();
        riscos = new ArrayList<>();
    }

    // Método para cadastrar setor usando o JFrame
    public void cadastrarSetor() {
        CadastrarSetor cadastrarSetor = new CadastrarSetor();
        cadastrarSetor.cadastrarSetor();
    }

    // Método para cadastrar função de setor
    public void cadastrarFuncaoSetor() {
        String funcao = JOptionPane.showInputDialog("Digite o nome da função do setor:");
        if (funcao == null || funcao.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de nome da função do setor não pode ser vazio.");
        } else {
            funcoesSetor.add(funcao.trim());
            JOptionPane.showMessageDialog(null, "Função de setor cadastrada com sucesso!");
        }
    }

    // Método para listar empresas
    public void listarEmpresas() {
        ListarEmpresas listarEmpresas = new ListarEmpresas();
        String empresasCadastradas = listarEmpresas.obterEmpresasCadastradas();

        if (empresasCadastradas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma empresa cadastrada no banco de dados.");
        } else {
            JOptionPane.showMessageDialog(null, "Empresas cadastradas:\n" + empresasCadastradas);
        }
    }

    // Método para listar setores
    public void listarSetores() {
        String cnpj = JOptionPane.showInputDialog("Digite o CNPJ da empresa para listar os setores:");

        if (cnpj == null || cnpj.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de CNPJ não pode ser vazio.");
        } else {
            VisualizarSetor visualizarSetor = new VisualizarSetor();
            List<String> setoresCadastrados = visualizarSetor.visualizar(cnpj.trim());

            if (setoresCadastrados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum setor cadastrado para essa empresa no banco de dados.");
            } else {
                StringBuilder lista = new StringBuilder("Setores cadastrados para a empresa:\n");
                for (String setor : setoresCadastrados) {
                    lista.append(setor).append("\n");
                }
                JOptionPane.showMessageDialog(null, lista.toString());
            }
        }
    }

    // Método para listar funções de setores
    public void listarFuncoes() {
        String cnpj = JOptionPane.showInputDialog("Digite o CNPJ da empresa para listar as funções de setor:");

        if (cnpj == null || cnpj.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de CNPJ não pode ser vazio.");
        } else {
            VisualizarFuncaoSetor visualizarFuncaoSetor = new VisualizarFuncaoSetor();
            List<String> funcoes = visualizarFuncaoSetor.visualizar(cnpj.trim());

            if (funcoes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhuma função de setor cadastrada para o CNPJ fornecido.");
            } else {
                StringBuilder lista = new StringBuilder("Funções cadastradas para a empresa:\n");
                for (String funcao : funcoes) {
                    lista.append(funcao).append("\n");
                }
                JOptionPane.showMessageDialog(null, lista.toString());
            }
        }
    }

    // Método para cadastrar riscos
    public void cadastrarRisco() {
        String risco = JOptionPane.showInputDialog("Digite o nome do risco:");
        if (risco == null || risco.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de nome do risco não pode ser vazio.");
        } else {
            riscos.add(risco.trim());
            JOptionPane.showMessageDialog(null, "Risco cadastrado com sucesso!");
        }
    }

    // Método para listar riscos
    public void listarRiscos() {
        if (riscos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum risco cadastrado.");
        } else {
            StringBuilder lista = new StringBuilder("Riscos cadastrados:\n");
            for (String risco : riscos) {
                lista.append(risco).append("\n");
            }
            JOptionPane.showMessageDialog(null, lista.toString());
        }
    }
}
