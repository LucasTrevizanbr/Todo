package br.com.todo.dominio.modelos;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objetivo;

    @Embedded
    private HistoricoDatas historicoDatasMeta;

    private Integer pontos;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta")
    private List<Tarefa> tarefasDaMeta = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    public Meta(String objetivo, HistoricoDatas historicoDatasMeta, Status status,
                 Usuario usuario, Dificuldade dificuldade) {
        this.objetivo = objetivo;
        this.historicoDatasMeta = historicoDatasMeta;
        this.status = status;
        this.usuario = usuario;
        this.dificuldade = dificuldade;
    }

    public Meta(){}

    public void adicionarTarefa(Tarefa tarefa){
        tarefa.setMeta(this);
        this.tarefasDaMeta.add(tarefa);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public HistoricoDatas getHistoricoDatasMeta() {
        return historicoDatasMeta;
    }

    public void setHistoricoDatasMeta(HistoricoDatas historicoDatasMeta) {
        this.historicoDatasMeta = historicoDatasMeta;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public List<Tarefa> getTarefasDaMeta() {
        return tarefasDaMeta;
    }

    public void setTarefasDaMeta(List<Tarefa> tarefasDaMeta) {
        this.tarefasDaMeta = tarefasDaMeta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
