package br.com.todo.todo.service;

import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.complemento.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConclusaoMetaService {

    public void validarTarefas(Meta meta) throws TarefasInacabadasException {

        if(!meta.getTarefasDaMeta().isEmpty()){
            List<Tarefa> tarefasMeta = meta.getTarefasDaMeta();

            if(tarefasMeta.stream().anyMatch(tarefa -> !tarefa.isConcluida())){
                throw new TarefasInacabadasException(tarefasMeta);
            };
        }else{
            meta.setStatus(Status.CONCLUIDA);
            meta.getHistoricoDatasMeta().setDataFinalizacaoReal(LocalDateTime.now());
        }
    }
}
