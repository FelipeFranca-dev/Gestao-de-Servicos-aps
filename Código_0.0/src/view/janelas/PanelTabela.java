package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import entities.Servico;

public class PanelTabela extends JPanel {
	private Object[] colNames = {"Id", "Cliente", "Tipo de serviço", "Preço", "Status"};
	private DefaultTableModel dtm = new DefaultTableModel(colNames, 0) {
		@Override
		public Class<?> getColumnClass(int coluna) {
			switch (coluna) {
				case 0: return Integer.class;
				case 3: return Double.class;
				default: return String.class;
			}
		}
		@Override
		public boolean isCellEditable(int linha, int coluna) {
			return false;
		}
	};

	private JTable tabela = new JTable(dtm);
	private JScrollPane scrollPane = new JScrollPane(tabela);
	private List<Servico> servicosNaTabela;
	private TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(dtm);
	private JTextField buscaField = new JTextField(20);
	private JButton buscarButton = new JButton("Buscar");
	private JButton mostraTodosButton = new JButton("Mostrar Todos os serviços");

	private JComboBox<String> ordenarPorBox = new JComboBox<>(
			new String[]{"Id", "Cliente", "Tipo de serviço", "Preço"});
	private JComboBox<String> ordemBox = new JComboBox<>(
			new String[]{"Crescente", "Decrescente"});
	private JComboBox<String> tipoBox = new JComboBox<>(
			new String[]{"Todos", "Pintura"});
	private JComboBox<String> statusBox = new JComboBox<>(
			new String[]{"Todos", "Orçamento", "Em andamento", "Concluído"});

	public PanelTabela() {
		setLayout(new BorderLayout());
		tabela.setRowSorter(sorter);

		JPanel linhaBusca = new JPanel(new FlowLayout());
		linhaBusca.add(buscaField);
		linhaBusca.add(buscarButton);
		linhaBusca.add(mostraTodosButton);

		JPanel linhaOrdem = new JPanel(new FlowLayout());
		linhaOrdem.add(ordenarPorBox);
		linhaOrdem.add(ordemBox);

		JPanel linhaFiltros = new JPanel(new FlowLayout());
		linhaFiltros.add(tipoBox);
		linhaFiltros.add(statusBox);

		JPanel panelTopo = new JPanel();
		panelTopo.setLayout(new BoxLayout(panelTopo, BoxLayout.PAGE_AXIS));
		panelTopo.add(linhaBusca);
		panelTopo.add(linhaOrdem);
		panelTopo.add(linhaFiltros);

		add(panelTopo, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);

		ordenarPorBox.addActionListener(e -> aplicaOrdenacao());
		ordemBox.addActionListener(e -> aplicaOrdenacao());
		buscarButton.addActionListener(e -> aplicaFiltros());
		buscaField.addActionListener(e -> aplicaFiltros()); // apertar Enter também busca
		tipoBox.addActionListener(e -> aplicaFiltros());
		statusBox.addActionListener(e -> aplicaFiltros());
		mostraTodosButton.addActionListener(e -> mostraTodos());
	}
	void limpaTabela() {
		dtm.setRowCount(0);
	}
	public void preencheTabela(List<Servico> servicos) {
		servicosNaTabela = servicos;
		limpaTabela();
		for (var s : servicos) {
			Object[] row = {s.getId(), s.getCliente(), s.getTipo(), s.getPreco(), s.getStatus()};
			dtm.addRow(row);
		}
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
	private void aplicaFiltros() {
		ArrayList<RowFilter<DefaultTableModel, Object>> filtros = new ArrayList<>();
		String texto = buscaField.getText().trim();
		if (!texto.isEmpty()) {
			filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 1));
		}
		String tipo = (String) tipoBox.getSelectedItem();
		if (!tipo.equals("Todos")) {
			filtros.add(RowFilter.regexFilter("^" + Pattern.quote(tipo) + "$", 2));
		}
		String status = (String) statusBox.getSelectedItem();
		if (!status.equals("Todos")) {
			filtros.add(RowFilter.regexFilter("^" + Pattern.quote(status) + "$", 4));
		}
		if (filtros.isEmpty()) {
			sorter.setRowFilter(null); // sem filtro: mostra tudo
		} else {
			sorter.setRowFilter(RowFilter.andFilter(filtros));
		}
	}
	private void mostraTodos() {
		buscaField.setText("");
		tipoBox.setSelectedIndex(0);
		statusBox.setSelectedIndex(0);
		aplicaFiltros();
	}

}
