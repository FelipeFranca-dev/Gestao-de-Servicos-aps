package view.janelas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import entities.Servico;

public class PanelTabela extends JPanel{
	
	private Object[] colNames = {"Id", "Nome", "Idade"};
	private DefaultTableModel dtm = new DefaultTableModel(colNames, 0);
	private JTable tabela = new JTable(dtm);
	private JScrollPane scrollPane = new JScrollPane(tabela);
	
	private List<Servico> animaisNaTabela;
	
	private JTextField buscaNomeField = new JTextField(20);
	private JButton buscaNomeButton = new JButton("Busca por Nome");
	private JButton mostraTodosButton = new JButton("Mostrar Todos");
	private JButton apagarButton = new JButton("Apagar");
	
	private ActionListener apagarAction;
	
	public PanelTabela() {
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelControles = new JPanel();
		panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.LINE_AXIS));
		panelControles.add(buscaNomeField);
		panelControles.add(buscaNomeButton);
		panelControles.add(mostraTodosButton);
		panelControles.add(apagarButton);
		add(panelControles, BorderLayout.PAGE_END);
		
		apagarButton.addActionListener(e -> {
			int row = tabela.getSelectedRow();
			if(row<0 || row>=animaisNaTabela.size()) {
				JOptionPane.showMessageDialog(PanelTabela.this, "Selecione uma linha da tabela para apagar");
				return;
			}
			Servico a = animaisNaTabela.get(row);
			int opcao = JOptionPane.showConfirmDialog(PanelTabela.this, "Deseja remover o animal de id " + a.getId() + "?");
			if(opcao == JOptionPane.YES_OPTION) {
				apagarAction.actionPerformed(new ActionEvent(apagarButton, 0, ""));
				
				// atualiza tabela com os animais ainda nela
				animaisNaTabela.remove(row);
				preencheTabela(animaisNaTabela);
			}
		});
		
		
	}
	
	public int getIdAnimalFromUserToRemove() throws IOException {
		return animaisNaTabela.get(tabela.getSelectedRow()).getId();
	}

	
	void limpaTabela() {
		dtm.setRowCount(0);
	}
	
	void preencheTabela(List<Servico> animais) {
		animaisNaTabela = animais;
		
		limpaTabela();
		for(var a: animais) {
			Object[] row = {a.getId(), a.getNome(), a.getIdade()};
			dtm.addRow(row);
		}
	}
	
	String getAnimalNomeBusca() {
		return buscaNomeField.getText().trim();
	}
	
	
	
	
	
	public void addAcaoMostraTodosAnimais(ActionListener al) {
		mostraTodosButton.addActionListener(al);
	}

	public void addAcaoBuscaAnimalByNome(ActionListener al) {
		buscaNomeButton.addActionListener(al);
	}

	public void addAcaoRemoveAnimalById(ActionListener al) {
		apagarAction = al;
	}

	

}
