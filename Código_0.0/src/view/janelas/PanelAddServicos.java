package view.janelas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PanelAddServicos extends JPanel {
    private static final long serialVersionUID = 1L;

    // Cliente, tipo e endereço são escolhidos em combobox para evitar nomes repetidos/digitados diferente
    // Os botões de Novo (cliente, tipo de serviço ou endereço) adicionam uma opção à lista; ela passa a existir só quando o serviço é criado
    private JComboBox<String> clienteCombo = new JComboBox<>();
    private JButton novoClienteButton = new JButton("Novo cliente");

    private JComboBox<String> tipoServicoCombo = new JComboBox<>();
    private JButton novoTipoButton = new JButton("Novo tipo");

    private JComboBox<String> enderecoCombo = new JComboBox<>();
    private JButton novoEnderecoButton = new JButton("Novo endereço");

    private JTextField dataField = new JTextField(12);

    // status não tem botão de novo status; não dá pra criar um status novo, só usar os pré-definidos
    private JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Em orçamento",
            "Agendado",
            "Em andamento",
            "Concluído",
            "Cancelado"
    });

    // observações são diferentes em cada serviço por definição, então não precisa do combobox pra evitar duplicatas
    private JTextArea observacoesArea = new JTextArea(3, 25);

    private JButton salvarButton = new JButton("Cadastrar serviço");
    private JButton limparButton = new JButton("Limpar campos");

    private boolean modoEdicao = false;

    public PanelAddServicos() {

        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("Novo serviço");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        titulo.setForeground(new Color(31, 95, 168));
        add(titulo, BorderLayout.NORTH);

        JPanel cartao = new JPanel(new GridBagLayout());
        cartao.setBackground(Color.WHITE);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 222)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        adicionaLinha(cartao, 0, "Cliente:", clienteCombo, novoClienteButton);
        adicionaLinha(cartao, 1, "Tipo de serviço:", tipoServicoCombo, novoTipoButton);
        adicionaLinha(cartao, 2, "Endereço:", enderecoCombo, novoEnderecoButton);
        adicionaLinha(cartao, 3, "Data (dd/mm/aaaa):", dataField, null);
        adicionaLinha(cartao, 4, "Status:", statusCombo, null);

        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 5;
        g.insets = new Insets(6, 6, 6, 6);

        g.gridx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        cartao.add(new JLabel("Observações:"), g);

        g.gridx = 1;
        g.gridwidth = 2;
        g.weightx = 1;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        cartao.add(new JScrollPane(observacoesArea), g);

        add(cartao, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesPanel.add(limparButton);
        botoesPanel.add(salvarButton);
        add(botoesPanel, BorderLayout.SOUTH);

        limparButton.addActionListener(e -> limparCampos());

        configurarBotoesCadastroAuxiliar();

        // ao criar um serviço novo, o status inicial sempre é: "Em orçamento", os combobox permitem catalogar serviços já concluídos
        statusCombo.setSelectedIndex(0);
    }

    private void adicionaLinha(JPanel painel, int linha, String rotulo, JComponent campo, JButton botao) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = linha;
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        painel.add(new JLabel(rotulo), g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        if (botao == null) {
            g.gridwidth = 2;
        }
        painel.add(campo, g);

        if (botao != null) {
            g.gridx = 2;
            g.gridwidth = 1;
            g.weightx = 0;
            painel.add(botao, g);
        }
    }

    // Os botões Novo (cliente, tipo de serviço e endereço) abrem uma caixa de diálogo e adicionam o valor digitado ao combo.
    private void configurarBotoesCadastroAuxiliar() {

        novoClienteButton.addActionListener(e ->
                abrirCadastro("Cadastrar cliente", "Nome do cliente:", this::adicionarClienteNaLista));

        novoTipoButton.addActionListener(e ->
                abrirCadastro("Cadastrar tipo de serviço", "Nome do tipo de serviço:", this::adicionarTipoNaLista));

        novoEnderecoButton.addActionListener(e ->
                abrirCadastro("Cadastrar endereço", "Endereço:", this::adicionarEnderecoNaLista));
    }

    private void abrirCadastro(String titulo, String rotulo, Consumer<String> aoCadastrar) {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);

        CadastroOpcaoDialog dialog = new CadastroOpcaoDialog(janelaPai, titulo, rotulo);
        dialog.setVisible(true);

        String valor = dialog.getValorCadastrado();
        if (valor != null) {
            aoCadastrar.accept(valor);
        }
    }

    public void limparCampos() {
        clienteCombo.setSelectedIndex(-1);
        tipoServicoCombo.setSelectedIndex(-1);
        enderecoCombo.setSelectedIndex(-1);

        dataField.setText("");
        statusCombo.setSelectedIndex(0);
        observacoesArea.setText("");

        setModoEdicao(false);
    }

    public void setModoEdicao(boolean modoEdicao) {
        this.modoEdicao = modoEdicao;
        salvarButton.setText(modoEdicao ? "Salvar alterações" : "Cadastrar serviço");
    }

    public boolean isModoEdicao() {
        return modoEdicao;
    }

    public void setClientes(List<String> clientes) {
        preencherCombo(clienteCombo, clientes);
    }

    public void setTiposServico(List<String> tipos) {
        preencherCombo(tipoServicoCombo, tipos);
    }

    public void setEnderecos(List<String> enderecos) {
        preencherCombo(enderecoCombo, enderecos);
    }

    private void preencherCombo(JComboBox<String> combo, List<String> itens) {
        String selecionado = (String) combo.getSelectedItem();

        combo.removeAllItems();

        if (itens != null) {
            for (String item : itens) {
                if (item != null && !item.isBlank()) {
                    combo.addItem(item);
                }
            }
        }

        combo.setSelectedIndex(-1);
        if (selecionado != null && !selecionado.isBlank()) {
            adicionarOpcao(combo, selecionado);
        }
    }

    public void adicionarClienteNaLista(String cliente) {
        adicionarOpcao(clienteCombo, cliente);
    }

    public void adicionarTipoNaLista(String tipo) {
        adicionarOpcao(tipoServicoCombo, tipo);
    }

    public void adicionarEnderecoNaLista(String endereco) {
        adicionarOpcao(enderecoCombo, endereco);
    }

    // Se a opção já existe, só seleciona; senão adiciona e seleciona, e ignora as maiúsculas.
    private void adicionarOpcao(JComboBox<String> combo, String opcao) {
        if (opcao == null || opcao.isBlank()) {
            return;
        }

        for (int i = 0; i < combo.getItemCount(); i++) {
            if (opcao.equalsIgnoreCase(combo.getItemAt(i))) {
                combo.setSelectedIndex(i);
                return;
            }
        }

        combo.addItem(opcao);
        combo.setSelectedItem(opcao);
    }

    public void addAcaoSalvarServico(ActionListener al) {
        salvarButton.addActionListener(al);
    }

    public String getClienteSelecionado() {
        return (String) clienteCombo.getSelectedItem();
    }

    public String getTipoServico() {
        return (String) tipoServicoCombo.getSelectedItem();
    }

    public String getEndereco() {
        return (String) enderecoCombo.getSelectedItem();
    }

    public String getData() {
        return dataField.getText().trim();
    }

    public String getStatus() {
        return (String) statusCombo.getSelectedItem();
    }

    public String getObservacoes() {
        return observacoesArea.getText().trim();
    }
}