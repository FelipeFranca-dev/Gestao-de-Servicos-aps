package view.janelas;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Predicate;

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

    private static final long serialVersionUID = 1L;

    private Servico servico;

    // Cada ação devolve true se deu certo (o Controller já mostra o erro se falhar)
    private final Predicate<Servico> aoSalvar;
    private final Predicate<Servico> aoExcluir;

    private final JLabel idLabel = new JLabel();
    private final JTextField clienteField = new JTextField(20);
    private final JComboBox<String> tiposervicoCombo = new JComboBox<>();
    private final JTextField enderecoField = new JTextField(20);
    private final JTextField dataField = new JTextField(20);
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Em orçamento",
            "Agendado",
            "Em andamento",
            "Concluído",
            "Cancelado"
    });
    private final JTextArea obsArea = new JTextArea(6, 20);

    private final JButton editarButton = new JButton("Editar");
    private final JButton excluirButton = new JButton("Excluir");
    private final JButton fecharButton = new JButton("Fechar");

    private boolean editando = false;

    public JanelaDetalhes(Window dono, Servico servico, List<String> tiposDisponiveis,
                          Predicate<Servico> aoSalvar, Predicate<Servico> aoExcluir) {
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

        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(idLabel);
        painelCampos.add(new JLabel("Cliente:"));
        painelCampos.add(clienteField);
        painelCampos.add(new JLabel("Tipo de serviço:"));
        painelCampos.add(tiposervicoCombo);
        painelCampos.add(new JLabel("Endereço:"));
        painelCampos.add(enderecoField);
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
        fecharButton.addActionListener(e -> cliqueFechar());

        preencheCampos();
        liberaEdicao(false);

        pack();
        setLocationRelativeTo(dono);
    }

    private void preencheCampos() {
        idLabel.setText(String.valueOf(servico.getId()));
        clienteField.setText(servico.getCliente());
        selecionaOuAdiciona(tiposervicoCombo, servico.getTipo());
        enderecoField.setText(servico.getEndereco());
        dataField.setText(servico.getData() != null ? servico.getData().format(Servico.FORMATO_DATA) : "");
        selecionaOuAdiciona(statusCombo, servico.getStatus());
        obsArea.setText(servico.getObservacoes());
    }

    // JComboBox não editável ignora setSelectedItem de um valor que não está na lista,
    // então adicionamos o valor antes (ex.: um tipo antigo que não está mais na lista).
    private void selecionaOuAdiciona(JComboBox<String> combo, String valor) {
        if (valor == null || valor.isBlank()) {
            return;
        }
        boolean existe = false;
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (valor.equals(combo.getItemAt(i))) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            combo.addItem(valor);
        }
        combo.setSelectedItem(valor);
    }

    private void liberaEdicao(boolean pode) {
        clienteField.setEditable(pode);
        tiposervicoCombo.setEnabled(pode);
        enderecoField.setEditable(pode);
        dataField.setEditable(pode);
        statusCombo.setEnabled(pode);
        obsArea.setEditable(pode);

        editarButton.setText(pode ? "Salvar" : "Editar");
        fecharButton.setText(pode ? "Cancelar" : "Fechar");
        editando = pode;
    }

    private void cliqueEditar() {
        if (!editando) {
            liberaEdicao(true);
        } else {
            salvar();
        }
    }

    // Enquanto edita, o botão vira "Cancelar" e descarta as alterações; senão, fecha a janela.
    private void cliqueFechar() {
        if (editando) {
            preencheCampos();
            liberaEdicao(false);
        } else {
            dispose();
        }
    }

    private void salvar() {
        String cliente = clienteField.getText().trim();
        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente.");
            return;
        }

        String endereco = enderecoField.getText().trim();
        if (endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o endereço.");
            return;
        }

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

        // Cria uma cópia com os dados novos; o original só é trocado se o salvamento der certo.
        Servico atualizado = new Servico(
                servico.getId(),
                cliente,
                tipoSelecionado,
                endereco,
                novaData,
                (String) statusCombo.getSelectedItem(),
                obsArea.getText().trim());

        boolean salvou = (aoSalvar == null) || aoSalvar.test(atualizado);
        if (!salvou) {
            return; // o Controller já mostrou o erro; continua em modo de edição
        }

        servico = atualizado;
        preencheCampos();
        liberaEdicao(false);
        JOptionPane.showMessageDialog(this, "Serviço salvo!");
    }

    private void excluir() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o serviço de id " + servico.getId() + "? Esse serviço será excluído para sempre!",
                "Excluir serviço", JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            boolean excluiu = (aoExcluir == null) || aoExcluir.test(servico);
            if (excluiu) {
                dispose();
            }
        }
    }
}
