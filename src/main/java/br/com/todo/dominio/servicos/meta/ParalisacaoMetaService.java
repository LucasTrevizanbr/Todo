package br.com.todo.dominio.servicos.meta;

import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.servicos.pontos.AplicarPontosMetaService;
import br.com.todo.dominio.modelos.HistoricoDatas;
import br.com.todo.dominio.modelos.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParalisacaoMetaService {

    @Autowired
    private AplicarPontosMetaService aplicarPontosService;

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
