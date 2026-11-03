package view;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Predicate;

import entities.Servico;

public interface View {

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

    void setServicos(List<Servico> servicos);

    void setAcaoEditarServico(Predicate<Servico> acao);

    void setAcaoExcluirServico(Predicate<Servico> acao);

    void setAcaoRecarregar(Runnable acao);

    void mostrarErro(String mensagem);
    void mostrarMensagem(String mensagem);
}
