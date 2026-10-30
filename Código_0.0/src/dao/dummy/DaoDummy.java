package dao.dummy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dao.Dao;
import dao.DaoException;
import entities.Servico;

/** DAO em memória: os dados somem quando o programa fecha. Bom para testar a tela. */
public class DaoDummy implements Dao {

	private final TreeMap<Integer, Servico> servicos = new TreeMap<>();

	public DaoDummy() {
		List<Servico> iniciais = List.of(
				new Servico(1, "Carlos Silva", "Pintura", "Rua A, 123", LocalDate.of(2026, 10, 10), "Agendado", "Nenhuma"),
				new Servico(2, "Ana Souza", "Alvenaria", "Av B, 456", LocalDate.of(2026, 10, 12), "Em andamento", "Urgente"),
				new Servico(3, "Marcos Lima", "Instalação elétrica", "Praça C, 789", LocalDate.of(2026, 10, 15), "Concluído", "Revisar fiação")
		);
		for (Servico s : iniciais) {
			servicos.put(s.getId(), s);
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		return new ArrayList<>(servicos.values());
	}

	@Override
	public List<Servico> buscaByCliente(String nomeCliente) {
		String busca = nomeCliente.toLowerCase();
		List<Servico> resultado = new ArrayList<>();
		for (Servico s : servicos.values()) {
			if (s.getCliente().toLowerCase().contains(busca)) {
				resultado.add(s);
			}
		}
		return resultado;
	}

	@Override
	public void addServico(Servico servico) {
		int novoId = servicos.isEmpty() ? 1 : servicos.lastKey() + 1;
		servico.setId(novoId);
		servicos.put(novoId, servico);
	}

	@Override
	public void atualizaServico(Servico servico) {
		if (servico.getId() == null || !servicos.containsKey(servico.getId())) {
			throw new DaoException("Serviço não encontrado (id " + servico.getId() + ").");
		}
		servicos.put(servico.getId(), servico);
	}

	@Override
	public void removeServicoById(int id) {
		servicos.remove(id);
	}
}
