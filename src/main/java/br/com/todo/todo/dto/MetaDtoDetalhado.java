package br.com.todo.todo.dto;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaDtoDetalhado {

    private Long id;

    private String objetivo;

    private HistoricoDatas historicoDatas;

    private Integer pontos;

    private Status status;

    private Dificuldade dificuldade;

    private List<TarefaDtoDetalhado> tarefasDaMeta = new ArrayList<>();

    private Usuario usuario;

    public MetaDtoDetalhado(Meta meta) {
        this.id = meta.getId();
        this.objetivo = meta.getObjetivo();
        this.historicoDatas = meta.getHistoricoDatasMeta();
        this.pontos = meta.getPontos();
        this.status = meta.getStatus();
        this.dificuldade = meta.getDificuldade();
        this.tarefasDaMeta = meta.getTarefasDaMeta()
                .stream()
                .map(tarefa -> new TarefaDtoDetalhado(tarefa))
                .collect(Collectors.toList());
        this.usuario = meta.getUsuario();
    }

    public MetaDtoDetalhado() {
    }

    public Long getId() {
        return id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public HistoricoDatas getHistoricoDatas() {
        return historicoDatas;
    }

    public Integer getPontos() {
        return pontos;
    }

    public Status getStatus() {
        return status;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public List<TarefaDtoDetalhado> getTarefasDaMeta() {
        return tarefasDaMeta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setTarefasDaMeta(List<TarefaDtoDetalhado> tarefasDaMeta) {
        this.tarefasDaMeta = tarefasDaMeta;
    }
}
