package br.com.todo.aplicacao.excecoes.dtoexception;

import br.com.todo.aplicacao.dto.TarefaDtoDetalhado;

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
