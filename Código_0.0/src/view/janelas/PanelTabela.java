package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import entities.Servico;

public class PanelTabela extends JPanel {

	private Object[] colNames = {"Cliente", "Tipo de serviço", "Data", "Status"};
	private DefaultTableModel dtm = new DefaultTableModel(colNames, 0) {
		@Override
		public Class<?> getColumnClass(int coluna) {
			if (coluna == 2) {
				return LocalDate.class;
			}
			return String.class;
		}
		@Override
		public boolean isCellEditable(int linha, int coluna) {
			return false;
		}
	};
	private JTable tabela = new JTable(dtm);
	private JScrollPane scrollPane = new JScrollPane(tabela);
	private List<Servico> servicosNaTabela = new ArrayList<>();
	private TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(dtm);

	private JTextField buscaField = new JTextField(20);
	private JButton buscarButton = new JButton("Buscar");
	private JButton mostraTodosButton = new JButton("Mostrar Todos os serviços");

	private JComboBox<String> ordenarPorBox = new JComboBox<>(
			new String[]{"Cliente", "Tipo de serviço", "Data", "Status"});
	private JComboBox<String> ordemBox = new JComboBox<>(
			new String[]{"Crescente", "Decrescente"});

	private JButton detalhesButton = new JButton("Ver detalhes do serviço");

	private Consumer<Servico> acaoSalvar;
	private Consumer<Servico> acaoExcluir;

	public PanelTabela() {
		setLayout(new BorderLayout());
		tabela.setRowSorter(sorter);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tabela.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
			@Override
			protected void setValue(Object valor) {
				if (valor instanceof LocalDate) {
					setText(((LocalDate) valor).format(Servico.FORMATO_DATA));
				} else {
					super.setValue(valor);
				}
			}
		});
		JPanel linhaBusca = new JPanel(new FlowLayout());
		linhaBusca.add(buscaField);
		linhaBusca.add(buscarButton);
		linhaBusca.add(mostraTodosButton);

		JPanel linhaOrdem = new JPanel(new FlowLayout());
		linhaOrdem.add(ordenarPorBox);
		linhaOrdem.add(ordemBox);

		JPanel panelTopo = new JPanel();
		panelTopo.setLayout(new BoxLayout(panelTopo, BoxLayout.PAGE_AXIS));
		panelTopo.add(linhaBusca);
		panelTopo.add(linhaOrdem);

		JPanel panelBaixo = new JPanel(new FlowLayout());
		panelBaixo.add(detalhesButton);

		add(panelTopo, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(panelBaixo, BorderLayout.PAGE_END);

		ordenarPorBox.addActionListener(e -> aplicaOrdenacao());
		ordemBox.addActionListener(e -> aplicaOrdenacao());
		buscarButton.addActionListener(e -> aplicaBusca());
		buscaField.addActionListener(e -> aplicaBusca());
		mostraTodosButton.addActionListener(e -> mostraTodos());
		detalhesButton.addActionListener(e -> abreDetalhes());

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					abreDetalhes();
				}
			}
		});
	}
	void limpaTabela() {
		dtm.setRowCount(0);
	}
	public void preencheTabela(List<Servico> servicos) {
		servicosNaTabela = new ArrayList<>(servicos);
		limpaTabela();
		for (var s : servicosNaTabela) {
			Object[] row = {s.getCliente(), s.getTipo(), s.getData(), s.getStatus()};
			dtm.addRow(row);
		}
	}
	public void setAcaoSalvar(Consumer<Servico> acao) {
		acaoSalvar = acao;
	}

	public void setAcaoExcluir(Consumer<Servico> acao) {
		acaoExcluir = acao;
	}
	private void aplicaOrdenacao() {
		int coluna = ordenarPorBox.getSelectedIndex();

		SortOrder ordem = SortOrder.ASCENDING;
		if (ordemBox.getSelectedItem().equals("Decrescente")) {
			ordem = SortOrder.DESCENDING;
		}
		ArrayList<RowSorter.SortKey> chaves = new ArrayList<>();
		chaves.add(new RowSorter.SortKey(coluna, ordem));
		sorter.setSortKeys(chaves);
	}
	private void aplicaBusca() {
		String texto = buscaField.getText().trim();
		if (texto.isEmpty()) {
			sorter.setRowFilter(null); // sem filtro: mostra tudo
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 0));
		}
	}
	private void mostraTodos() {
		buscaField.setText("");
		sorter.setRowFilter(null);
	}
	private void abreDetalhes() {
		int linhaNaTela = tabela.getSelectedRow();
		if (linhaNaTela < 0) {
			JOptionPane.showMessageDialog(this, "Selecione um serviço na tabela.");
			return;
		}
		// a linha da tela pode estar ordenada/filtrada, então converte para a do modelo
		int linhaNoModelo = tabela.convertRowIndexToModel(linhaNaTela);
		Servico escolhido = servicosNaTabela.get(linhaNoModelo);

		JanelaDetalhes janela = new JanelaDetalhes(
				SwingUtilities.getWindowAncestor(this),
				escolhido,
				editado -> {
					if (acaoSalvar != null) {
						acaoSalvar.accept(editado);
					}
					preencheTabela(servicosNaTabela);
				},
				excluido -> {
					if (acaoExcluir != null) {
						acaoExcluir.accept(excluido);
					}
					servicosNaTabela.remove(excluido);
					preencheTabela(servicosNaTabela);
				});
		janela.setVisible(true);
	}
}