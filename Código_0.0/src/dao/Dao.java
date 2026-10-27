package dao;

import java.util.List;

import entities.Servico;

public interface Dao {
    List<Servico> getTodosServicos();
    List<Servico> buscaByCliente(String nomeCliente);
    void addServico(Servico servico);
    void removeServicoById(int id);
    List<String> getTiposServicos();
}