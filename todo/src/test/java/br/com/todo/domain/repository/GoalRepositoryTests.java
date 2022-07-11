package br.com.todo.domain.repository;

import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GoalRepositoryTests {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    private Goal goal;

    private User user;

    @BeforeEach
    public void setUp(){

        goalRepository.deleteAll();
        userRepository.deleteAll();

        LocalDateTime dataFinal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        user = new User();
        user.setEmail("jorgeBlablu@hotmail.com");

        goal = new Goal("Aprender Kotlin", new DatesHistory(dataFinal),
                Status.ONGOING, user, Difficulty.MEDIUM);
        goal.addTask(new Task("Aprender paradigma Funcional"));
        goal.addTask(new Task("Aprender scope functions"));

        userRepository.save(user);
        goalRepository.save(goal);
    }

    @AfterEach
    public void tearDow(){
        goalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return goals in deadline that are not finished")
    public void deveBuscarMetasNoDiaFinal(){

        LocalDateTime deadlineGoal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        LocalDateTime startOfDay = deadlineGoal.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = deadlineGoal.with(LocalTime.of(23,59,59));

        List<Goal> goalsInDeadline = goalRepository.findGoalsInDeadLine(startOfDay,endOfDay);

        Assertions.assertThat(goalsInDeadline).hasSize(1);
    }

}
