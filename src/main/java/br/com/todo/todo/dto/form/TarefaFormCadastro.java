package br.com.todo.todo.dto.form;

import br.com.todo.todo.model.Tarefa;

public class TarefaFormCadastro {

    private boolean concluida = false;
    private String descricao;

    public TarefaFormCadastro(String descricao){
        this.descricao = descricao;
    }

    public TarefaFormCadastro() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Tarefa converterParaEntidade() {
        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(this.descricao);

        return tarefa;
    }
}
