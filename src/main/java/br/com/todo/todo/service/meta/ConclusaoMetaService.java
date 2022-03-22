package br.com.todo.todo.service.meta;

import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.service.pontos.AplicarPontosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConclusaoMetaService {

    @Autowired
    private AplicarPontosService aplicarPontosService;

    public boolean validarTarefas(Meta meta) throws TarefasInacabadasException {

        if(!meta.getTarefasDaMeta().isEmpty()){
            List<Tarefa> tarefasMeta = meta.getTarefasDaMeta();

            if(tarefasMeta.stream().anyMatch(tarefa -> !tarefa.isConcluida())){
                return false;
            }
        }
        return true;
    }

    public boolean validarStatus(Meta meta) {
        if(meta.getStatus() == Status.PARADA) {
            return false;
        }

        return true;
    }

    public void concluirMeta(Meta metaPresente) {
        metaPresente.setStatus(Status.CONCLUIDA);
        metaPresente.getHistoricoDatasMeta().setDataFinalizacaoReal(LocalDateTime.now());
        int pontosAplicaveis = metaPresente.getPontos() + aplicarPontosService.pontosPorConclusao(metaPresente);
        metaPresente.setPontos(pontosAplicaveis);
    }


}
