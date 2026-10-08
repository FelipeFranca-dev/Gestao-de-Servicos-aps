package dao.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.Dao;
import entities.Animal;

public class DaoSql implements Dao{
	
	static private String USER = "root";
	static private String PASS = "";
	static private String DATABASE = "veterinario_unip";
	static private String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	
	public static void testaConnection() {
		try(Connection c = DriverManager.getConnection(URL, USER, PASS)){
			System.out.println("conexao estabelecida");
		}catch (SQLException e) {
			System.err.println("nao fez a conection");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}


	@Override
	public List<Animal> getTodosAnimais() {
		List<Animal> animais = new ArrayList<>();
		
		final String query = "SELECT * FROM animais"; 
		
		
		try(
				Connection c = DriverManager.getConnection(URL, USER, PASS); 
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(query);
			){
			while(rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				int idade = rs.getInt("idade");
				animais.add(new Animal(id, nome, idade));
			}
			
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return animais;
	}

	@Override
	public List<Animal> buscaByAnimalNome(String keyNome) {
		List<Animal> animais = new ArrayList<>();
		
		final String query = "SELECT * FROM animais WHERE nome LIKE(?)"; 
		
		
		try(
				Connection c = DriverManager.getConnection(URL, USER, PASS); 
				PreparedStatement s = c.prepareStatement(query);
			){
			
			s.setString(1, "%"+keyNome+"%");
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				int idade = rs.getInt("idade");
				animais.add(new Animal(id, nome, idade));
			}
			
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return animais;
	}

	@Override
	public void addAnimal(Animal animal) {
		final String query = "INSERT INTO animais(nome, idade) VALUES(?, ?)"; 
		
		try(
				Connection c = DriverManager.getConnection(URL, USER, PASS); 
				PreparedStatement s = c.prepareStatement(query);
			){
			
			s.setString(1, animal.getNome());
			s.setInt(2, animal.getIdade());
			
			int count = s.executeUpdate();
			System.out.println("" + count + " linhas foram modificadas");
			
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void removeAnimalById(int id) {
		final String query = "DELETE FROM animais WHERE id=?"; 
		
		try(
				Connection c = DriverManager.getConnection(URL, USER, PASS); 
				PreparedStatement s = c.prepareStatement(query);
			){
			
			s.setInt(1, id);
			
			int count = s.executeUpdate();
			System.out.println("" + count + " linhas foram modificadas");
			
		}catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
