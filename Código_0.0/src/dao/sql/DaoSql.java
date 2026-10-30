package dao.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.Dao;
import dao.DaoException;
import entities.Servico;

/**
 * DAO para MySQL. Crie o banco rodando banco/schema.sql antes de usar.
 *
 * Por padrão conecta em localhost:3306, banco "gestao_servicos", usuário "root", sem senha.
 * Para mudar sem mexer no código, defina variáveis de ambiente:
 *   GS_DB_URL, GS_DB_USER, GS_DB_PASS
 * (o .jar do mysql-connector-j precisa estar no classpath)
 */
public class DaoSql implements Dao {

	private static final String URL = config("GS_DB_URL", "jdbc:mysql://localhost:3306/gestao_servicos");
	private static final String USER = config("GS_DB_USER", "root");
	private static final String PASS = config("GS_DB_PASS", "");

	private static final String COLUNAS = "id, cliente, tipo, endereco, data_servico, status, observacoes";

	private static String config(String nome, String padrao) {
		String valor = System.getenv(nome);
		return (valor == null || valor.isBlank()) ? padrao : valor;
	}

	/** Retorna true se conseguiu conectar. Útil para avisar o usuário logo na abertura. */
	public static boolean testaConnection() {
		try (Connection c = DriverManager.getConnection(URL, USER, PASS)) {
			System.out.println("Conexão com o banco estabelecida.");
			return true;
		} catch (SQLException e) {
			System.err.println("Não foi possível conectar ao banco: " + e.getMessage());
			return false;
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		final String query = "SELECT " + COLUNAS + " FROM servicos ORDER BY id";
		try (Connection c = DriverManager.getConnection(URL, USER, PASS);
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery(query)) {

			List<Servico> servicos = new ArrayList<>();
			while (rs.next()) {
				servicos.add(paraServico(rs));
			}
			return servicos;

		} catch (SQLException e) {
			throw new DaoException("Erro ao listar serviços: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Servico> buscaByCliente(String nomeCliente) {
		final String query = "SELECT " + COLUNAS + " FROM servicos WHERE cliente LIKE ? ORDER BY id";
		try (Connection c = DriverManager.getConnection(URL, USER, PASS);
			 PreparedStatement s = c.prepareStatement(query)) {

			// escapa \ % _ para que o usuário digitando "50%" não vire curinga
			String termo = nomeCliente.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
			s.setString(1, "%" + termo + "%");

			try (ResultSet rs = s.executeQuery()) {
				List<Servico> servicos = new ArrayList<>();
				while (rs.next()) {
					servicos.add(paraServico(rs));
				}
				return servicos;
			}

		} catch (SQLException e) {
			throw new DaoException("Erro ao buscar por cliente: " + e.getMessage(), e);
		}
	}

	@Override
	public void addServico(Servico servico) {
		final String query = "INSERT INTO servicos(cliente, tipo, endereco, data_servico, status, observacoes) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(URL, USER, PASS);
			 PreparedStatement s = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			s.setString(1, servico.getCliente());
			s.setString(2, servico.getTipo());
			s.setString(3, servico.getEndereco());
			s.setDate(4, Date.valueOf(servico.getData()));
			s.setString(5, servico.getStatus());
			s.setString(6, servico.getObservacoes());
			s.executeUpdate();

			// devolve o id que o banco gerou (AUTO_INCREMENT)
			try (ResultSet keys = s.getGeneratedKeys()) {
				if (keys.next()) {
					servico.setId(keys.getInt(1));
				}
			}

		} catch (SQLException e) {
			throw new DaoException("Erro ao cadastrar serviço: " + e.getMessage(), e);
		}
	}

	@Override
	public void atualizaServico(Servico servico) {
		final String query = "UPDATE servicos SET cliente=?, tipo=?, endereco=?, data_servico=?, "
				+ "status=?, observacoes=? WHERE id=?";
		try (Connection c = DriverManager.getConnection(URL, USER, PASS);
			 PreparedStatement s = c.prepareStatement(query)) {

			s.setString(1, servico.getCliente());
			s.setString(2, servico.getTipo());
			s.setString(3, servico.getEndereco());
			s.setDate(4, Date.valueOf(servico.getData()));
			s.setString(5, servico.getStatus());
			s.setString(6, servico.getObservacoes());
			s.setInt(7, servico.getId());

			if (s.executeUpdate() == 0) {
				throw new DaoException("Serviço não encontrado (id " + servico.getId() + ").");
			}

		} catch (SQLException e) {
			throw new DaoException("Erro ao atualizar serviço: " + e.getMessage(), e);
		}
	}

	@Override
	public void removeServicoById(int id) {
		final String query = "DELETE FROM servicos WHERE id=?";
		try (Connection c = DriverManager.getConnection(URL, USER, PASS);
			 PreparedStatement s = c.prepareStatement(query)) {

			s.setInt(1, id);
			s.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException("Erro ao excluir serviço: " + e.getMessage(), e);
		}
	}

	private static Servico paraServico(ResultSet rs) throws SQLException {
		return new Servico(
				rs.getInt("id"),
				rs.getString("cliente"),
				rs.getString("tipo"),
				rs.getString("endereco"),
				rs.getDate("data_servico").toLocalDate(),
				rs.getString("status"),
				rs.getString("observacoes"));
	}
}
