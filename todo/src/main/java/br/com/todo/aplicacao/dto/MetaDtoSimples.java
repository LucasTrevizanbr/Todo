package br.com.todo.aplicacao.dto;

import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Dificuldade;
import br.com.todo.dominio.modelos.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MetaDtoSimples {

    private Long id;

    private String objetivo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
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
