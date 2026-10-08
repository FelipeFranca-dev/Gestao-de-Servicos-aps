package dao.dao_csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import dao.Dao;
import entities.Animal;

public class DaoCsv implements Dao{
	
	public static final List<String> PATH_TO_FILE_STRING = Arrays.asList("files");
	public static final String FILE_PATH_STRING = "animais.csv";
	
	public static Path PATH_TO_FILE;
	public static Path FILE_PATH;
	
	public DaoCsv() {
		try {
			PATH_TO_FILE = Files.createDirectories(Path.of("files")) ;
			FILE_PATH = Files.createFile(PATH_TO_FILE.resolve("animais.csv")) ;
		}catch (IOException e) {
			System.err.println("Nao foi possivel criar o caminho: " + PATH_TO_FILE_STRING);
			System.err.println("OU Nao foi possivel criar o arquivo: " + FILE_PATH_STRING);
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public List<Animal> getTodosAnimais() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Animal> buscaByAnimalNome(String keyNome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAnimal(Animal animal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAnimalById(int id) {
		// TODO Auto-generated method stub
		
	}
	
	

}
