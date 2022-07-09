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
public class ParalisacaoMetaServiceTest {

    @Autowired
    ParalisacaoMetaService paralisacaoMetaService;

    @Test
    @DisplayName("Deve paralisar a Meta, alterando seu Status para PARADA e salvando a Data")
    public void paralisarMetaTest(){
        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);

        paralisacaoMetaService.paralisar(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.STOPPED);
        Assertions.assertThat(meta.getDateHistory().getStopDate()).isNotNull();
    }

    @Test
    @DisplayName("Deve retomar a Meta, alterando seu Status para RETOMADA e salvando a Data")
    public void retomarMetaTest(){
        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);

        meta.setPoints(0);

        DatesHistory historicoData = meta.getDateHistory();
        historicoData.setStopDate(LocalDateTime.now());
        meta.setDateHistory(historicoData);

        paralisacaoMetaService.retomar(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.RETAKEN);
        Assertions.assertThat(meta.getDateHistory().getRetakenDate()).isNotNull();
    }
}
