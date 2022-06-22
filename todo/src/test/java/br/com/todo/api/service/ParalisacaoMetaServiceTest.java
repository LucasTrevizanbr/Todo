package br.com.todo.api.service;

import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Usuario;
import br.com.todo.dominio.modelos.Dificuldade;
import br.com.todo.dominio.modelos.HistoricoDatas;
import br.com.todo.dominio.modelos.Status;
import br.com.todo.dominio.servicos.meta.ParalisacaoMetaService;
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
public class ParalisacaoMetaServiceTest {

    @Autowired
    ParalisacaoMetaService paralisacaoMetaService;

    @Test
    @DisplayName("Deve paralisar a Meta, alterando seu Status para PARADA e salvando a Data")
    public void paralisarMetaTest(){
        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);

        paralisacaoMetaService.paralisar(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.PARADA);
        Assertions.assertThat(meta.getHistoricoDatasMeta().getDataInicioParalisacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve retomar a Meta, alterando seu Status para RETOMADA e salvando a Data")
    public void retomarMetaTest(){
        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);

        meta.setPontos(0);

        HistoricoDatas historicoData = meta.getHistoricoDatasMeta();
        historicoData.setDataInicioParalisacao(LocalDateTime.now());
        meta.setHistoricoDatasMeta(historicoData);

        paralisacaoMetaService.retomar(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.RETOMADA);
        Assertions.assertThat(meta.getHistoricoDatasMeta().getDataRetornoDaParalisacao()).isNotNull();
    }
}