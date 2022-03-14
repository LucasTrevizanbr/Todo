package br.com.todo.todo.dto;

import br.com.todo.todo.model.Tarefa;

public class TarefaDtoDetalhado {

    private boolean concluida = false;

    private String descricao;

    public TarefaDtoDetalhado(Tarefa tarefa) {
        this.concluida = tarefa.isConcluida();
        this.descricao = tarefa.getDescricao();
    }

    public boolean isConcluida() {
        return concluida;
    }

    public String getDescricao() {
        return descricao;
    }
}
