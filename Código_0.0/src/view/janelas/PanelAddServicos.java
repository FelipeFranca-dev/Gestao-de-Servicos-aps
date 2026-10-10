package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import entities.Servico;

public class PanelAddServicos extends JPanel {

    private static final long serialVersionUID = 1L;

    // Cliente
    private JComboBox<String> clienteCombo =
            new JComboBox<>();

    private JButton novoClienteButton =
            new JButton("Novo Cliente");

    // Dados do serviço
    private JComboBox<String> tipoServicoCombo =
            new JComboBox<>(new String[] {
                "Pintura",
                "Instalação elétrica",
                "Alvenaria",
            });

    private JTextField dataField = new JTextField(12);
    private JTextField valorField = new JTextField(12);
    private JTextField enderecoField = new JTextField(25);

    private JComboBox<String> statusCombo =
            new JComboBox<>(new String[] {
                "Orçamento",
                "Agendado",
                "Em andamento",
                "Concluído",
                "Cancelado"
            });

    private JTextArea descricaoArea = new JTextArea(3, 25);
    private JTextArea observacoesArea = new JTextArea(3, 25);

    // Materiais
    private JComboBox<String> materialCombo =
            new JComboBox<>();

    private JTextField quantidadeField =
            new JTextField(6);

    private JButton adicionarMaterialButton =
            new JButton("Adicionar");

    private JButton removerMaterialButton =
            new JButton("Remover selecionado");

    private DefaultTableModel materialModel =
            new DefaultTableModel(
                new Object[] {"Material", "Quantidade"}, 0
            ) {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private JTable materiaisTabela =
            new JTable(materialModel);

    // Ações principais
    private JButton salvarButton =
            new JButton("Salvar Serviço");

    private JButton limparButton =
            new JButton("Limpar Campos");

    public PanelAddServicos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel();
        formulario.setLayout(
                new BoxLayout(formulario, BoxLayout.Y_AXIS)
        );

        // 1. Cliente
        JPanel clientePanel = criarSecao("Cliente");
        clientePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        clientePanel.add(new JLabel("Cliente:"));
        clientePanel.add(clienteCombo);
        clientePanel.add(novoClienteButton);

        // 2. Dados do serviço
        JPanel dadosPanel = criarSecao("Dados do Serviço");
        dadosPanel.setLayout(new GridLayout(0, 2, 8, 8));

        dadosPanel.add(new JLabel("Tipo de serviço:"));
        dadosPanel.add(tipoServicoCombo);

        dadosPanel.add(new JLabel("Data (dd/mm/aaaa):"));
        dadosPanel.add(dataField);

        dadosPanel.add(new JLabel("Valor (R$):"));
        dadosPanel.add(valorField);

        dadosPanel.add(new JLabel("Status:"));
        dadosPanel.add(statusCombo);

        dadosPanel.add(new JLabel("Endereço do serviço:"));
        dadosPanel.add(enderecoField);

        dadosPanel.add(new JLabel("Descrição:"));
        dadosPanel.add(new JScrollPane(descricaoArea));

        // 3. Materiais
        JPanel materiaisPanel = criarSecao("Materiais");

        JPanel adicionarPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        adicionarPanel.add(new JLabel("Material:"));
        adicionarPanel.add(materialCombo);

        adicionarPanel.add(new JLabel("Quantidade:"));
        adicionarPanel.add(quantidadeField);

        adicionarPanel.add(adicionarMaterialButton);

        materiaisPanel.add(adicionarPanel, BorderLayout.NORTH);
        materiaisPanel.add(
                new JScrollPane(materiaisTabela),
                BorderLayout.CENTER
        );

        JPanel removerPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        removerPanel.add(removerMaterialButton);
        materiaisPanel.add(removerPanel, BorderLayout.SOUTH);

        // 4. Observações
        JPanel observacoesPanel = criarSecao("Observações");
        observacoesPanel.setLayout(new BorderLayout());
        observacoesPanel.add(
                new JScrollPane(observacoesArea),
                BorderLayout.CENTER
        );

        // Montagem do formulário
        formulario.add(clientePanel);
        formulario.add(dadosPanel);
        formulario.add(materiaisPanel);
        formulario.add(observacoesPanel);

        add(new JScrollPane(formulario), BorderLayout.CENTER);

        // Botões inferiores
        JPanel botoesPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        botoesPanel.add(limparButton);
        botoesPanel.add(salvarButton);

        add(botoesPanel, BorderLayout.SOUTH);

        // Adicionar material à lista visual
        adicionarMaterialButton.addActionListener(e -> {
            String material =
                    (String) materialCombo.getSelectedItem();

            String quantidadeTexto =
                    quantidadeField.getText().trim();

            if (material == null || material.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um material."
                );
                return;
            }

            try {
                double quantidade =
                        Double.parseDouble(quantidadeTexto);

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "A quantidade deve ser maior que zero."
                    );
                    return;
                }

                materialModel.addRow(new Object[] {
                    material, quantidade
                });

                quantidadeField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe uma quantidade válida."
                );
            }
        });

        // Remover material selecionado
        removerMaterialButton.addActionListener(e -> {
            int linha = materiaisTabela.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um material da lista."
                );
                return;
            }

            materialModel.removeRow(linha);
        });

        // Limpar formulário
        limparButton.addActionListener(e -> limparCampos());
    }

    private JPanel criarSecao(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBorder(
                BorderFactory.createTitledBorder(titulo)
        );

        return panel;
    }

    public void limparCampos() {
        clienteCombo.setSelectedIndex(-1);
        tipoServicoCombo.setSelectedIndex(0);
        dataField.setText("");
        valorField.setText("");
        enderecoField.setText("");
        statusCombo.setSelectedIndex(0);
        descricaoArea.setText("");
        observacoesArea.setText("");
        materialCombo.setSelectedIndex(-1);
        quantidadeField.setText("");
        materialModel.setRowCount(0);
    }

    // Métodos para conectar os botões ao Controller

    public void addAcaoSalvarServico(ActionListener al) {
        salvarButton.addActionListener(al);
    }

    public void addAcaoNovoCliente(ActionListener al) {
        novoClienteButton.addActionListener(al);
    }

    // Métodos para o Controller consultar os campos

    public String getClienteSelecionado() {
        return (String) clienteCombo.getSelectedItem();
    }

    public String getTipoServico() {
        return (String) tipoServicoCombo.getSelectedItem();
    }

    public String getData() {
        return dataField.getText().trim();
    }

    public String getValor() {
        return valorField.getText().trim();
    }

    public String getEndereco() {
        return enderecoField.getText().trim();
    }

    public String getStatus() {
        return (String) statusCombo.getSelectedItem();
    }

    public String getDescricao() {
        return descricaoArea.getText().trim();
    }

    public String getObservacoes() {
        return observacoesArea.getText().trim();
    }

    public DefaultTableModel getMaterialModel() {
        return materialModel;
    }
}