package dao.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dao.Dao;
import entities.Animal;

public class DaoDummy implements Dao{
	
	List<Animal> animaisList = Arrays.asList(
			new Animal(1, "Brutus", 7),
			new Animal(2, "Mili", 8),
			new Animal(3, "Chirriro", 9)
			);
	// animais indexados pelo indice
	Map<Integer, Animal> animais = new TreeMap<>();
	{
		for(var a: animaisList)
			animais.put(a.getId(), a);
	}

	@Override
	public List<Animal> getTodosAnimais() {
		return new ArrayList<>(animais.values());
	}

	@Override
	public List<Animal> buscaByAnimalNome(String keyNome) {
		List<Animal> resultado = new ArrayList<Animal>();
		for(var a: animais.values())
			if(a.getNome().toLowerCase().contains(keyNome.toLowerCase()))
				resultado.add(a);
		return resultado;
	}

	@Override
	public void addAnimal(Animal animal) {
		int newId = Collections.max(animais.keySet());
		newId++;
		animal.setId(newId);
		animais.put(animal.getId(), animal);
	}

	@Override
	public void removeAnimalById(int id) {
		animais.remove(id);
	}

}
