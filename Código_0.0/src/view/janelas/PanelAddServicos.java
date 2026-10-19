
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PanelAddServicos extends JPanel {

    private static final long serialVersionUID = 1L;

    // Cliente
    private JComboBox<String> clienteCombo = new JComboBox<>();
    private JButton novoClienteButton = new JButton("Novo cliente");

    // Tipo de serviço
    private JComboBox<String> tipoServicoCombo = new JComboBox<>();
    private JButton novoTipoButton = new JButton("Novo tipo");

    // Endereço do serviço
    private JComboBox<String> enderecoCombo = new JComboBox<>();
    private JButton novoEnderecoButton = new JButton("Novo endereço");

    // Dados do serviço
    private JTextField dataField = new JTextField(12);

    private JComboBox<String> statusCombo = new JComboBox<>(new String[] {
        "Em orçamento",
        "Agendado",
        "Em andamento",
        "Concluído",
        "Cancelado"
    });

    private JTextArea observacoesArea = new JTextArea(3, 25);

    // Ações principais
    private JButton salvarButton = new JButton("Cadastrar serviço");
    private JButton limparButton = new JButton("Limpar campos");

    private boolean modoEdicao = false;

    // Construtor
    public PanelAddServicos() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel formulario = new JPanel();

        formulario.setLayout(
            new BoxLayout(formulario, BoxLayout.Y_AXIS)
        );

        // 1. Cliente
        JPanel clientePanel = criarSecao("Cliente");

        clientePanel.setLayout(
            new FlowLayout(FlowLayout.LEFT)
        );

        clientePanel.add(new JLabel("Cliente:"));
        clientePanel.add(clienteCombo);
        clientePanel.add(novoClienteButton);

        // 2. Tipo de serviço
        JPanel tipoPanel = criarSecao("Tipo de serviço");

        tipoPanel.setLayout(
            new FlowLayout(FlowLayout.LEFT)
        );

        tipoPanel.add(new JLabel("Tipo:"));
        tipoPanel.add(tipoServicoCombo);
        tipoPanel.add(novoTipoButton);

        // 3. Endereço
        JPanel enderecoPanel = criarSecao("Endereço do serviço");

        enderecoPanel.setLayout(
            new FlowLayout(FlowLayout.LEFT)
        );

        enderecoPanel.add(new JLabel("Endereço:"));
        enderecoPanel.add(enderecoCombo);
        enderecoPanel.add(novoEnderecoButton);

        // 4. Dados gerais
        JPanel dadosPanel = criarSecao("Dados do serviço");

        dadosPanel.setLayout(
            new GridLayout(0, 2, 8, 8)
        );

        dadosPanel.add(new JLabel("Data (dd/mm/aa):"));
        dadosPanel.add(dataField);

        dadosPanel.add(new JLabel("Status:"));
        dadosPanel.add(statusCombo);

        // 5. Observações
        JPanel observacoesPanel = criarSecao("Observações");

        observacoesPanel.add(
            new JScrollPane(observacoesArea),
            BorderLayout.CENTER
        );

        // Montagem da tela
        formulario.add(clientePanel);
        formulario.add(tipoPanel);
        formulario.add(enderecoPanel);
        formulario.add(dadosPanel);
        formulario.add(observacoesPanel);

        add(
            new JScrollPane(formulario),
            BorderLayout.CENTER
        );

        // Botões inferiores
        JPanel botoesPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        botoesPanel.add(limparButton);
        botoesPanel.add(salvarButton);

        add(botoesPanel, BorderLayout.SOUTH);

        // Ação local da interface
        limparButton.addActionListener(
            e -> limparCampos()
        );

        // Botões de cadastro auxiliar
        configurarBotoesCadastroAuxiliar();

        // Status inicial
        statusCombo.setSelectedIndex(0);
    }

    // Criação de seções
    private JPanel criarSecao(String titulo) {

        JPanel panel = new JPanel(
            new BorderLayout(5, 5)
        );

        panel.setBorder(
            BorderFactory.createTitledBorder(titulo)
        );

        return panel;
    }

    // Cadastro auxiliar
    private void configurarBotoesCadastroAuxiliar() {

        novoClienteButton.addActionListener(e ->
            abrirCadastro(
                "Cadastrar cliente",
                "Nome do cliente:",
                this::adicionarClienteNaLista
            )
        );

        novoTipoButton.addActionListener(e ->
            abrirCadastro(
                "Cadastrar tipo de serviço",
                "Nome do tipo de serviço:",
                this::adicionarTipoNaLista
            )
        );

        novoEnderecoButton.addActionListener(e ->
            abrirCadastro(
                "Cadastrar endereço",
                "Endereço:",
                this::adicionarEnderecoNaLista
            )
        );
    }

    private void abrirCadastro(
            String titulo,
            String rotulo,
            Consumer<String> aoCadastrar) {

        Window janelaPai = SwingUtilities.getWindowAncestor(this);

        CadastroOpcaoDialog dialog = new CadastroOpcaoDialog(
            janelaPai,
            titulo,
            rotulo
        );

        dialog.setVisible(true);

        String valor = dialog.getValorCadastrado();

        if (valor != null) {
            aoCadastrar.accept(valor);
        }
    }

    // Limpar formulário
    public void limparCampos() {

        clienteCombo.setSelectedIndex(-1);
        tipoServicoCombo.setSelectedIndex(-1);
        enderecoCombo.setSelectedIndex(-1);

        dataField.setText("");
        statusCombo.setSelectedIndex(0);
        observacoesArea.setText("");

        setModoEdicao(false);
    }

    // Modo de cadastro / edição
    public void setModoEdicao(boolean modoEdicao) {

        this.modoEdicao = modoEdicao;

        if (modoEdicao) {
            salvarButton.setText("Salvar alterações");
        } else {
            salvarButton.setText("Cadastrar serviço");
        }
    }

    public boolean isModoEdicao() {
        return modoEdicao;
    }

    // Carregamento dos ComboBoxes
    public void setClientes(List<String> clientes) {
        preencherCombo(clienteCombo, clientes);
    }

    public void setTiposServico(List<String> tipos) {
        preencherCombo(tipoServicoCombo, tipos);
    }

    public void setEnderecos(List<String> enderecos) {
        preencherCombo(enderecoCombo, enderecos);
    }

    private void preencherCombo(
            JComboBox<String> combo,
            List<String> itens) {

        combo.removeAllItems();

        if (itens == null) {
            return;
        }

        for (String item : itens) {
            if (item != null && !item.isBlank()) {
                combo.addItem(item);
            }
        }

        combo.setSelectedIndex(-1);
    }

    // Adicionar opções às listas
    public void adicionarClienteNaLista(String cliente) {
        adicionarOpcao(clienteCombo, cliente);
    }

    public void adicionarTipoNaLista(String tipo) {
        adicionarOpcao(tipoServicoCombo, tipo);
    }

    public void adicionarEnderecoNaLista(String endereco) {
        adicionarOpcao(enderecoCombo, endereco);
    }

    private void adicionarOpcao(
            JComboBox<String> combo,
            String opcao) {

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

    // Mensagens da interface
    public void mostrarErro(String mensagem) {

        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public void mostrarMensagem(String mensagem) {

        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Informação",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public boolean confirmar(String mensagem) {

        int resposta = JOptionPane.showConfirmDialog(
            this,
            mensagem,
            "Confirmar operação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        return resposta == JOptionPane.YES_OPTION;
    }

    // Métodos de conexão com o Controller
    public void addAcaoSalvarServico(ActionListener al) {
        salvarButton.addActionListener(al);
    }

    public void addAcaoNovoCliente(ActionListener al) {
        novoClienteButton.addActionListener(al);
    }

    public void addAcaoNovoTipo(ActionListener al) {
        novoTipoButton.addActionListener(al);
    }

    public void addAcaoNovoEndereco(ActionListener al) {
        novoEnderecoButton.addActionListener(al);
    }

    // Getters
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
