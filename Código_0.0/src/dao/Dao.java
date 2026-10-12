package dao;

import java.util.List;

import entities.Servico;

public interface Dao {
	List<Servico> getTodosAnimais();
	List<Servico> buscaByAnimalNome(String keyNome);
	void addAnimal(Servico servico);
	void removeAnimalById(int id);
}
