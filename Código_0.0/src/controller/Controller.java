package controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import dao.Dao;
import dao.DaoException;
import entities.Servico;
import view.View;

public class Controller {

	private final Dao model;
	private final View view;

	public Controller(Dao aModel, View aView) {
		this.model = aModel;
		this.view = aView;

		// liga as ações da tela a métodos do controller
		view.addAcaoSalvarServico(e -> cadastrarServico());
		view.setAcaoEditarServico(this::editarServico);
		view.setAcaoExcluirServico(this::excluirServico);
		view.setAcaoRecarregar(this::recarregar);

		recarregar();
	}

	/** Busca tudo no DAO e atualiza a tabela e as listas dos combos. */
	private void recarregar() {
		try {
			view.setServicos(model.getTodosServicos());
			view.setTiposServico(model.getTiposServicos());
			view.setClientes(model.getClientes());
			view.setEnderecos(model.getEnderecos());
		} catch (DaoException e) {
			view.mostrarErro("Não foi possível carregar os serviços:\n" + e.getMessage());
		}
	}

	private void cadastrarServico() {
		String cliente = view.getClienteSelecionado();
		String tipo = view.getTipoServico();
		String endereco = view.getEndereco();

		if (vazio(cliente)) {
			view.mostrarErro("Selecione um cliente (ou use \"Novo cliente\").");
			return;
		}
		if (vazio(tipo)) {
			view.mostrarErro("Selecione um tipo de serviço (ou use \"Novo tipo\").");
			return;
		}
		if (vazio(endereco)) {
			view.mostrarErro("Selecione um endereço (ou use \"Novo endereço\").");
			return;
		}

		LocalDate data;
		try {
			data = LocalDate.parse(view.getData(), Servico.FORMATO_DATA);
		} catch (DateTimeParseException e) {
			view.mostrarErro("Data inválida. Use o formato dd/mm/aaaa (ex.: 25/12/2026).");
			return;
		}

		Servico novo = new Servico(cliente.trim(), tipo.trim(), endereco.trim(),
				data, view.getStatus(), view.getObservacoes());

		try {
			model.addServico(novo);
		} catch (DaoException e) {
			view.mostrarErro("Não foi possível cadastrar o serviço:\n" + e.getMessage());
			return;
		}

		view.limparFormulario();
		recarregar();
		view.mostrarMensagem("Serviço cadastrado! (id " + novo.getId() + ")");
	}

	/** @return true se salvou com sucesso */
	private boolean editarServico(Servico servico) {
		try {
			model.atualizaServico(servico);
		} catch (DaoException e) {
			view.mostrarErro("Não foi possível salvar as alterações:\n" + e.getMessage());
			return false;
		}
		recarregar();
		return true;
	}

	/** @return true se excluiu com sucesso */
	private boolean excluirServico(Servico servico) {
		try {
			model.removeServicoById(servico.getId());
		} catch (DaoException e) {
			view.mostrarErro("Não foi possível excluir o serviço:\n" + e.getMessage());
			return false;
		}
		recarregar();
		return true;
	}

	private static boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}
}
