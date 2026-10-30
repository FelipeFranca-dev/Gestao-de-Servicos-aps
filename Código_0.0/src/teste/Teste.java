package teste;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import controller.Controller;
import dao.Dao;
import dao.DaoException;
import dao.dao_csv.DaoCsv;
import dao.dummy.DaoDummy;
import dao.sql.DaoSql;
import view.janelas.ViewJanelas;

/**
 * Como escolher onde os dados ficam (argumento do programa):
 *   csv   -> arquivo files/servicos.csv (padrão, não precisa instalar nada)
 *   sql   -> banco MySQL (rode banco/schema.sql antes e coloque o .jar do conector no classpath)
 *   dummy -> só memória, os dados somem ao fechar
 */
public class Teste {

	public static void main(String[] args) {
		String modo = (args.length > 0) ? args[0].trim().toLowerCase() : "csv";

		EventQueue.invokeLater(() -> {
			try {
				Dao dao = criaDao(modo);
				new Controller(dao, new ViewJanelas());
			} catch (DaoException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao iniciar", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		});
	}

	private static Dao criaDao(String modo) {
		switch (modo) {
			case "dummy":
				return new DaoDummy();
			case "sql":
				if (DaoSql.testaConnection()) {
					return new DaoSql();
				}
				JOptionPane.showMessageDialog(null,
						"Não foi possível conectar ao MySQL.\n"
								+ "Verifique se o servidor está ligado, se o banco foi criado (banco/schema.sql)\n"
								+ "e se o .jar do mysql-connector está no classpath.\n\n"
								+ "O programa vai usar o arquivo CSV no lugar.",
						"Banco indisponível", JOptionPane.WARNING_MESSAGE);
				return new DaoCsv();
			default:
				return new DaoCsv();
		}
	}
}
