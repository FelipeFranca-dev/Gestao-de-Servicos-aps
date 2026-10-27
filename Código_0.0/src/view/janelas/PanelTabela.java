package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
<<<<<<< HEAD
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
=======
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
>>>>>>> 7938082 (atualização geral interface/DAO)
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
<<<<<<< HEAD
=======
import javax.swing.JLabel;
>>>>>>> 7938082 (atualização geral interface/DAO)
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
<<<<<<< HEAD
import javax.swing.table.DefaultTableCellRenderer;
=======
>>>>>>> 7938082 (atualização geral interface/DAO)
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.Dao;
import dao.dummy.DaoDummy;
import entities.Servico;

public class PanelTabela extends JPanel {

<<<<<<< HEAD
	private Object[] colNames = {"Cliente", "Tipo de serviço", "Data", "Status"};
	private DefaultTableModel dtm = new DefaultTableModel(colNames, 0) {
		@Override
		public Class<?> getColumnClass(int coluna) {
			if (coluna == 2) {
				return LocalDate.class;
=======
	private Dao dao;

	private Object[] colNames = {"Id", "Cliente", "Tipo de serviço", "Status", "Data"};

	private DefaultTableModel dtm = new DefaultTableModel(colNames, 0) {
		@Override
		public Class<?> getColumnClass(int coluna) {
			switch (coluna) {
				case 0: return Integer.class;
				default: return String.class;
>>>>>>> 7938082 (atualização geral interface/DAO)
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

<<<<<<< HEAD
	private JTextField buscaField = new JTextField(20);
=======
	// Componentes de Busca e Ordenação
	private JTextField buscaField = new JTextField(15);
>>>>>>> 7938082 (atualização geral interface/DAO)
	private JButton buscarButton = new JButton("Buscar");
	private JButton limpaFiltrosButton = new JButton("Limpar Filtros");

	private JComboBox<String> ordenarPorBox = new JComboBox<>(
<<<<<<< HEAD
			new String[]{"Cliente", "Tipo de serviço", "Data", "Status"});
	private JComboBox<String> ordemBox = new JComboBox<>(
			new String[]{"Crescente", "Decrescente"});

	private JButton detalhesButton = new JButton("Ver detalhes do serviço");

	private Consumer<Servico> acaoSalvar;
	private Consumer<Servico> acaoExcluir;
=======
			new String[]{"Id", "Cliente", "Tipo de serviço", "Status", "Data"});
	private JComboBox<String> ordemBox = new JComboBox<>(
			new String[]{"Crescente", "Decrescente"});

	// Filtro contextual de valores da coluna selecionada
	private JComboBox<String> valorColunaBox = new JComboBox<>();

	private JButton detalhesButton = new JButton("Ver detalhes do serviço");
>>>>>>> 7938082 (atualização geral interface/DAO)

	public PanelTabela() {
		setLayout(new BorderLayout());
		tabela.setRowSorter(sorter);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

<<<<<<< HEAD
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
=======
		dao = new DaoDummy();

		// Linha 1: Busca rápida global
		JPanel linhaBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
		linhaBusca.add(new JLabel("Buscar:"));
>>>>>>> 7938082 (atualização geral interface/DAO)
		linhaBusca.add(buscaField);
		linhaBusca.add(buscarButton);

<<<<<<< HEAD
		JPanel linhaOrdem = new JPanel(new FlowLayout());
		linhaOrdem.add(ordenarPorBox);
		linhaOrdem.add(ordemBox);
=======
		// Linha 2: Ordenação + Filtro por valores da coluna ordenada
		JPanel linhaOrdemEFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		linhaOrdemEFiltro.add(new JLabel("Ordenar por:"));
		linhaOrdemEFiltro.add(ordenarPorBox);
		linhaOrdemEFiltro.add(ordemBox);
		linhaOrdemEFiltro.add(new JLabel(" Filtrar valor:"));
		linhaOrdemEFiltro.add(valorColunaBox);
		linhaOrdemEFiltro.add(limpaFiltrosButton);
>>>>>>> 7938082 (atualização geral interface/DAO)

		JPanel panelTopo = new JPanel();
		panelTopo.setLayout(new BoxLayout(panelTopo, BoxLayout.PAGE_AXIS));
		panelTopo.add(linhaBusca);
<<<<<<< HEAD
		panelTopo.add(linhaOrdem);

		JPanel panelBaixo = new JPanel(new FlowLayout());
=======
		panelTopo.add(linhaOrdemEFiltro);

		JPanel panelBaixo = new JPanel(new FlowLayout(FlowLayout.LEFT));
>>>>>>> 7938082 (atualização geral interface/DAO)
		panelBaixo.add(detalhesButton);

		add(panelTopo, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(panelBaixo, BorderLayout.PAGE_END);

		carregarDadosDoDao();

		// Eventos
		ordenarPorBox.addActionListener(e -> {
			reordenaColunaTabela();
			aplicaOrdenacao();
			atualizaValoresFiltroColuna();
		});
		ordemBox.addActionListener(e -> aplicaOrdenacao());
<<<<<<< HEAD
		buscarButton.addActionListener(e -> aplicaBusca());
		buscaField.addActionListener(e -> aplicaBusca());
		mostraTodosButton.addActionListener(e -> mostraTodos());
=======
		valorColunaBox.addActionListener(e -> aplicaFiltros());
		buscarButton.addActionListener(e -> aplicaFiltros());
		buscaField.addActionListener(e -> aplicaFiltros());
		limpaFiltrosButton.addActionListener(e -> limpaTodosFiltros());
>>>>>>> 7938082 (atualização geral interface/DAO)
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

	public void carregarDadosDoDao() {
		List<Servico> lista = dao.getTodosServicos();
		preencheTabela(lista);
	}

	public void preencheTabela(List<Servico> servicos) {
<<<<<<< HEAD
		servicosNaTabela = new ArrayList<>(servicos);
		limpaTabela();
		for (var s : servicosNaTabela) {
			Object[] row = {s.getCliente(), s.getTipo(), s.getData(), s.getStatus()};
=======
		this.servicosNaTabela = new ArrayList<>(servicos);
		dtm.setRowCount(0);
		for (var s : servicos) {
			String dataFormatada = (s.getData() != null) ? s.getData().format(Servico.FORMATO_DATA) : "";
			Object[] row = {s.getId(), s.getCliente(), s.getTipo(), s.getStatus(), dataFormatada};
>>>>>>> 7938082 (atualização geral interface/DAO)
			dtm.addRow(row);
		}
		atualizaValoresFiltroColuna();
	}
<<<<<<< HEAD
	public void setAcaoSalvar(Consumer<Servico> acao) {
		acaoSalvar = acao;
	}

	public void setAcaoExcluir(Consumer<Servico> acao) {
		acaoExcluir = acao;
	}
=======

	private void reordenaColunaTabela() {
		int colModelo = ordenarPorBox.getSelectedIndex();
		int colVisivel = tabela.convertColumnIndexToView(colModelo);
		tabela.moveColumn(colVisivel, 0);
	}

	private void atualizaValoresFiltroColuna() {
		int colModelo = ordenarPorBox.getSelectedIndex();

		TreeSet<String> valoresUnicos = new TreeSet<>();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			Object val = dtm.getValueAt(i, colModelo);
			if (val != null) {
				valoresUnicos.add(val.toString());
			}
		}

		valorColunaBox.removeAllItems();
		valorColunaBox.addItem("Todos");
		for (String val : valoresUnicos) {
			valorColunaBox.addItem(val);
		}

		aplicaFiltros();
	}

>>>>>>> 7938082 (atualização geral interface/DAO)
	private void aplicaOrdenacao() {
		int coluna = ordenarPorBox.getSelectedIndex();
		SortOrder ordem = ordemBox.getSelectedItem().equals("Decrescente")
				? SortOrder.DESCENDING : SortOrder.ASCENDING;

		List<RowSorter.SortKey> chaves = new ArrayList<>();
		chaves.add(new RowSorter.SortKey(coluna, ordem));
		sorter.setSortKeys(chaves);
	}
<<<<<<< HEAD
	private void aplicaBusca() {
		String texto = buscaField.getText().trim();
		if (texto.isEmpty()) {
			sorter.setRowFilter(null); // sem filtro: mostra tudo
=======

	private void aplicaFiltros() {
		List<RowFilter<DefaultTableModel, Object>> filtros = new ArrayList<>();

		// 1. Filtro de texto livre (busca global)
		String texto = buscaField.getText().trim();
		if (!texto.isEmpty()) {
			filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
		}

		// 2. Filtro do valor selecionado na coluna ativa
		String valorSelecionado = (String) valorColunaBox.getSelectedItem();
		if (valorSelecionado != null && !valorSelecionado.equals("Todos")) {
			int colModelo = ordenarPorBox.getSelectedIndex();
			filtros.add(RowFilter.regexFilter("^" + Pattern.quote(valorSelecionado) + "$", colModelo));
		}

		if (filtros.isEmpty()) {
			sorter.setRowFilter(null);
>>>>>>> 7938082 (atualização geral interface/DAO)
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 0));
		}
	}

	private void limpaTodosFiltros() {
		buscaField.setText("");
<<<<<<< HEAD
		sorter.setRowFilter(null);
=======
		ordenarPorBox.setSelectedIndex(0);
		ordemBox.setSelectedIndex(0);
		reordenaColunaTabela();
		aplicaOrdenacao();
		carregarDadosDoDao();
>>>>>>> 7938082 (atualização geral interface/DAO)
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

<<<<<<< HEAD
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
=======
	private void abreDetalhes() {
		int linhaNaTela = tabela.getSelectedRow();
		if (linhaNaTela < 0) {
			JOptionPane.showMessageDialog(this, "Selecione um serviço na tabela.");
			return;
		}

		int linhaNoModelo = tabela.convertRowIndexToModel(linhaNaTela);
		Servico escolhido = servicosNaTabela.get(linhaNoModelo);

		JanelaDetalhes janela = new JanelaDetalhes(
				SwingUtilities.getWindowAncestor(this),
				escolhido,
				dao.getTiposServicos(),
				editado -> carregarDadosDoDao(),
				excluido -> {
					dao.removeServicoById(excluido.getId());
					carregarDadosDoDao();
>>>>>>> 7938082 (atualização geral interface/DAO)
				});
		janela.setVisible(true);
	}
}