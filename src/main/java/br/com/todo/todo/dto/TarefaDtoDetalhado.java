package br.com.todo.todo.dto;

import br.com.todo.todo.model.Tarefa;

public class TarefaDtoDetalhado {

    private Long id;

    private boolean concluida;

    private String descricao;

    public TarefaDtoDetalhado(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.concluida = tarefa.isConcluida();
        this.descricao = tarefa.getDescricao();
    }

    public TarefaDtoDetalhado() {
    }

    public boolean isConcluida() {
        return concluida;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
