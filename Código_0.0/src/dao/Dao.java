package dao;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

import entities.Servico;

public interface Dao {

    /** Tipos que sempre aparecem na lista, mesmo sem nenhum serviço cadastrado. */
    List<String> TIPOS_PADRAO = List.of(
            "Pintura", "Alvenaria", "Instalação elétrica", "Hidráulica", "Jardinagem");

    List<Servico> getTodosServicos();

    List<Servico> buscaByCliente(String nomeCliente);

    /** Grava o serviço e define o id gerado nele (servico.getId()). */
    void addServico(Servico servico);

    /** Atualiza o serviço que tem o mesmo id. Lança DaoException se não existir. */
    void atualizaServico(Servico servico);

    void removeServicoById(int id);

    // As listas abaixo são derivadas dos serviços já cadastrados,
    // então qualquer DAO (dummy, csv, sql) ganha elas de graça.

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
        // ignora maiúsculas/minúsculas para não duplicar "Pintura" e "pintura"
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
