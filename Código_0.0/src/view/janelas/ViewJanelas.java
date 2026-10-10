package view.janelas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import entities.Servico;
import view.View;

public class ViewJanelas extends JFrame implements View {

    private static final long serialVersionUID = 1L;

    private PanelAddServicos panelAddServicos =
            new PanelAddServicos();

    private PanelTabela panelTabela =
            new PanelTabela();

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

}
