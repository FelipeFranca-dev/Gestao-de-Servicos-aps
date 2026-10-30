package view;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Predicate;

import entities.Servico;

/**
 * O que o Controller precisa saber da tela.
 * Assim o Controller não depende de Swing e dá para trocar a interface depois.
 */
public interface View {

    // ----- Cadastro de serviço -----
    void addAcaoSalvarServico(ActionListener acao);

    String getClienteSelecionado();
    String getTipoServico();
    String getEndereco();
    String getData();
    String getStatus();
    String getObservacoes();

    void limparFormulario();

    void setClientes(List<String> clientes);
    void setTiposServico(List<String> tipos);
    void setEnderecos(List<String> enderecos);

    // ----- Agenda (tabela) -----
    void setServicos(List<Servico> servicos);

    /** Chamada quando o usuário salva uma edição. Retorna true se deu certo. */
    void setAcaoEditarServico(Predicate<Servico> acao);

    /** Chamada quando o usuário confirma a exclusão. Retorna true se deu certo. */
    void setAcaoExcluirServico(Predicate<Servico> acao);

    /** Chamada quando o usuário pede para recarregar os dados (botão Limpar Filtros). */
    void setAcaoRecarregar(Runnable acao);

    // ----- Mensagens -----
    void mostrarErro(String mensagem);
    void mostrarMensagem(String mensagem);
}
