package br.com.todo.api.service;

import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.enums.Status;
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
    ApplyScoreService aplicarPontosService;

    @Test
    @DisplayName("Deve aplicar penalidade de 0.5 pontos por cada dia que a Meta ficou paralisada, valor negativo")
    public void deveAplicarPenalidadeRetomadaPontoValorNegativo(){

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);

        LocalDateTime dataInicioParalisacao =
                LocalDateTime.of(2022, 3, 4, 13, 22, 00);
        LocalDateTime dataRetomada =
                LocalDateTime.of(2022, 3, 7, 13, 22, 00);
        meta.getDateHistory().setStopDate(dataInicioParalisacao);
        meta.getDateHistory().setRetakenDate(dataRetomada);
        meta.setPoints(-1);

        aplicarPontosService.aplicarPenalidadeAposRetomada(meta);

        Assertions.assertThat(meta.getPoints()).isEqualTo(-3);
    }

    @Test
    @DisplayName("Deve aplicar penalidade de 0.5 pontos por cada dia que a Meta ficou paralisada, valor positivo")
    public void deveAplicarPenalidadeRetomadaPontoValorPositivo(){

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);

        LocalDateTime dataInicioParalisacao =
                LocalDateTime.of(2022, 3, 4, 13, 22, 00);
        LocalDateTime dataRetomada =
                LocalDateTime.of(2022, 3, 7, 13, 22, 00);
        meta.getDateHistory().setStopDate(dataInicioParalisacao);
        meta.getDateHistory().setRetakenDate(dataRetomada);
        meta.setPoints(10);

        aplicarPontosService.aplicarPenalidadeAposRetomada(meta);

        Assertions.assertThat(meta.getPoints()).isEqualTo(8);
    }

    @Test
    @DisplayName("Deve conceder 1 ponto por cada dia de duração da meta, onde meta finalizada no prazo estipulado")
    public void deveAplicarPontosDeConclusao(){

        LocalDateTime dataFinalizacaoEstipulada =
                LocalDateTime.of(2022, 3, 28, 13, 22, 00);
        LocalDateTime dataCriacaoMeta =
                LocalDateTime.of(2022, 3, 1, 13, 22, 00);

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(dataFinalizacaoEstipulada),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);
        meta.getDateHistory().setCreationDate(dataCriacaoMeta);


        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 28, 16, 22, 00);

        meta.getDateHistory().setRealFinalizationDate(dataFinalizacaoEstipulada);
        meta.getDateHistory().setExpectedFinalizationDate(dataFinalizacaoReal);
        meta.setPoints(-2);

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

        Goal meta = new Goal("Aprender Kotlin",
                new DatesHistory(dataFinalizacaoEstipulada),
                Status.RETAKEN,
                new User("Jorberto"),
                Difficulty.HARD);
        meta.getDateHistory().setCreationDate(dataCriacaoMeta);

        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 31, 16, 22, 00);

        meta.getDateHistory().setRealFinalizationDate(dataFinalizacaoEstipulada);
        meta.setPoints(0);

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

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(dataFinalizacaoEstipulada),
                Status.ONGOING, new User("Jorberto"), Difficulty.EASY);
        meta.getDateHistory().setCreationDate(dataCriacaoMeta);


        LocalDateTime dataFinalizacaoReal =
                LocalDateTime.of(2022, 3, 31, 16, 22, 00);

        meta.getDateHistory().setRealFinalizationDate(dataFinalizacaoReal);
        meta.setPoints(0);

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
