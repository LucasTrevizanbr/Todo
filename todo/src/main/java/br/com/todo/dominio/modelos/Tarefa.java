package br.com.todo.dominio.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean concluida = false;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tarefasDaMeta")
    private Meta meta;

    public Tarefa(String descricao) {
        this.descricao = descricao;;
    }

    public Tarefa() {
    }

    @PreRemove
    public void desassociarMeta(){
        this.setMeta(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
