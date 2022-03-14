package br.com.todo.todo.exceptions;


import br.com.todo.todo.dto.TarefaDtoDetalhado;
import br.com.todo.todo.model.Tarefa;

import java.util.List;
import java.util.stream.Collectors;

public class TarefasInacabadasException extends RuntimeException {

    private List<TarefaDtoDetalhado> tarefasNaoConcluidas;

    public TarefasInacabadasException(List<Tarefa> tarefasMeta) {
        tarefasNaoConcluidas = tarefasMeta
                .stream()
                .map(tarefa -> new TarefaDtoDetalhado(tarefa))
                .collect(Collectors.toList());

    }

    public List<TarefaDtoDetalhado> getTarefasNaoConcluidas() {
        return tarefasNaoConcluidas;
    }
}

