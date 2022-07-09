package br.com.todo.api.repository;

import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MetaRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GoalRepository metaRepository;

    private Goal meta;

    private User user;

    @BeforeEach
    public void setUp(){
        LocalDateTime dataFinal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        user = new User("Jorberto");
        user.setEmail("jorgeBlablu@hotmail.com");

        meta = new Goal("Aprender Kotlin", new DatesHistory(dataFinal),
                Status.ONGOING, user, Difficulty.MEDIUM);
        meta.addTask(new Task("Aprender paradigma Funcional"));
        meta.addTask(new Task("Aprender scope functions"));

        testEntityManager.persist(user);
        testEntityManager.persist(meta);
    }

    @Test
    @DisplayName("Deve retornar uma Meta pelo seu ID")
    public void deveRetornarMeta(){

        Optional<Goal> metaBuscada = metaRepository.findById(meta.getId());
        Assertions.assertThat(metaBuscada.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar a meta com os novos valores ")
    public void deveSalvarMeta(){
        meta.setObjective("Gerenciar Agência");

        Goal metaAtualizada = metaRepository.save(meta);

        Assertions.assertThat(metaAtualizada.getObjective()).isEqualTo("Gerenciar Agência");
    }
    
    @Test
    @DisplayName("Deve deletar uma meta pelo id")
    public void deveDeletarUmaMeta(){

        metaRepository.deleteById(meta.getId());
        Optional<Goal> metaAtualizada = metaRepository.findById(meta.getId());

        Assertions.assertThat(metaAtualizada.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar metas que estão no dia final do prazo e que não estão concluidas")
    public void deveBuscarMetasNoDiaFinal(){

        LocalDateTime dataFinal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        LocalDateTime inicioDia = dataFinal.toLocalDate().atStartOfDay();
        LocalDateTime fimDoDia = dataFinal.with(LocalTime.of(23,59,59));

        List<Goal> metasPrazoFinal = metaRepository.findGoalsInDeadLine(inicioDia,fimDoDia);

        Assertions.assertThat(metasPrazoFinal).hasSize(1).contains(meta);
    }
}
