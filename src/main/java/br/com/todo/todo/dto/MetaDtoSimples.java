package br.com.todo.todo.dto;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.Status;

import java.time.LocalDateTime;

public class MetaDtoSimples {

    private Long id;

    private String objetivo;

    private LocalDateTime prazoFinalEstipulado;

    private Status status;

    private Dificuldade dificuldade;

    public MetaDtoSimples(Meta meta) {
        this.id = meta.getId();
        this.objetivo = meta.getObjetivo();
        this.prazoFinalEstipulado = meta.getHistoricoDatasMeta().getDataFinalizacaoEstipulada();
        this.status = meta.getStatus();
        this.dificuldade = meta.getDificuldade();
    }

    public Long getId() {
        return id;
    }

    public String getObjetivo() {
        return objetivo;
    }


    public LocalDateTime getPrazoFinalEstipulado() {
        return prazoFinalEstipulado;
    }

    public Status getStatus() {
        return status;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }
}
