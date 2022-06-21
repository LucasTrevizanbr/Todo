package br.com.todo.api.service;

import br.com.todo.aplicacao.excecoes.TarefasInacabadasException;
import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Tarefa;
import br.com.todo.dominio.modelos.Usuario;
import br.com.todo.dominio.modelos.Dificuldade;
import br.com.todo.dominio.modelos.HistoricoDatas;
import br.com.todo.dominio.modelos.Status;
import br.com.todo.dominio.servicos.meta.ConclusaoMetaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ConclusaoMetaServiceTest {

    @Autowired
    ConclusaoMetaService conclusaoMetaService;

    @Test
    @DisplayName("Deve concluir com sucesso a meta")
    public void concluirMetaTest() throws TarefasInacabadasException {

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);
        meta.setPontos(2);

        conclusaoMetaService.concluirMeta(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.CONCLUIDA);
        Assertions.assertThat(meta.getHistoricoDatasMeta().getDataFinalizacaoReal()).isNotNull();

    }

    @Test
    @DisplayName("Deve lançar exceção com tarefas inacabadas da Meta")
    public void naoDeveConcluirMetaTest() {

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);
        meta.adicionarTarefa(new Tarefa("Aprender paradigma Funcional"));
        meta.adicionarTarefa(new Tarefa("Aprender scope functions"));

        try {
            conclusaoMetaService.validarTarefas(meta);
        } catch (TarefasInacabadasException e) {
            Assertions.assertThatExceptionOfType(TarefasInacabadasException.class);
            Assertions.assertThat( "Tarefas ainda não concluidas: [Aprender paradigma Funcional," +
                    " Aprender scope functions]".equals(e.getMessage()));
            Assertions.assertThat(meta.getHistoricoDatasMeta().getDataFinalizacaoReal()).isNull();
            Assertions.assertThat(meta.getStatus()).isNotEqualTo(Status.CONCLUIDA);
        }

    }



}
