package dao;

import java.util.List;

import entities.Animal;

public interface Dao {
	List<Animal> getTodosAnimais();
	List<Animal> buscaByAnimalNome(String keyNome);
	void addAnimal(Animal animal);
	void removeAnimalById(int id);
}
