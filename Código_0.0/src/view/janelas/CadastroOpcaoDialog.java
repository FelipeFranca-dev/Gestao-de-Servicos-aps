
package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CadastroOpcaoDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField campoTexto = new JTextField(25);

    private String valorCadastrado;

    public CadastroOpcaoDialog(
            Window janelaPai,
            String titulo,
            String rotulo) {

        super(janelaPai, titulo, ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel(new BorderLayout(8, 8));

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        painelPrincipal.add(
                new JLabel(rotulo),
                BorderLayout.NORTH
        );

        painelPrincipal.add(
                campoTexto,
                BorderLayout.CENTER
        );

        JButton botaoCancelar = new JButton("Cancelar");
        JButton botaoCadastrar = new JButton("Cadastrar");

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        painelBotoes.add(botaoCancelar);
        painelBotoes.add(botaoCadastrar);

        painelPrincipal.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        setContentPane(painelPrincipal);

        botaoCancelar.addActionListener(e -> dispose());

        botaoCadastrar.addActionListener(e -> {
            String valor = campoTexto.getText().trim();

            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Preencha o campo antes de cadastrar.",
                        "Campo obrigatório",
                        JOptionPane.WARNING_MESSAGE
                );

                campoTexto.requestFocusInWindow();
                return;
            }

            valorCadastrado = valor;
            dispose();
        });

        getRootPane().setDefaultButton(botaoCadastrar);

        pack();
        setLocationRelativeTo(janelaPai);
    }

    public String getValorCadastrado() {
        return valorCadastrado;
    }
}
