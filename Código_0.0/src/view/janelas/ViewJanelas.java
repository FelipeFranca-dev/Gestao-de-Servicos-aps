package view.janelas;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import entities.Servico;
import view.View;

public class ViewJanelas extends JFrame implements View {

    private static final long serialVersionUID = 1L;

    private final PanelAddServicos panelAddServicos = new PanelAddServicos();
    private final PanelTabela panelTabela = new PanelTabela();

    public ViewJanelas() {
        setLayout(new BorderLayout());

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Cadastrar Serviço", panelAddServicos);
        tabPane.addTab("Agenda de Serviços", panelTabela);
        add(tabPane, BorderLayout.CENTER);

        setTitle("Gestão de Serviços");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void addAcaoSalvarServico(ActionListener acao) {
        panelAddServicos.addAcaoSalvarServico(acao);
    }

    @Override public String getClienteSelecionado() { return panelAddServicos.getClienteSelecionado(); }
    @Override public String getTipoServico() { return panelAddServicos.getTipoServico(); }
    @Override public String getEndereco() { return panelAddServicos.getEndereco(); }
    @Override public String getData() { return panelAddServicos.getData(); }
    @Override public String getStatus() { return panelAddServicos.getStatus(); }
    @Override public String getObservacoes() { return panelAddServicos.getObservacoes(); }

    @Override
    public void limparFormulario() {
        panelAddServicos.limparCampos();
    }

    @Override
    public void setClientes(List<String> clientes) {
        panelAddServicos.setClientes(clientes);
    }

    @Override
    public void setTiposServico(List<String> tipos) {
        panelAddServicos.setTiposServico(tipos);
        panelTabela.setTiposServico(tipos);
    }

    @Override
    public void setEnderecos(List<String> enderecos) {
        panelAddServicos.setEnderecos(enderecos);
    }

    @Override
    public void setServicos(List<Servico> servicos) {
        panelTabela.setServicos(servicos);
    }

    @Override
    public void setAcaoEditarServico(Predicate<Servico> acao) {
        panelTabela.setAcaoEditar(acao);
    }

    @Override
    public void setAcaoExcluirServico(Predicate<Servico> acao) {
        panelTabela.setAcaoExcluir(acao);
    }

    @Override
    public void setAcaoRecarregar(Runnable acao) {
        panelTabela.setAcaoRecarregar(acao);
    }

    @Override
    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(janelaAtiva(), mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(janelaAtiva(), mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private Window janelaAtiva() {
        Window ativa = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        return ativa != null ? ativa : this;
    }
}
