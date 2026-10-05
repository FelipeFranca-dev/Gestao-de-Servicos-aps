package view.janelas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import entities.Animal;
import view.View;

public class ViewJanelas extends JFrame implements View{
	
	PanelAddAnimal panelAddAnimal = new PanelAddAnimal();
	PanelTabela panelTabela = new PanelTabela();
	
	public ViewJanelas(){
		
		setLayout(new BorderLayout());
		
		JTabbedPane tabPane = new JTabbedPane();
		add(tabPane);
		
		tabPane.add("Adicionar", panelAddAnimal);
		
		tabPane.add("Ver Animais", panelTabela);
		
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	

	@Override
	public void mostraAnimais(List<Animal> animais) {
		panelTabela.preencheTabela(animais);
	}

	@Override
	public Animal getAnimalFromUserToAdd() throws IOException{
			return panelAddAnimal.getAnimal();
	}

	@Override
	public int getIdAnimalFromUserToRemove() throws IOException {
		return panelTabela.getIdAnimalFromUserToRemove();
	}

	@Override
	public String getAnimalNomeBusca() {
		return panelTabela.getAnimalNomeBusca();
	}

	@Override
	public void addAcaoMostraTodosAnimais(ActionListener al) {
		panelTabela.addAcaoMostraTodosAnimais(al);
	}

	@Override
	public void addAcaoBuscaAnimalByNome(ActionListener al) {
		panelTabela.addAcaoBuscaAnimalByNome(al);
	}

	@Override
	public void addAcaoAddAnimal(ActionListener al) {
		panelAddAnimal.addAcaoAddAnimal(al);
	}

	@Override
	public void addAcaoRemoveAnimalById(ActionListener al) {
		panelTabela.addAcaoRemoveAnimalById(al);
	}

}
