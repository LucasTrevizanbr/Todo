package br.com.todo.todo.service;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.utils.DataUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AplicarPontosService {

    public void aplicarPenalidadeAposRetomada(Meta meta) {
        HistoricoDatas datas = meta.getHistoricoDatasMeta();
        LocalDateTime dataInicioParalisacao = datas.getDataInicioParalisacao();
        LocalDateTime dataRetomada = datas.getDataRetornoDaParalisacao();
        int diasParalisada = DataUtils.diasEntreDatas(dataInicioParalisacao, dataRetomada);

        double penalidade = 0;

        for (int i = 0 ; i < diasParalisada; i++){
            penalidade += 0.5;
        }

        int pontosDescontados =(int) Math.round(penalidade);

        Integer pontosAtuais = meta.getPontos();
        meta.setPontos(pontosAtuais - pontosDescontados);
    }

    public int pontosPorConclusao(Meta meta) {

        HistoricoDatas datas = meta.getHistoricoDatasMeta();

        int pontosBaseadoEmData = calcularPontosBaseadoEmData(datas);
        int pontosBaseadosEmStatus = calcularPontosBaseadoEmStatus(meta.getStatus());
        int pontoBaseadoEmDificuldade = calcularPontosBaseadoEmDificuldade (meta.getDificuldade());

        return pontosBaseadoEmData + pontosBaseadosEmStatus + pontoBaseadoEmDificuldade;
    }


    private int calcularPontosBaseadoEmDificuldade(Dificuldade dificuldade) {
        int pontos = 0;
        switch (dificuldade){
            case FACIL: pontos = 2;
                break;
            case MEDIO: pontos = 4;
                break;
            case DIFICIL: pontos = 6;
            break;
        }
        return pontos;
    }

    private int calcularPontosBaseadoEmStatus(Status status) {
        int pontos = 0;
        switch (status){
            case RETOMADA : pontos = 2;
            break;
            case CONCLUIDA: pontos = 4;
            break;
        }
         return pontos;
    }

    private int calcularPontosBaseadoEmData(HistoricoDatas datas) {
        LocalDateTime dataCriacaoMeta = datas.getDataCriacao();
        LocalDateTime dataFinalEstipulada = datas.getDataFinalizacaoEstipulada();
        LocalDateTime dataFinalizacaoReal = datas.getDataFinalizacaoReal();

        if(dataCriacaoMeta.toLocalDate().equals(dataFinalEstipulada.toLocalDate())){
            int pontosPorDia = DataUtils.diasEntreDatas(dataCriacaoMeta, dataFinalEstipulada);
            return pontosPorDia;
        }else{
            int pontosBase = DataUtils.diasEntreDatas(dataCriacaoMeta, dataFinalEstipulada);
            int diasPassadosAteConclusao = DataUtils.diasEntreDatas(dataFinalEstipulada, dataFinalizacaoReal);

            double penalidade = 0 ;

            for(int i= 0; i < diasPassadosAteConclusao; i++){
                penalidade += 0.5;
            }

            int pontosReduzidos = (int) Math.round(penalidade);
            return pontosBase - pontosReduzidos;
        }
    }
}
