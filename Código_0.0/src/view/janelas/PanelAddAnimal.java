package view.janelas;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entities.Animal;

public class PanelAddAnimal extends JPanel{

	private JTextField fieldNome = new JTextField(20);
	private JTextField fieldIdade = new JTextField(20);
	
	private JButton addAnimalButton = new JButton("Adicionar Animal");
	
	public PanelAddAnimal() {
		
		setLayout(new FlowLayout());
		JPanel controles = new JPanel(new GridLayout(0, 2));
		add(controles);
		
		controles.add(new JLabel("Nome", JLabel.TRAILING));
		controles.add(fieldNome);
		
		controles.add(new JLabel("Idade", JLabel.TRAILING));
		controles.add(fieldIdade);
		
		controles.add(addAnimalButton);
		
		JButton limpaCamposButton = new JButton("Limpar Campos");
		controles.add(limpaCamposButton);
		limpaCamposButton.addActionListener(e -> {
			fieldNome.setText("");
			fieldIdade.setText("");
		});
		
	}
	
	Animal getAnimal() throws IOException{
		String nome = fieldNome.getText().strip();
		int idade = -1;
		
		if(nome.isBlank()) {
			JOptionPane.showMessageDialog(fieldIdade, "Nome esta vazio");
			throw new IOException();
		}
		
		try {
			idade = Integer.parseInt(fieldIdade.getText());
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(fieldIdade, "Idade nao eh um numero inteiro");
			throw new IOException();
		}
		
		
		return new Animal(nome, idade);
	}
	
	void addAcaoAddAnimal(ActionListener al) {
		addAnimalButton.addActionListener(al);
	}

	
}
