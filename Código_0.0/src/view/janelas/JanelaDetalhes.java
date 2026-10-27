package view.janelas;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JComboBox<String> tiposervicoCombo = new JComboBox<>();
    private JTextField dataField = new JTextField(20);
    private JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Em orçamento",
            "Agendado",
            "Em andamento",
            "Concluído",
            "Cancelado"
        });
    private JTextArea obsArea = new JTextArea(6, 20);

    private JButton editarButton = new JButton("Editar");
    private JButton excluirButton = new JButton("Excluir");
    private JButton fecharButton = new JButton("Fechar");

    private boolean editando = false;

    public JanelaDetalhes(Window dono, Servico servico, List<String> tiposDisponiveis,
                          Consumer<Servico> aoSalvar, Consumer<Servico> aoExcluir) {
        super(dono, "Detalhes do serviço", Dialog.ModalityType.APPLICATION_MODAL);

        this.servico = servico;
        this.aoSalvar = aoSalvar;
        this.aoExcluir = aoExcluir;

        if (tiposDisponiveis != null) {
            for (String tipo : tiposDisponiveis) {
                tiposervicoCombo.addItem(tipo);
            }
        }

        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);

        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(idLabel);
        painelCampos.add(new JLabel("Cliente:"));
        painelCampos.add(clienteField);
        painelCampos.add(new JLabel("Tipo de serviço:"));
        painelCampos.add(tiposervicoCombo);
        painelCampos.add(new JLabel("Data (dd/mm/aaaa):"));
        painelCampos.add(dataField);
        painelCampos.add(new JLabel("Status:"));
        painelCampos.add(statusCombo);

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
        tiposervicoCombo.setSelectedItem(servico.getTipo());
        if (servico.getData() != null) {
            dataField.setText(servico.getData().format(Servico.FORMATO_DATA));
        }
        statusCombo.setSelectedItem(servico.getStatus());
        obsArea.setText(servico.getObservacoes());
    }

    private void liberaEdicao(boolean pode) {
        clienteField.setEditable(false);
        tiposervicoCombo.setEnabled(pode);
        dataField.setEditable(pode);
        statusCombo.setEnabled(pode);
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

        String tipoSelecionado = (String) tiposervicoCombo.getSelectedItem();
        if (tipoSelecionado == null || tipoSelecionado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de serviço.");
            return;
        }

        servico.setTipo(tipoSelecionado);
        servico.setData(novaData);
        servico.setStatus((String) statusCombo.getSelectedItem());
        servico.setObservacoes(obsArea.getText());

        if (aoSalvar != null) {
            aoSalvar.accept(servico);
        }
        liberaEdicao(false);
        JOptionPane.showMessageDialog(this, "Serviço salvo!");
    }

    private void excluir() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o serviço de id " + servico.getId() + "? Esse serviço será excluído para sempre!",
                "Excluir serviço", JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            if (aoExcluir != null) {
                aoExcluir.accept(servico);
            }
            dispose();
        }
    }
}