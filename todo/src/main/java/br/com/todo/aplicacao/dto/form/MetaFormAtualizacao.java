package br.com.todo.aplicacao.dto.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MetaFormAtualizacao {

    @NotNull @Size(min = 10, max = 200)
    private String objetivo;

    public MetaFormAtualizacao(String objetivo) {
        this.objetivo = objetivo;
    }

    public MetaFormAtualizacao() {
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
}
