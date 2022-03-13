package br.com.todo.todo.form;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class MetaFormCadastro {

    @NotNull @Size(min = 10, max = 100)
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

    public MetaFormCadastro(String objetivo, LocalDateTime dataFinalEstipulada,
                Dificuldade dificuldade, List<String> descTarefasDaMeta, Long idUsuaruio) {
        this.objetivo = objetivo;
        this.dataFinalizacaoEstipulada = dataFinalEstipulada;
        this.dificuldade = dificuldade;
        this.descricaoTarefasDaMeta = descTarefasDaMeta;
        this.idUsuario = idUsuaruio;
    }

    public MetaFormCadastro() {
    }

    public  Meta converterParaMetaEntidade(MetaFormCadastro metaForm) {

        Meta meta = new Meta();
        meta.setObjetivo(metaForm.getObjetivo());
        meta.setHistoricoDatasMeta(new HistoricoDatas(metaForm.dataFinalizacaoEstipulada));
        meta.setStatus(metaForm.getStatus());
        meta.setDificuldade(metaForm.getDificuldade());

        if(metaForm.descricaoTarefasDaMeta != null){
            for (String descTarefa : metaForm.descricaoTarefasDaMeta){
                meta.adicionarTarefa(new TarefaFormCadastro(descTarefa).converterParaEntidade());
            }
        }

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

}
