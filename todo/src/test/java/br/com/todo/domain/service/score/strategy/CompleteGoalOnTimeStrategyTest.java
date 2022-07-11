package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class CompleteGoalOnTimeStrategyTest {

    @Mock
    private ScoreDateStrategy completeGoalOnTimeStrategy;

    private DatesHistory datesHistory;
    private double pointsMultiplier;

    @BeforeEach
    public void setUpHistoryDateAndPointsMultiplier(){

        LocalDate goalCreationDate = LocalDate.of(2022,1,13);
        LocalDate goalDeadLineDate = LocalDate.of(2022,1,23);

        LocalDateTime expectedFinalizationDate = LocalDateTime.of(goalDeadLineDate, LocalTime.now());
        LocalDateTime realFinalizationDate = LocalDateTime.of(goalDeadLineDate, LocalTime.now());
        LocalDateTime creationDate = LocalDateTime.of(goalCreationDate, LocalTime.now());

        pointsMultiplier = ScoreValues.POINTS_PER_DAY.getValue();
        datesHistory = new DatesHistory();
        completeGoalOnTimeStrategy = new CompleteGoalOnTimeStrategy();

        datesHistory.setExpectedFinalizationDate(expectedFinalizationDate);
        datesHistory.setRealFinalizationDate(realFinalizationDate);
        datesHistory.setCreationDate(creationDate);
    }

    @Test
    @DisplayName("Each day gives 1 point. 10 days of goal should give 10 points")
    public void givePointsInDeadlineMet(){

        int pointsGiven = completeGoalOnTimeStrategy.getScoreBasedOnDate(datesHistory);

        assertThat(pointsGiven).isEqualTo(10);
    }

}