package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

import java.time.LocalDateTime;

import static br.com.todo.infraestructure.util.ScoreCalculationUtils.getPointsPerDay;

public class LateCompleteGoalStrategy implements ScoreDateStrategy {

    @Override
    public int getScoreBasedOnDate(DatesHistory datesHistory) {
        int expectedPoints = expectedPoints(datesHistory);
        int discountPoints = penaltyPoints(datesHistory);

        return expectedPoints - discountPoints;
    }

    private int expectedPoints(DatesHistory datesHistory) {
        int expectedPoints = 0;
        LocalDateTime create = datesHistory.getCreationDate();
        LocalDateTime expectedFinish = datesHistory.getExpectedFinalizationDate();
        expectedPoints = getPointsPerDay(create, expectedFinish, ScoreValues.POINTS_PER_DAY.getValue());

        return expectedPoints;
    }

    private int penaltyPoints(DatesHistory datesHistory) {
        int discountPoints = 0;
        LocalDateTime expectedFinish = datesHistory.getExpectedFinalizationDate();
        LocalDateTime realFinish = datesHistory.getRealFinalizationDate();
        discountPoints = getPointsPerDay(expectedFinish, realFinish, ScoreValues.POINTS_PER_DAY_PENALTY.getValue());
        return discountPoints;
    }

}
