package dao;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

import entities.Servico;

public interface Dao {

    List<String> TIPOS_PADRAO = List.of(
            "Pintura", "Alvenaria", "Instalação elétrica", "Hidráulica");

    List<Servico> getTodosServicos();

    List<Servico> buscaByCliente(String nomeCliente);

    void addServico(Servico servico);

    void atualizaServico(Servico servico);

    void removeServicoById(int id);

    default List<String> getTiposServicos() {
        return valoresUnicos(getTodosServicos(), Servico::getTipo, TIPOS_PADRAO);
    }

    default List<String> getClientes() {
        return valoresUnicos(getTodosServicos(), Servico::getCliente, List.of());
    }

    default List<String> getEnderecos() {
        return valoresUnicos(getTodosServicos(), Servico::getEndereco, List.of());
    }

    private static List<String> valoresUnicos(List<Servico> servicos,
                                              Function<Servico, String> campo, Collection<String> padroes) {

        TreeSet<String> unicos = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        unicos.addAll(padroes);
        for (Servico s : servicos) {
            String valor = campo.apply(s);
            if (valor != null && !valor.isBlank()) {
                unicos.add(valor.trim());
            }
        }
        return List.copyOf(unicos);
    }
}
