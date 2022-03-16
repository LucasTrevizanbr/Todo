package br.com.todo.todo.service;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParalisacaoMetaService {

    public void paralisar(Meta meta) {
        HistoricoDatas datas = meta.getHistoricoDatasMeta();
        datas.setDataInicioParalisacao(LocalDateTime.now());

        meta.setHistoricoDatasMeta(datas);
        meta.setStatus(Status.PARADA);
    }

    public void retomar(Meta meta){

        HistoricoDatas datas = meta.getHistoricoDatasMeta();
        datas.setDataRetornoDaParalisacao(LocalDateTime.now());

        meta.setHistoricoDatasMeta(datas);
        meta.setStatus(Status.RETOMADA);

    }
}
