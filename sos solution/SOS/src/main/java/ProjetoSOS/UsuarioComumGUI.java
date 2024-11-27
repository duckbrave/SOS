package ProjetoSOS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class UsuarioComumGUI extends JFrame {

    public UsuarioComumGUI() {
        setTitle("Usuário Comum");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Tela do Usuário Comum");
        add(label);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UsuarioComumGUI window = new UsuarioComumGUI();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
