package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PanelAddServicos extends JPanel {
    private static final long serialVersionUID = 1L;

    // Cliente, tipo e endereço são escolhidos em listas (para evitar nomes repetidos/digitados diferente).
    // Os botões "Novo ..." adicionam uma opção à lista; ela passa a existir de verdade
    // quando um serviço com ela é cadastrado.
    private JComboBox<String> clienteCombo = new JComboBox<>();
    private JButton novoClienteButton = new JButton("Novo cliente");

    private JComboBox<String> tipoServicoCombo = new JComboBox<>();
    private JButton novoTipoButton = new JButton("Novo tipo");

    private JComboBox<String> enderecoCombo = new JComboBox<>();
    private JButton novoEnderecoButton = new JButton("Novo endereço");

    private JTextField dataField = new JTextField(12);

    // status não tem botão de "novo": só se escolhe entre opções predefinidas
    private JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Em orçamento",
            "Agendado",
            "Em andamento",
            "Concluído",
            "Cancelado"
    });

    // observações são diferentes em cada serviço, então não há problema de duplicata
    private JTextArea observacoesArea = new JTextArea(3, 25);

    private JButton salvarButton = new JButton("Cadastrar serviço");
    private JButton limparButton = new JButton("Limpar campos");

    private boolean modoEdicao = false;

    public PanelAddServicos() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        JPanel clientePanel = criarSecao("Cliente");
        clientePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        clientePanel.add(new JLabel("Cliente:"));
        clientePanel.add(clienteCombo);
        clientePanel.add(novoClienteButton);

        JPanel tipoPanel = criarSecao("Tipo de serviço");
        tipoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.add(new JLabel("Tipo:"));
        tipoPanel.add(tipoServicoCombo);
        tipoPanel.add(novoTipoButton);

        JPanel enderecoPanel = criarSecao("Endereço do serviço");
        enderecoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        enderecoPanel.add(new JLabel("Endereço:"));
        enderecoPanel.add(enderecoCombo);
        enderecoPanel.add(novoEnderecoButton);

        JPanel dadosPanel = criarSecao("Dados do serviço");
        dadosPanel.setLayout(new GridLayout(0, 2, 8, 8));
        dadosPanel.add(new JLabel("Data (dd/mm/aaaa):"));
        dadosPanel.add(dataField);
        dadosPanel.add(new JLabel("Status:"));
        dadosPanel.add(statusCombo);

        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        JPanel observacoesPanel = criarSecao("Observações");
        observacoesPanel.add(new JScrollPane(observacoesArea), BorderLayout.CENTER);

        formulario.add(clientePanel);
        formulario.add(tipoPanel);
        formulario.add(enderecoPanel);
        formulario.add(dadosPanel);
        formulario.add(observacoesPanel);

        add(new JScrollPane(formulario), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesPanel.add(limparButton);
        botoesPanel.add(salvarButton);
        add(botoesPanel, BorderLayout.SOUTH);

        limparButton.addActionListener(e -> limparCampos());

        configurarBotoesCadastroAuxiliar();

        // status inicial: "Em orçamento" (índice 0)
        statusCombo.setSelectedIndex(0);
    }

    private JPanel criarSecao(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        return panel;
    }

    // Os botões "Novo ..." abrem um pequeno diálogo e adicionam o valor digitado ao combo.
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

    // ----- Listas dos combos (o Controller manda os valores do DAO) -----

    public void setClientes(List<String> clientes) {
        preencherCombo(clienteCombo, clientes);
    }

    public void setTiposServico(List<String> tipos) {
        preencherCombo(tipoServicoCombo, tipos);
    }

    public void setEnderecos(List<String> enderecos) {
        preencherCombo(enderecoCombo, enderecos);
    }

    /**
     * Troca os itens do combo mas mantém o que o usuário já tinha escolhido
     * (inclusive um "Novo cliente" que ainda não foi salvo no DAO).
     */
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

    // Se a opção já existe (ignorando maiúsculas), só seleciona; senão adiciona e seleciona.
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

    // ----- Ligação com o Controller -----

    public void addAcaoSalvarServico(ActionListener al) {
        salvarButton.addActionListener(al);
    }

    // ----- Getters -----

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
