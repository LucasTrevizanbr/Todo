package br.com.todo.todo.service.meta;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.service.pontos.AplicarPontosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParalisacaoMetaService {

    @Autowired
    private AplicarPontosService aplicarPontosService;

    public void paralisar(Meta meta) {
        HistoricoDatas datas = meta.getHistoricoDatasMeta();
        datas.setDataInicioParalisacao(LocalDateTime.now());

        meta.setHistoricoDatasMeta(datas);
        meta.setStatus(Status.PARADA);
    }

    public void retomar(Meta meta){

        meta.getHistoricoDatasMeta().setDataRetornoDaParalisacao(LocalDateTime.now());

        aplicarPontosService.aplicarPenalidadeAposRetomada(meta);

        meta.setStatus(Status.RETOMADA);
    }
}
