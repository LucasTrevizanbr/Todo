package br.com.todo.todo.service;

import br.com.todo.todo.model.Meta;
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

    public void aplicarPontosPorConclusao(Meta meta) {

        HistoricoDatas datas = meta.getHistoricoDatasMeta();
        LocalDateTime dataCriacaoMeta = datas.getDataCriacao();
        LocalDateTime dataFinalEstipulada = datas.getDataFinalizacaoEstipulada();
        LocalDateTime dataFinalizacaoReal = datas.getDataFinalizacaoReal();

        if(dataCriacaoMeta.toLocalDate().equals(dataFinalEstipulada.toLocalDate())){
            int diasPassadosAteConclusao = DataUtils.diasEntreDatas(dataCriacaoMeta, dataFinalEstipulada);
            int pontosAtuais = meta.getPontos();
            meta.setPontos(pontosAtuais + diasPassadosAteConclusao);
        }else{
            int pontosBase = DataUtils.diasEntreDatas(dataCriacaoMeta, dataFinalEstipulada);
            int diasPassadosAteConclusao = DataUtils.diasEntreDatas(dataFinalEstipulada, dataFinalizacaoReal);

            double penalidade = 0 ;

            for(int i= 0; i < diasPassadosAteConclusao; i++){
                penalidade += 0.5;
            }

            int pontosReduzidos = (int) Math.round(penalidade);
            int pontosAtuais = meta.getPontos();
            int pontoPorRetomada = 0;

            if(meta.getStatus().equals(Status.RETOMADA)){
                pontoPorRetomada = 2;
            }

            meta.setPontos(pontosAtuais + pontosBase - pontosReduzidos + pontoPorRetomada);
        }

    }
}
