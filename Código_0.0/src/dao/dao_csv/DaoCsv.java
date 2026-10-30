package dao.dao_csv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.Dao;
import dao.DaoException;
import entities.Servico;

/**
 * Guarda os serviços em files/servicos.csv.
 *
 * Formato (UTF-8, separado por ponto e vírgula):
 *   id;cliente;tipo;endereco;dd/MM/aaaa;status;observacoes
 *
 * Dentro dos campos, os caracteres especiais são "escapados":
 *   ";" vira "\;"   |   quebra de linha vira "\n"   |   "\" vira "\\"
 * Assim uma observação com ";" ou com várias linhas não quebra o arquivo.
 */
public class DaoCsv implements Dao {

	public static final String FILE_PATH_STRING = "servicos.csv";
	public static final Path PATH_TO_FILE = Path.of("files");
	public static final Path FILE_PATH = PATH_TO_FILE.resolve(FILE_PATH_STRING);

	private static final char SEPARADOR = ';';
	private static final int NUM_CAMPOS = 7;

	public DaoCsv() {
		try {
			if (!Files.exists(PATH_TO_FILE)) {
				Files.createDirectories(PATH_TO_FILE);
			}
			if (!Files.exists(FILE_PATH)) {
				Files.createFile(FILE_PATH);
			}
		} catch (IOException e) {
			throw new DaoException("Não foi possível criar a pasta/arquivo " + FILE_PATH.toAbsolutePath(), e);
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		List<Servico> servicos = new ArrayList<>();
		if (!Files.exists(FILE_PATH)) {
			return servicos;
		}

		List<String> linhas;
		try {
			linhas = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new DaoException("Não foi possível ler " + FILE_PATH.toAbsolutePath(), e);
		}

		for (int i = 0; i < linhas.size(); i++) {
			String linha = linhas.get(i);
			if (linha.isBlank()) {
				continue;
			}
			try {
				servicos.add(paraServico(linha));
			} catch (RuntimeException e) {
				// uma linha estragada não pode derrubar o programa nem esconder as outras
				System.err.println("Linha " + (i + 1) + " de " + FILE_PATH_STRING
						+ " ignorada (" + e.getMessage() + "): " + linha);
			}
		}
		return servicos;
	}

	@Override
	public List<Servico> buscaByCliente(String nomeCliente) {
		String busca = nomeCliente.toLowerCase();
		List<Servico> resultado = new ArrayList<>();
		for (Servico s : getTodosServicos()) {
			if (s.getCliente().toLowerCase().contains(busca)) {
				resultado.add(s);
			}
		}
		return resultado;
	}

	@Override
	public void addServico(Servico servico) {
		List<Servico> todos = getTodosServicos();
		int novoId = todos.stream().mapToInt(Servico::getId).max().orElse(0) + 1;
		servico.setId(novoId);
		todos.add(servico);
		salvarTodos(todos);
	}

	@Override
	public void atualizaServico(Servico servico) {
		List<Servico> todos = getTodosServicos();
		boolean achou = false;
		for (int i = 0; i < todos.size(); i++) {
			if (todos.get(i).getId().equals(servico.getId())) {
				todos.set(i, servico);
				achou = true;
				break;
			}
		}
		if (!achou) {
			throw new DaoException("Serviço não encontrado (id " + servico.getId() + ").");
		}
		salvarTodos(todos);
	}

	@Override
	public void removeServicoById(int id) {
		List<Servico> todos = getTodosServicos();
		if (todos.removeIf(s -> s.getId() == id)) {
			salvarTodos(todos);
		}
	}

	// ---------------------------------------------------------------
	// Leitura e escrita de linhas
	// ---------------------------------------------------------------

	private void salvarTodos(List<Servico> servicos) {
		List<String> linhas = new ArrayList<>();
		for (Servico s : servicos) {
			linhas.add(paraLinha(s));
		}
		try {
			Files.write(FILE_PATH, linhas, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new DaoException("Não foi possível gravar em " + FILE_PATH.toAbsolutePath(), e);
		}
	}

	private static String paraLinha(Servico s) {
		return s.getId() + String.valueOf(SEPARADOR)
				+ escapar(s.getCliente()) + SEPARADOR
				+ escapar(s.getTipo()) + SEPARADOR
				+ escapar(s.getEndereco()) + SEPARADOR
				+ s.getData().format(Servico.FORMATO_DATA) + SEPARADOR
				+ escapar(s.getStatus()) + SEPARADOR
				+ escapar(s.getObservacoes());
	}

	private static Servico paraServico(String linha) {
		List<String> campos = separarCampos(linha);
		if (campos.size() != NUM_CAMPOS) {
			throw new IllegalArgumentException("esperava " + NUM_CAMPOS + " campos, achou " + campos.size());
		}
		Integer id = Integer.parseInt(campos.get(0));
		LocalDate data = LocalDate.parse(campos.get(4), Servico.FORMATO_DATA);
		return new Servico(id, campos.get(1), campos.get(2), campos.get(3),
				data, campos.get(5), campos.get(6));
	}

	private static String escapar(String texto) {
		if (texto == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (char c : texto.toCharArray()) {
			switch (c) {
				case '\\': sb.append("\\\\"); break;
				case SEPARADOR: sb.append("\\;"); break;
				case '\n': sb.append("\\n"); break;
				case '\r': break; // descarta (Windows manda \r\n)
				default: sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Divide a linha nos ";" que NÃO estão escapados e já desfaz o escape.
	 * Diferente de linha.split(";"), mantém campos vazios no final
	 * (uma observação vazia não faz mais o serviço sumir).
	 */
	private static List<String> separarCampos(String linha) {
		List<String> campos = new ArrayList<>();
		StringBuilder atual = new StringBuilder();
		for (int i = 0; i < linha.length(); i++) {
			char c = linha.charAt(i);
			if (c == '\\' && i + 1 < linha.length()) {
				char proximo = linha.charAt(++i);
				atual.append(proximo == 'n' ? '\n' : proximo);
			} else if (c == SEPARADOR) {
				campos.add(atual.toString().trim());
				atual.setLength(0);
			} else {
				atual.append(c);
			}
		}
		campos.add(atual.toString().trim());
		return campos;
	}
}
