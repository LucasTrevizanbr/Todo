package br.com.todo.aplicacao.dto.form;

import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Dificuldade;
import br.com.todo.dominio.modelos.HistoricoDatas;
import br.com.todo.dominio.modelos.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class MetaFormCadastro {

    @NotNull @Size(min = 10, max = 200)
    private String objetivo;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataFinalizacaoEstipulada;

    private Status status = Status.ANDAMENTO;

    @NotNull
    private Dificuldade dificuldade;

    private List<String> descricaoTarefasDaMeta ;

    @NotNull
    private Long idUsuario;

    private Integer pontos = 0;

    public MetaFormCadastro(String objetivo, LocalDateTime dataFinalEstipulada,
                Dificuldade dificuldade, List<String> descTarefasDaMeta, Long idUsuario) {
        this.objetivo = objetivo;
        this.dataFinalizacaoEstipulada = dataFinalEstipulada;
        this.dificuldade = dificuldade;
        this.descricaoTarefasDaMeta = descTarefasDaMeta;
        this.idUsuario = idUsuario;
    }

    public MetaFormCadastro() {
    }

    public Meta converterParaMetaEntidade(MetaFormCadastro metaForm) {

        Meta meta = new Meta();
        meta.setObjetivo(metaForm.getObjetivo());
        meta.setHistoricoDatasMeta(new HistoricoDatas(metaForm.dataFinalizacaoEstipulada));
        meta.setStatus(metaForm.getStatus());
        meta.setDificuldade(metaForm.getDificuldade());

        if(metaForm.getDescricaoTarefasDaMeta() != null){
            for (String descTarefa : metaForm.descricaoTarefasDaMeta){
                meta.adicionarTarefa(new TarefaFormCadastro(descTarefa).converterParaEntidade());
            }
        }

        meta.setPontos(metaForm.getPontos());

        return meta;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public Status getStatus() {
        return status;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public List<String> getDescricaoTarefasDaMeta() {
        return descricaoTarefasDaMeta;
    }

    public Integer getPontos() {
        return pontos;
    }
}
