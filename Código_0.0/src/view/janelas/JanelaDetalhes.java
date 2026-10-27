package view.janelas;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 7938082 (atualização geral interface/DAO)
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
<<<<<<< HEAD
=======
import javax.swing.JComboBox;
>>>>>>> 7938082 (atualização geral interface/DAO)
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
<<<<<<< HEAD

=======
>>>>>>> 7938082 (atualização geral interface/DAO)
    private Consumer<Servico> aoSalvar;
    private Consumer<Servico> aoExcluir;

    private JLabel idLabel = new JLabel();
    private JTextField clienteField = new JTextField(20);
<<<<<<< HEAD
    private JTextField tipoField = new JTextField(20);
    private JTextField dataField = new JTextField(20);
    private JTextField statusField = new JTextField(20);
=======
    private JComboBox<String> tiposervicoCombo = new JComboBox<>();
    private JTextField dataField = new JTextField(20);
    private JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Em orçamento",
            "Agendado",
            "Em andamento",
            "Concluído",
            "Cancelado"
        });
>>>>>>> 7938082 (atualização geral interface/DAO)
    private JTextArea obsArea = new JTextArea(6, 20);

    private JButton editarButton = new JButton("Editar");
    private JButton excluirButton = new JButton("Excluir");
    private JButton fecharButton = new JButton("Fechar");

    private boolean editando = false;

<<<<<<< HEAD
    public JanelaDetalhes(Window dono, Servico servico,
=======
    public JanelaDetalhes(Window dono, Servico servico, List<String> tiposDisponiveis,
>>>>>>> 7938082 (atualização geral interface/DAO)
                          Consumer<Servico> aoSalvar, Consumer<Servico> aoExcluir) {
        super(dono, "Detalhes do serviço", Dialog.ModalityType.APPLICATION_MODAL);

        this.servico = servico;
        this.aoSalvar = aoSalvar;
        this.aoExcluir = aoExcluir;

<<<<<<< HEAD
=======
        if (tiposDisponiveis != null) {
            for (String tipo : tiposDisponiveis) {
                tiposervicoCombo.addItem(tipo);
            }
        }

>>>>>>> 7938082 (atualização geral interface/DAO)
        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);

        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(idLabel);
        painelCampos.add(new JLabel("Cliente:"));
        painelCampos.add(clienteField);
        painelCampos.add(new JLabel("Tipo de serviço:"));
<<<<<<< HEAD
        painelCampos.add(tipoField);
        painelCampos.add(new JLabel("Data (dd/mm/aaaa):"));
        painelCampos.add(dataField);
        painelCampos.add(new JLabel("Status:"));
        painelCampos.add(statusField);
=======
        painelCampos.add(tiposervicoCombo);
        painelCampos.add(new JLabel("Data (dd/mm/aaaa):"));
        painelCampos.add(dataField);
        painelCampos.add(new JLabel("Status:"));
        painelCampos.add(statusCombo);
>>>>>>> 7938082 (atualização geral interface/DAO)

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
<<<<<<< HEAD
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
=======

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
>>>>>>> 7938082 (atualização geral interface/DAO)
        obsArea.setEditable(pode);

        editarButton.setText(pode ? "Salvar" : "Editar");
        editando = pode;
    }
<<<<<<< HEAD
=======

>>>>>>> 7938082 (atualização geral interface/DAO)
    private void cliqueEditar() {
        if (!editando) {
            liberaEdicao(true);
        } else {
            salvar();
        }
    }
<<<<<<< HEAD
=======

>>>>>>> 7938082 (atualização geral interface/DAO)
    private void salvar() {
        LocalDate novaData;
        try {
            novaData = LocalDate.parse(dataField.getText().trim(), Servico.FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/mm/aaaa.");
            return;
        }
<<<<<<< HEAD
        if (tipoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O tipo de serviço não pode ficar vazio.");
            return;
        }
        servico.setTipo(tipoField.getText().trim());
        servico.setData(novaData);
        servico.setStatus(statusField.getText().trim());
=======

        String tipoSelecionado = (String) tiposervicoCombo.getSelectedItem();
        if (tipoSelecionado == null || tipoSelecionado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de serviço.");
            return;
        }

        servico.setTipo(tipoSelecionado);
        servico.setData(novaData);
        servico.setStatus((String) statusCombo.getSelectedItem());
>>>>>>> 7938082 (atualização geral interface/DAO)
        servico.setObservacoes(obsArea.getText());

        if (aoSalvar != null) {
            aoSalvar.accept(servico);
        }
        liberaEdicao(false);
        JOptionPane.showMessageDialog(this, "Serviço salvo!");
    }
<<<<<<< HEAD
    private void excluir() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o serviço de id " + servico.getId() + "?",
=======

    private void excluir() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o serviço de id " + servico.getId() + "? Esse serviço será excluído para sempre!",
>>>>>>> 7938082 (atualização geral interface/DAO)
                "Excluir serviço", JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            if (aoExcluir != null) {
                aoExcluir.accept(servico);
            }
            dispose();
        }
    }
}