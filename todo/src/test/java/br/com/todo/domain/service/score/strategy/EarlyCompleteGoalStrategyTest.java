package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class EarlyCompleteGoalStrategyTest {

    @MockBean
    private ScoreDateStrategy earlyCompleteGoalStrategy;

    private DatesHistory datesHistory;
    private double pointsMultiplier;
    private double pointsMultiplierWithBonus;

    @BeforeEach
    public void setUpHistoryDateAndPointsMultiplier(){

        LocalDate goalCreationDate = LocalDate.of(2022,1,13);
        LocalDate goalDeadLineDate = LocalDate.of(2022,1,23);
        LocalDate realCompleteDate = LocalDate.of(2022,1,20);

        LocalDateTime expectedFinalizationDate = goalDeadLineDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime realFinalizationDate = realCompleteDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime creationDate = goalCreationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        pointsMultiplier = ScoreValues.POINTS_PER_DAY.getValue();
        pointsMultiplierWithBonus = ScoreValues.POINTS_PER_DAY_BONUS.getValue();
        datesHistory = new DatesHistory();
        earlyCompleteGoalStrategy = new EarlyCompleteGoalStrategy();

        datesHistory.setExpectedFinalizationDate(expectedFinalizationDate);
        datesHistory.setRealFinalizationDate(realFinalizationDate);
        datesHistory.setCreationDate(creationDate);
    }


    @Test
    @DisplayName("Each day before the deadline gives 1.5 points of bonus. 3 days early complete should give +5 points")
    public void givePointsInDeadlineNotMet(){

        int pointsGiven = earlyCompleteGoalStrategy.getScoreBasedOnDate(datesHistory);

        assertThat(pointsGiven).isEqualTo(15);
    }

}