package dao.dao_csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.Dao;
import entities.Servico;

public class DaoCsv implements Dao {
	
	public static final String FILE_PATH_STRING = "servicos.csv";
	public static Path PATH_TO_FILE = Path.of("files");
	public static Path FILE_PATH = PATH_TO_FILE.resolve(FILE_PATH_STRING);
	
	public DaoCsv() {
		try {
			// Cria a pasta "files" caso ela não exista
			if (!Files.exists(PATH_TO_FILE)) {
				Files.createDirectories(PATH_TO_FILE);
			}
			// Cria o arquivo "servicos.csv" caso ele não exista
			if (!Files.exists(FILE_PATH)) {
				Files.createFile(FILE_PATH);
			}
		} catch (IOException e) {
			System.err.println("Nao foi possivel criar o caminho ou o arquivo: " + FILE_PATH_STRING);
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		List<Servico> servicos = new ArrayList<>();
		if (!Files.exists(FILE_PATH)) {
			return servicos;
		}
		
		try {
			List<String> linhas = Files.readAllLines(FILE_PATH);
			for (String linha : linhas) {
				if (linha.trim().isEmpty()) continue;
				
				// Divide a linha usando ponto e vírgula (;)
				// Ordem: id;cliente;tipo;endereco;data;status;observacoes
				String[] partes = linha.split(";");
				if (partes.length >= 7) {
					Integer id = Integer.parseInt(partes[0].trim());
					String cliente = partes[1].trim();
					String tipo = partes[2].trim();
					String endereco = partes[3].trim();
					LocalDate data = LocalDate.parse(partes[4].trim(), Servico.FORMATO_DATA);
					String status = partes[5].trim();
					String observacoes = partes[6].trim();
					
					servicos.add(new Servico(id, cliente, tipo, endereco, data, status, observacoes));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return servicos;
	}

	@Override
	public List<Servico> buscaByCliente(String nomeCliente) {
		List<Servico> todos = getTodosServicos();
		List<Servico> resultado = new ArrayList<>();
		for (Servico s : todos) {
			if (s.getCliente().toLowerCase().contains(nomeCliente.toLowerCase())) {
				resultado.add(s);
			}
		}
		return resultado;
	}

	@Override
	public void addServico(Servico servico) {
		List<Servico> todos = getTodosServicos();
		
		// Gera o próximo ID baseado nos registros existentes
		int novoId = todos.isEmpty() ? 1 : todos.stream().mapToInt(Servico::getId).max().orElse(0) + 1;
		servico.setId(novoId);
		
		// Monta a linha no formato CSV
		String linha = String.format("%d;%s;%s;%s;%s;%s;%s%n",
				servico.getId(),
				servico.getCliente(),
				servico.getTipo(),
				servico.getEndereco(),
				servico.getData().format(Servico.FORMATO_DATA),
				servico.getStatus(),
				servico.getObservacoes()
		);
		
		try {
			// Adiciona a nova linha ao final do arquivo
			Files.writeString(FILE_PATH, linha, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeServicoById(int id) {
		List<Servico> todos = getTodosServicos();
		todos.removeIf(s -> s.getId() == id);
		
		try {
			List<String> novasLinhas = new ArrayList<>();
			for (Servico s : todos) {
				String linha = String.format("%d;%s;%s;%s;%s;%s;%s",
						s.getId(),
						s.getCliente(),
						s.getTipo(),
						s.getEndereco(),
						s.getData().format(Servico.FORMATO_DATA),
						s.getStatus(),
						s.getObservacoes()
				);
				novasLinhas.add(linha);
			}
			// Reescreve o arquivo atualizado sem o item removido
			Files.write(FILE_PATH, novasLinhas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}