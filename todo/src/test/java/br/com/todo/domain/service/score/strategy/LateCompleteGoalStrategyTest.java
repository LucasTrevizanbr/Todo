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
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class LateCompleteGoalStrategyTest {

    @Mock
    private ScoreDateStrategy lateCompleteGoalStrategy;

    private DatesHistory datesHistory;
    private double pointsMultiplier;
    private double pointsMultiplierWithPenalty;

    @BeforeEach
    public void setUpHistoryDateAndPointsMultiplier(){

        LocalDate goalCreationDate = LocalDate.of(2022,1,13);
        LocalDate goalDeadLineDate = LocalDate.of(2022,1,23);
        LocalDate realCompleteDate = LocalDate.of(2022,1,26);

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
        pointsMultiplierWithPenalty = ScoreValues.POINTS_PER_DAY_PENALTY.getValue();
        datesHistory = new DatesHistory();
        lateCompleteGoalStrategy = new LateCompleteGoalStrategy();

        datesHistory.setExpectedFinalizationDate(expectedFinalizationDate);
        datesHistory.setRealFinalizationDate(realFinalizationDate);
        datesHistory.setCreationDate(creationDate);
    }


    @Test
    @DisplayName("Each day after the deadline gives 0.5 points of penalty. 3 days of delays should give -3 points")
    public void givePointsInDeadlineNotMet(){

        int pointsGiven = lateCompleteGoalStrategy.getScoreBasedOnDate(datesHistory);

        assertThat(pointsGiven).isEqualTo(8);
    }

}