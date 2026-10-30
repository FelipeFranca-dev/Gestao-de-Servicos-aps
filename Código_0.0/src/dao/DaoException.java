package dao;

/**
 * Erro de acesso a dados (arquivo, banco etc.).
 * É "unchecked" para não obrigar try/catch em todo lugar;
 * o Controller captura e mostra a mensagem na tela.
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DaoException(String mensagem) {
        super(mensagem);
    }

    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
