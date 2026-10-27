package dao.dummy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import dao.Dao;
import entities.Servico;

public class DaoDummy implements Dao {

	// Lista inicial mockada compatível com a classe Servico
	List<Servico> servicosList = Arrays.asList(
			new Servico(1, "Carlos Silva", "Pintura", "Rua A, 123", LocalDate.of(2026, 10, 10), "Agendado", "Nenhuma"),
			new Servico(2, "Ana Souza", "Alvenaria", "Av B, 456", LocalDate.of(2026, 10, 12), "Em andamento", "Urgente"),
			new Servico(3, "Marcos Lima", "Instalação elétrica", "Praça C, 789", LocalDate.of(2026, 10, 15), "Concluído", "Revisar fiação")
	);

	Map<Integer, Servico> servicos = new TreeMap<>();

	{
		for (var s : servicosList) {
			servicos.put(s.getId(), s);
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		return new ArrayList<>(servicos.values());
	}

	@Override
	public List<Servico> buscaByCliente(String nomeCliente) {
		List<Servico> resultado = new ArrayList<>();
		for (var s : servicos.values()) {
			if (s.getCliente().toLowerCase().contains(nomeCliente.toLowerCase())) {
				resultado.add(s);
			}
		}
		return resultado;
	}

	@Override
	public void addServico(Servico servico) {
		int newId = servicos.isEmpty() ? 1 : Collections.max(servicos.keySet()) + 1;
		servico.setId(newId);
		servicos.put(servico.getId(), servico);
	}

	@Override
	public void removeServicoById(int id) {
		servicos.remove(id);
	}

	@Override
	public List<String> getTiposServicos() {
		// Tipos padrão sempre disponíveis + os que já existem nos serviços
		TreeSet<String> tipos = new TreeSet<>(Arrays.asList(
				"Pintura", "Alvenaria", "Instalação elétrica", "Hidráulica"));
		for (Servico s : servicos.values()) {
			if (s.getTipo() != null && !s.getTipo().isEmpty()) {
				tipos.add(s.getTipo());
			}
		}
		return new ArrayList<>(tipos);
	}
}