package br.com.todo.todo.api.service;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.service.pontos.AplicarPontosService;
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
public class AplicarPontosServiceTest {

    @Autowired
    AplicarPontosService aplicarPontosService;

    @Test
    @DisplayName("Deve aplicar penalidade de 0.5 pontos por cada dia que a Meta ficou paralisada, valor negativo")
    public void deveAplicarPenalidadeRetomadaPontoValorNegativo(){

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);

        LocalDateTime dataInicioParalisacao =
                LocalDateTime.of(2022, 3, 4, 13, 22, 00);
        LocalDateTime dataRetomada =
                LocalDateTime.of(2022, 3, 7, 13, 22, 00);
        meta.getHistoricoDatasMeta().setDataInicioParalisacao(dataInicioParalisacao);
        meta.getHistoricoDatasMeta().setDataRetornoDaParalisacao(dataRetomada);
        meta.setPontos(-1);

        aplicarPontosService.aplicarPenalidadeAposRetomada(meta);

        Assertions.assertThat(meta.getPontos()).isEqualTo(-3);
    }

    @Test
    @DisplayName("Deve aplicar penalidade de 0.5 pontos por cada dia que a Meta ficou paralisada, valor positivo")
    public void deveAplicarPenalidadeRetomadaPontoValorPositivo(){

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);

        LocalDateTime dataInicioParalisacao =
                LocalDateTime.of(2022, 3, 4, 13, 22, 00);
        LocalDateTime dataRetomada =
                LocalDateTime.of(2022, 3, 7, 13, 22, 00);
        meta.getHistoricoDatasMeta().setDataInicioParalisacao(dataInicioParalisacao);
        meta.getHistoricoDatasMeta().setDataRetornoDaParalisacao(dataRetomada);
        meta.setPontos(10);

        aplicarPontosService.aplicarPenalidadeAposRetomada(meta);

        Assertions.assertThat(meta.getPontos()).isEqualTo(8);
    }

    @Test
    @DisplayName("Deve conceder 1 ponto por cada dia de duração da meta, onde meta finalizada no prazo estipulado")
    public void deveAplicarPontosDeConclusao(){

        LocalDateTime dataFinalizacaoEstipulada =
                LocalDateTime.of(2022, 3, 28, 13, 22, 00);
        LocalDateTime dataCriacaoMeta =
                LocalDateTime.of(2022, 3, 1, 13, 22, 00);

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(dataFinalizacaoEstipulada),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);
        meta.getHistoricoDatasMeta().setDataCriacao(dataCriacaoMeta);


        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 28, 16, 22, 00);

        meta.getHistoricoDatasMeta().setDataFinalizacaoReal(dataFinalizacaoEstipulada);
        meta.getHistoricoDatasMeta().setDataFinalizacaoEstipulada(dataFinalizacaoReal);
        meta.setPontos(-2);

        int pontosBaseTarefaNoPrazo = 27;
        int pontosPorDificuldadeMedia = 4;
        int pontosNegativosPorDiaDeAtraso = 0;

        int pontosQueDeveraoSerConcedidos = pontosBaseTarefaNoPrazo + pontosPorDificuldadeMedia
                - pontosNegativosPorDiaDeAtraso;

        int pontosAplicaveis = aplicarPontosService.pontosPorConclusao(meta);

        Assertions.assertThat(pontosQueDeveraoSerConcedidos).isEqualTo(pontosAplicaveis);
    }

    @Test
    @DisplayName("Deve aplicar pontos de acordo com duração da meta, dificuldade e status")
    public void deveAplicarPontosDeConclusaoComPenalidadeDeAtraso(){

        LocalDateTime dataFinalizacaoEstipulada =
                LocalDateTime.of(2022, 3, 28, 13, 22, 00);
        LocalDateTime dataCriacaoMeta =
                LocalDateTime.of(2022, 3, 1, 13, 22, 00);

        Meta meta = new Meta("Aprender Kotlin",
                new HistoricoDatas(dataFinalizacaoEstipulada),
                Status.RETOMADA,
                new Usuario("Jorberto"),
                Dificuldade.DIFICIL);
        meta.getHistoricoDatasMeta().setDataCriacao(dataCriacaoMeta);

        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 31, 16, 22, 00);

        meta.getHistoricoDatasMeta().setDataFinalizacaoReal(dataFinalizacaoEstipulada);
        meta.setPontos(0);

        int pontosBaseTarefaNoPrazo = 27;
        int pontosPorStatusRetomada = 2;
        int pontosPorDificuldadeDificil = 6;
        int pontosNegativosPorDiaDeAtraso = 0;

        int pontosQueDeveraoSerConcedidos = pontosBaseTarefaNoPrazo + pontosPorStatusRetomada
                + pontosPorDificuldadeDificil - pontosNegativosPorDiaDeAtraso;

        int pontosAplicaveis = aplicarPontosService.pontosPorConclusao(meta);

        Assertions.assertThat(pontosAplicaveis).isEqualTo(pontosQueDeveraoSerConcedidos);
    }

    @Test
    @DisplayName("Deve aplicar pontos de acordo com duração da meta, dificuldade e status")
    public void deveAplicarPontosDeConclusaoComPenalidadeDeAtrasoMaisBonusPorRetomar(){

        LocalDateTime dataFinalizacaoEstipulada =
                LocalDateTime.of(2022, 3, 26, 13, 22, 00);
        LocalDateTime dataCriacaoMeta =
                LocalDateTime.of(2022, 3, 1, 13, 22, 00);

        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(dataFinalizacaoEstipulada),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.FACIL);
        meta.getHistoricoDatasMeta().setDataCriacao(dataCriacaoMeta);


        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 31, 16, 22, 00);

        meta.getHistoricoDatasMeta().setDataFinalizacaoReal(dataFinalizacaoReal);
        meta.setPontos(0);

        int pontosBaseTarefaNoPrazo = 25;
        int pontosPorStatusRetomada = 0;
        int pontosPorDificuldadeFacil = 2;
        int pontosNegativosPorDiaDeAtraso = 3;

        int pontosQueDeveraoSerConcedidos = pontosBaseTarefaNoPrazo + pontosPorStatusRetomada
                + pontosPorDificuldadeFacil - pontosNegativosPorDiaDeAtraso;

        int pontosAplicaveis = aplicarPontosService.pontosPorConclusao(meta);

        Assertions.assertThat(pontosAplicaveis).isEqualTo(pontosQueDeveraoSerConcedidos);
    }
}
