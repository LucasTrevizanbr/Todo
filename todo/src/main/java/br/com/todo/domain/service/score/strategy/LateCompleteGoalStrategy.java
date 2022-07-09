package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

import java.time.LocalDate;

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
        LocalDate create = datesHistory.getCreationDate().toLocalDate();
        LocalDate expectedFinish = datesHistory.getExpectedFinalizationDate().toLocalDate();
        expectedPoints = getPointsPerDay(create, expectedFinish, ScoreValues.PER_DAY.getValue());

        return expectedPoints;
    }

    private int penaltyPoints(DatesHistory datesHistory) {
        int discountPoints = 0;
        LocalDate create = datesHistory.getCreationDate().toLocalDate();
        LocalDate realFinish = datesHistory.getRealFinalizationDate().toLocalDate();
        discountPoints = getPointsPerDay(create, realFinish, ScoreValues.PER_DAY_PENALTY.getValue());
        return discountPoints;
    }

}
