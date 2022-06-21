package br.com.todo.dominio.modelos;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Embeddable
public class HistoricoDatas {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataFinalizacaoEstipulada;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataFinalizacaoReal;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataInicioParalisacao;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataRetornoDaParalisacao;

    public HistoricoDatas(LocalDateTime dataFinalizacaoEstipulada) {
        this.dataFinalizacaoEstipulada = dataFinalizacaoEstipulada;
    }

    public HistoricoDatas() {
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataFinalizacaoEstipulada() {
        return dataFinalizacaoEstipulada;
    }

    public void setDataFinalizacaoEstipulada(LocalDateTime dataFinalizacaoEstipulada) {
        this.dataFinalizacaoEstipulada = dataFinalizacaoEstipulada;
    }

    public LocalDateTime getDataFinalizacaoReal() {
        return dataFinalizacaoReal;
    }

    public void setDataFinalizacaoReal(LocalDateTime dataFinalizacaoReal) {
        this.dataFinalizacaoReal = dataFinalizacaoReal;
    }

    public LocalDateTime getDataInicioParalisacao() {
        return dataInicioParalisacao;
    }

    public void setDataInicioParalisacao(LocalDateTime dataInicioParalisacao) {
        this.dataInicioParalisacao = dataInicioParalisacao;
    }

    public LocalDateTime getDataRetornoDaParalisacao() {
        return dataRetornoDaParalisacao;
    }

    public void setDataRetornoDaParalisacao(LocalDateTime dataRetornoDaParalisacao) {
        this.dataRetornoDaParalisacao = dataRetornoDaParalisacao;
    }
}
