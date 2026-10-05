package view;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import entities.Animal;

public interface View {
	
	void mostraAnimais(List<Animal> animais);
	Animal getAnimalFromUserToAdd() throws IOException;
	int getIdAnimalFromUserToRemove() throws IOException;
	String getAnimalNomeBusca();
	
	void showMessage(String message);
	
	void addAcaoMostraTodosAnimais(ActionListener al);
	void addAcaoBuscaAnimalByNome(ActionListener al);
	void addAcaoAddAnimal(ActionListener al);
	void addAcaoRemoveAnimalById(ActionListener al);

}
