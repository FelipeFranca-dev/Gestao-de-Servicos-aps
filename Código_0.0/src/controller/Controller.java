package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import dao.Dao;
import entities.Animal;
import view.View;

public class Controller {
	
	Dao model;
	View view;
	
	public Controller(Dao aModel, View aView) {
		this.model = aModel;
		this.view = aView;
		
		view.addAcaoMostraTodosAnimais(new AcaoListarTodosAnimais());
		view.addAcaoBuscaAnimalByNome(new AcaoBuscarAnimalByNome());
		view.addAcaoAddAnimal(new AcaoAddAnimal());
		view.addAcaoRemoveAnimalById(new AcaoRemoveAnimalById());
		
	}
	
	class AcaoListarTodosAnimais implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Animal> animais = model.getTodosAnimais();
			view.mostraAnimais(animais);
		}
	}
	
	class AcaoBuscarAnimalByNome implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String nome = view.getAnimalNomeBusca();
			List<Animal> animais = model.buscaByAnimalNome(nome);
			view.mostraAnimais(animais);
		}
	}
	
	class AcaoAddAnimal implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Animal animal = view.getAnimalFromUserToAdd();
				model.addAnimal(animal);
				view.showMessage("Animal adicionado com sucesso\n" + animal);
			}catch (IOException excp) {}
		}
	}
	
	class AcaoRemoveAnimalById implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int id = view.getIdAnimalFromUserToRemove();
				model.removeAnimalById(id);
			}catch (IOException excp) {}
		}
	}
	
	
}
