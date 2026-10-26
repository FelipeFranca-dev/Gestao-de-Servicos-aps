package view.janelas;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entities.Servico;

public class JanelaDetalhes extends JDialog {

    private Servico servico;

    private Consumer<Servico> aoSalvar;
    private Consumer<Servico> aoExcluir;

    private JLabel idLabel = new JLabel();
    private JTextField clienteField = new JTextField(20);
    private JTextField tipoField = new JTextField(20);
    private JTextField dataField = new JTextField(20);
    private JTextField statusField = new JTextField(20);
    private JTextArea obsArea = new JTextArea(6, 20);

    private JButton editarButton = new JButton("Editar");
    private JButton excluirButton = new JButton("Excluir");
    private JButton fecharButton = new JButton("Fechar");

    private boolean editando = false;

    public JanelaDetalhes(Window dono, Servico servico,
                          Consumer<Servico> aoSalvar, Consumer<Servico> aoExcluir) {
        super(dono, "Detalhes do serviço", Dialog.ModalityType.APPLICATION_MODAL);

        this.servico = servico;
        this.aoSalvar = aoSalvar;
        this.aoExcluir = aoExcluir;

        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);

        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(idLabel);
        painelCampos.add(new JLabel("Cliente:"));
        painelCampos.add(clienteField);
        painelCampos.add(new JLabel("Tipo de serviço:"));
        painelCampos.add(tipoField);
        painelCampos.add(new JLabel("Data (dd/mm/aaaa):"));
        painelCampos.add(dataField);
        painelCampos.add(new JLabel("Status:"));
        painelCampos.add(statusField);

        JScrollPane painelObs = new JScrollPane(obsArea);
        painelObs.setBorder(BorderFactory.createTitledBorder("Observações"));

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(editarButton);
        painelBotoes.add(excluirButton);
        painelBotoes.add(fecharButton);

        setLayout(new BorderLayout());
        add(painelCampos, BorderLayout.PAGE_START);
        add(painelObs, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.PAGE_END);

        editarButton.addActionListener(e -> cliqueEditar());
        excluirButton.addActionListener(e -> excluir());
        fecharButton.addActionListener(e -> dispose());

        preencheCampos();
        liberaEdicao(false);

        pack();
        setLocationRelativeTo(dono);
    }
    private void preencheCampos() {
        idLabel.setText(String.valueOf(servico.getId()));
        clienteField.setText(servico.getCliente());
        tipoField.setText(servico.getTipo());
        if (servico.getData() != null) {
            dataField.setText(servico.getData().format(Servico.FORMATO_DATA));
        }
        statusField.setText(servico.getStatus());
        obsArea.setText(servico.getObservacoes());
    }
    private void liberaEdicao(boolean pode) {
        clienteField.setEditable(false);
        tipoField.setEditable(pode);
        dataField.setEditable(pode);
        statusField.setEditable(pode);
        obsArea.setEditable(pode);

        editarButton.setText(pode ? "Salvar" : "Editar");
        editando = pode;
    }
    private void cliqueEditar() {
        if (!editando) {
            liberaEdicao(true);
        } else {
            salvar();
        }
    }
    private void salvar() {
        LocalDate novaData;
        try {
            novaData = LocalDate.parse(dataField.getText().trim(), Servico.FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/mm/aaaa.");
            return;
        }
        if (tipoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O tipo de serviço não pode ficar vazio.");
            return;
        }
        servico.setTipo(tipoField.getText().trim());
        servico.setData(novaData);
        servico.setStatus(statusField.getText().trim());
        servico.setObservacoes(obsArea.getText());

        if (aoSalvar != null) {
            aoSalvar.accept(servico);
        }
        liberaEdicao(false);
        JOptionPane.showMessageDialog(this, "Serviço salvo!");
    }
    private void excluir() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o serviço de id " + servico.getId() + "?",
                "Excluir serviço", JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            if (aoExcluir != null) {
                aoExcluir.accept(servico);
            }
            dispose();
        }
    }
}