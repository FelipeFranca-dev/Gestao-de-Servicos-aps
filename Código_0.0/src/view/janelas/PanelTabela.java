package view.janelas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;

import entities.Servico;

public class PanelTabela extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int COL_ID = 0;
	private static final int COL_DATA = 4;

	private final Object[] colNames = {"Id", "Cliente", "Tipo de serviço", "Status", "Data"};

	// A coluna Data guarda LocalDate de verdade (e não texto), assim a ordenação é cronológica.
	private final DefaultTableModel dtm = new DefaultTableModel(colNames, 0) {
		private static final long serialVersionUID = 1L;

		@Override
		public Class<?> getColumnClass(int coluna) {
			switch (coluna) {
				case COL_ID: return Integer.class;
				case COL_DATA: return LocalDate.class;
				default: return String.class;
			}
		}

		@Override
		public boolean isCellEditable(int linha, int coluna) {
			return false;
		}
	};

	private final JTable tabela = new JTable(dtm);
	private final JScrollPane scrollPane = new JScrollPane(tabela);
	private List<Servico> servicosNaTabela = new ArrayList<>();
	private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(dtm);

	// Componentes de busca e ordenação
	private final JTextField buscaField = new JTextField(15);
	private final JButton buscarButton = new JButton("Buscar");
	private final JButton limpaFiltrosButton = new JButton("Limpar Filtros");

	private final JComboBox<String> ordenarPorBox = new JComboBox<>(
			new String[]{"Id", "Cliente", "Tipo de serviço", "Status", "Data"});
	private final JComboBox<String> ordemBox = new JComboBox<>(
			new String[]{"Crescente", "Decrescente"});

	// Filtro pelos valores da coluna escolhida em "Ordenar por"
	private final JComboBox<String> valorColunaBox = new JComboBox<>();
	private boolean atualizandoValorColuna = false;

	private final JButton detalhesButton = new JButton("Ver detalhes do serviço");

	// Ligações com o Controller
	private List<String> tiposServico = new ArrayList<>();
	private Predicate<Servico> aoEditar;
	private Predicate<Servico> aoExcluir;
	private Runnable aoRecarregar;

	public PanelTabela() {
		setLayout(new BorderLayout());
		tabela.setRowSorter(sorter);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Mostra a data como dd/MM/aaaa
		tabela.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void setValue(Object valor) {
				setText(textoDaCelula(valor));
			}
		});

		// Faz a busca e o filtro enxergarem a data como "dd/MM/aaaa" (e não "aaaa-MM-dd")
		sorter.setStringConverter(new TableStringConverter() {
			@Override
			public String toString(TableModel model, int row, int column) {
				return textoDaCelula(model.getValueAt(row, column));
			}
		});

		// Linha 1: busca rápida global
		JPanel linhaBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
		linhaBusca.add(new JLabel("Buscar:"));
		linhaBusca.add(buscaField);
		linhaBusca.add(buscarButton);

		// Linha 2: ordenação + filtro por valores da coluna ordenada
		JPanel linhaOrdemEFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		linhaOrdemEFiltro.add(new JLabel("Ordenar por:"));
		linhaOrdemEFiltro.add(ordenarPorBox);
		linhaOrdemEFiltro.add(ordemBox);
		linhaOrdemEFiltro.add(new JLabel(" Filtrar valor:"));
		linhaOrdemEFiltro.add(valorColunaBox);
		linhaOrdemEFiltro.add(limpaFiltrosButton);

		JPanel panelTopo = new JPanel();
		panelTopo.setLayout(new BoxLayout(panelTopo, BoxLayout.PAGE_AXIS));
		panelTopo.add(linhaBusca);
		panelTopo.add(linhaOrdemEFiltro);

		JPanel panelBaixo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBaixo.add(detalhesButton);

		add(panelTopo, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(panelBaixo, BorderLayout.PAGE_END);

		// Eventos
		ordenarPorBox.addActionListener(e -> {
			reordenaColunaTabela();
			aplicaOrdenacao();
			atualizaValoresFiltroColuna(false);
		});
		ordemBox.addActionListener(e -> aplicaOrdenacao());
		valorColunaBox.addActionListener(e -> {
			if (!atualizandoValorColuna) {
				aplicaFiltros();
			}
		});
		buscarButton.addActionListener(e -> aplicaFiltros());
		buscaField.addActionListener(e -> aplicaFiltros());
		limpaFiltrosButton.addActionListener(e -> limpaTodosFiltros());
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

	// ----- Ligação com o Controller -----

	public void setAcaoEditar(Predicate<Servico> acao) {
		this.aoEditar = acao;
	}

	public void setAcaoExcluir(Predicate<Servico> acao) {
		this.aoExcluir = acao;
	}

	public void setAcaoRecarregar(Runnable acao) {
		this.aoRecarregar = acao;
	}

	public void setTiposServico(List<String> tipos) {
		this.tiposServico = (tipos == null) ? new ArrayList<>() : new ArrayList<>(tipos);
	}

	/** O Controller chama isto com a lista que veio do DAO. */
	public void setServicos(List<Servico> servicos) {
		this.servicosNaTabela = new ArrayList<>(servicos);
		dtm.setRowCount(0);
		for (Servico s : servicos) {
			dtm.addRow(new Object[]{s.getId(), s.getCliente(), s.getTipo(), s.getStatus(), s.getData()});
		}
		atualizaValoresFiltroColuna(true);
	}

	// ----- Texto das células -----

	private static String textoDaCelula(Object valor) {
		if (valor == null) {
			return "";
		}
		if (valor instanceof LocalDate) {
			return ((LocalDate) valor).format(Servico.FORMATO_DATA);
		}
		return valor.toString();
	}

	// ----- Ordenação e filtros -----

	/** Move a coluna escolhida em "Ordenar por" para o começo da tabela. */
	private void reordenaColunaTabela() {
		restauraOrdemOriginalDasColunas();
		int colModelo = ordenarPorBox.getSelectedIndex();
		tabela.moveColumn(tabela.convertColumnIndexToView(colModelo), 0);
	}

	private void restauraOrdemOriginalDasColunas() {
		for (int i = 0; i < tabela.getColumnCount(); i++) {
			tabela.moveColumn(tabela.convertColumnIndexToView(i), i);
		}
	}

	/**
	 * Recarrega as opções de "Filtrar valor" com os valores da coluna escolhida.
	 * @param preservarSelecao true ao recarregar os dados (mantém o filtro que o usuário tinha);
	 *                         false ao trocar de coluna ou limpar (volta para "Todos").
	 */
	private void atualizaValoresFiltroColuna(boolean preservarSelecao) {
		int colModelo = ordenarPorBox.getSelectedIndex();
		if (colModelo < 0) {
			return;
		}

		String anterior = preservarSelecao ? (String) valorColunaBox.getSelectedItem() : null;

		// TreeSet ordena pelo valor real: números como números e datas em ordem cronológica
		TreeSet<Object> valoresUnicos = new TreeSet<>();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			Object val = dtm.getValueAt(i, colModelo);
			if (val != null) {
				valoresUnicos.add(val);
			}
		}

		atualizandoValorColuna = true;
		try {
			valorColunaBox.removeAllItems();
			valorColunaBox.addItem("Todos");
			for (Object val : valoresUnicos) {
				valorColunaBox.addItem(textoDaCelula(val));
			}

			valorColunaBox.setSelectedIndex(0);
			if (anterior != null) {
				for (int i = 0; i < valorColunaBox.getItemCount(); i++) {
					if (anterior.equals(valorColunaBox.getItemAt(i))) {
						valorColunaBox.setSelectedIndex(i);
						break;
					}
				}
			}
		} finally {
			atualizandoValorColuna = false;
		}

		aplicaFiltros();
	}

	private void aplicaOrdenacao() {
		int coluna = ordenarPorBox.getSelectedIndex();
		SortOrder ordem = "Decrescente".equals(ordemBox.getSelectedItem())
				? SortOrder.DESCENDING : SortOrder.ASCENDING;

		List<RowSorter.SortKey> chaves = new ArrayList<>();
		chaves.add(new RowSorter.SortKey(coluna, ordem));
		sorter.setSortKeys(chaves);
	}

	private void aplicaFiltros() {
		List<RowFilter<DefaultTableModel, Object>> filtros = new ArrayList<>();

		// 1. Texto livre (busca em todas as colunas da tabela)
		String texto = buscaField.getText().trim();
		if (!texto.isEmpty()) {
			filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
		}

		// 2. Valor escolhido na coluna ativa
		String valorSelecionado = (String) valorColunaBox.getSelectedItem();
		if (valorSelecionado != null && !valorSelecionado.equals("Todos")) {
			int colModelo = ordenarPorBox.getSelectedIndex();
			filtros.add(RowFilter.regexFilter("^" + Pattern.quote(valorSelecionado) + "$", colModelo));
		}

		if (filtros.isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.andFilter(filtros));
		}
	}

	private void limpaTodosFiltros() {
		buscaField.setText("");
		ordenarPorBox.setSelectedIndex(0);
		ordemBox.setSelectedIndex(0);
		restauraOrdemOriginalDasColunas();
		aplicaOrdenacao();

		if (aoRecarregar != null) {
			aoRecarregar.run();
		}
		atualizaValoresFiltroColuna(false);
	}

	// ----- Detalhes -----

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
				tiposServico,
				aoEditar,
				aoExcluir);
		janela.setVisible(true);
	}
}
