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
 * DAO para MySQL, usando as 3 tabelas do banco "aps_servicos":
 *
 *   clientes           (id, nome)
 *   dados_servicos     (id, tipo, endereco, data, status, observacoes)
 *   clientes_servicos  (id_clientes, id_servicos)   <- liga o cliente ao serviço
 *
 * Por padrão conecta em localhost:3306, banco "aps_servicos", usuário "root", sem senha.
 * Para mudar sem mexer no código, defina variáveis de ambiente:
 *   GS_DB_URL, GS_DB_USER, GS_DB_PASS
 * (o .jar do mysql-connector-j precisa estar no classpath)
 */
public class DaoSql implements Dao {

	private static final String URL = config("GS_DB_URL", "jdbc:mysql://localhost:3306/aps_servicos");
	private static final String USER = config("GS_DB_USER", "root");
	private static final String PASS = config("GS_DB_PASS", "");

	// Junta os três "cadernos": o serviço, a ligação e o nome do cliente.
	// LEFT JOIN: se algum serviço estiver sem cliente ligado, ele ainda aparece (com nome vazio).
	private static final String SELECT_SERVICOS =
			"SELECT d.id, COALESCE(c.nome, '') AS cliente, d.tipo, d.endereco, "
					+ "d.`data` AS data_servico, d.`status` AS status_servico, d.observacoes "
					+ "FROM dados_servicos d "
					+ "LEFT JOIN clientes_servicos cs ON cs.id_servicos = d.id "
					+ "LEFT JOIN clientes c ON c.id = cs.id_clientes ";

	private static String config(String nome, String padrao) {
		String valor = System.getenv(nome);
		return (valor == null || valor.isBlank()) ? padrao : valor;
	}

	private static Connection abreConexao() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}

	/** Retorna true se conseguiu conectar. Útil para avisar o usuário logo na abertura. */
	public static boolean testaConnection() {
		try (Connection c = abreConexao()) {
			System.out.println("Conexão com o banco estabelecida.");
			return true;
		} catch (SQLException e) {
			System.err.println("Não foi possível conectar ao banco: " + e.getMessage());
			return false;
		}
	}

	@Override
	public List<Servico> getTodosServicos() {
		final String query = SELECT_SERVICOS + "ORDER BY d.id";
		try (Connection c = abreConexao();
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
		final String query = SELECT_SERVICOS + "WHERE c.nome LIKE ? ORDER BY d.id";
		try (Connection c = abreConexao();
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

	/** Lista os clientes direto da tabela clientes (inclusive os que ainda não têm serviço). */
	@Override
	public List<String> getClientes() {
		final String query = "SELECT nome FROM clientes ORDER BY nome";
		try (Connection c = abreConexao();
			 Statement s = c.createStatement();
			 ResultSet rs = s.executeQuery(query)) {

			List<String> nomes = new ArrayList<>();
			while (rs.next()) {
				nomes.add(rs.getString("nome"));
			}
			return nomes;

		} catch (SQLException e) {
			throw new DaoException("Erro ao listar clientes: " + e.getMessage(), e);
		}
	}

	/**
	 * Cadastrar um serviço mexe em 3 tabelas. Tudo acontece numa "transação":
	 * ou grava os três passos, ou não grava nada (rollback).
	 */
	@Override
	public void addServico(Servico servico) {
		try (Connection c = abreConexao()) {
			c.setAutoCommit(false);
			try {
				int idCliente = buscaOuCriaCliente(c, servico.getCliente());
				int idServico = insereServico(c, servico);
				ligaClienteAoServico(c, idCliente, idServico);
				c.commit();
				servico.setId(idServico); // devolve o id que o banco gerou
			} catch (SQLException | RuntimeException e) {
				c.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DaoException("Erro ao cadastrar serviço: " + e.getMessage(), e);
		}
	}

	/** Atualiza os dados do serviço. O cliente ligado a ele não muda por aqui. */
	@Override
	public void atualizaServico(Servico servico) {
		final String query = "UPDATE dados_servicos SET tipo=?, endereco=?, `data`=?, "
				+ "`status`=?, observacoes=? WHERE id=?";
		try (Connection c = abreConexao();
			 PreparedStatement s = c.prepareStatement(query)) {

			s.setString(1, servico.getTipo());
			s.setString(2, servico.getEndereco());
			s.setDate(3, Date.valueOf(servico.getData()));
			s.setString(4, servico.getStatus());
			s.setString(5, servico.getObservacoes());
			s.setInt(6, servico.getId());

			if (s.executeUpdate() == 0) {
				throw new DaoException("Serviço não encontrado (id " + servico.getId() + ").");
			}

		} catch (SQLException e) {
			throw new DaoException("Erro ao atualizar serviço: " + e.getMessage(), e);
		}
	}

	/** Apaga a ligação e depois o serviço. O cliente continua cadastrado. */
	@Override
	public void removeServicoById(int id) {
		try (Connection c = abreConexao()) {
			c.setAutoCommit(false);
			try {
				try (PreparedStatement s = c.prepareStatement(
						"DELETE FROM clientes_servicos WHERE id_servicos=?")) {
					s.setInt(1, id);
					s.executeUpdate();
				}
				try (PreparedStatement s = c.prepareStatement(
						"DELETE FROM dados_servicos WHERE id=?")) {
					s.setInt(1, id);
					s.executeUpdate();
				}
				c.commit();
			} catch (SQLException | RuntimeException e) {
				c.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DaoException("Erro ao excluir serviço: " + e.getMessage(), e);
		}
	}

	// ---------- passos pequenos usados pelo addServico ----------

	/** Procura o cliente pelo nome. Se não existir, cadastra. Devolve o id dele. */
	private static int buscaOuCriaCliente(Connection c, String nome) throws SQLException {
		String nomeLimpo = nome.trim();

		try (PreparedStatement s = c.prepareStatement("SELECT id FROM clientes WHERE nome = ? LIMIT 1")) {
			s.setString(1, nomeLimpo);
			try (ResultSet rs = s.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		try (PreparedStatement s = c.prepareStatement(
				"INSERT INTO clientes(nome) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
			s.setString(1, nomeLimpo);
			s.executeUpdate();
			try (ResultSet keys = s.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}
		}

		throw new SQLException("Não foi possível obter o id do cliente \"" + nomeLimpo + "\".");
	}

	/** Grava os dados do serviço e devolve o id gerado pelo banco. */
	private static int insereServico(Connection c, Servico servico) throws SQLException {
		final String query = "INSERT INTO dados_servicos(tipo, endereco, `data`, `status`, observacoes) "
				+ "VALUES(?, ?, ?, ?, ?)";
		try (PreparedStatement s = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			s.setString(1, servico.getTipo());
			s.setString(2, servico.getEndereco());
			s.setDate(3, Date.valueOf(servico.getData()));
			s.setString(4, servico.getStatus());
			s.setString(5, servico.getObservacoes());
			s.executeUpdate();

			try (ResultSet keys = s.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}
		}
		throw new SQLException("Não foi possível obter o id do serviço cadastrado.");
	}

	/** Escreve na tabela clientes_servicos: "este cliente contratou este serviço". */
	private static void ligaClienteAoServico(Connection c, int idCliente, int idServico) throws SQLException {
		try (PreparedStatement s = c.prepareStatement(
				"INSERT INTO clientes_servicos(id_clientes, id_servicos) VALUES(?, ?)")) {
			s.setInt(1, idCliente);
			s.setInt(2, idServico);
			s.executeUpdate();
		}
	}

	private static Servico paraServico(ResultSet rs) throws SQLException {
		Date data = rs.getDate("data_servico");
		return new Servico(
				rs.getInt("id"),
				rs.getString("cliente"),
				rs.getString("tipo"),
				rs.getString("endereco"),
				data == null ? null : data.toLocalDate(),
				rs.getString("status_servico"),
				rs.getString("observacoes"));
	}
}