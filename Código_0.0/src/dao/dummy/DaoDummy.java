package dao.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dao.Dao;
import entities.Servico;

public class DaoDummy implements Dao{
	
	List<Servico> animaisList = Arrays.asList(
			new Servico(1, "Brutus", 7),
			new Servico(2, "Mili", 8),
			new Servico(3, "Chirriro", 9)
			);
	// animais indexados pelo indice
	Map<Integer, Servico> animais = new TreeMap<>();
	{
		for(var a: animaisList)
			animais.put(a.getId(), a);
	}

	@Override
	public List<Servico> getTodosAnimais() {
		return new ArrayList<>(animais.values());
	}

	@Override
	public List<Servico> buscaByAnimalNome(String keyNome) {
		List<Servico> resultado = new ArrayList<Servico>();
		for(var a: animais.values())
			if(a.getNome().toLowerCase().contains(keyNome.toLowerCase()))
				resultado.add(a);
		return resultado;
	}

	@Override
	public void addAnimal(Servico servico) {
		int newId = Collections.max(animais.keySet());
		newId++;
		servico.setId(newId);
		animais.put(servico.getId(), servico);
	}

	@Override
	public void removeAnimalById(int id) {
		animais.remove(id);
	}

}
