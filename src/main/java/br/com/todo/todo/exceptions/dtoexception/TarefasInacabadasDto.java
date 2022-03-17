package br.com.todo.todo.exceptions.dtoexception;

import br.com.todo.todo.dto.TarefaDtoDetalhado;

import java.util.List;

public class TarefasInacabadasDto {

    private List<TarefaDtoDetalhado> tarefasNaoConcluidas;

    public TarefasInacabadasDto(List<TarefaDtoDetalhado> tarefasNaoConcluidas) {
        this.tarefasNaoConcluidas = tarefasNaoConcluidas;
    }

    public List<TarefaDtoDetalhado> getTarefasNaoConcluidas() {
        return tarefasNaoConcluidas;
    }
}
