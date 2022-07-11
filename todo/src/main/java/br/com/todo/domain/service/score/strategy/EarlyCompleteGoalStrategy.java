package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

import java.time.LocalDateTime;

import static br.com.todo.infraestructure.util.ScoreCalculationUtils.getPointsPerDay;

public class EarlyCompleteGoalStrategy implements ScoreDateStrategy{

    @Override
    public int getScoreBasedOnDate(DatesHistory datesHistory) {
        int expectedPoints = expectedPoints(datesHistory);
        int extraPoints = extraPoints(datesHistory);

        return expectedPoints + extraPoints;
    }

    private int expectedPoints(DatesHistory datesHistory) {
        int expectedPoints = 0;
        LocalDateTime create = datesHistory.getCreationDate();
        LocalDateTime expectedFinish = datesHistory.getExpectedFinalizationDate();
        expectedPoints = getPointsPerDay(create, expectedFinish, ScoreValues.POINTS_PER_DAY.getValue());

        return expectedPoints;
    }

    private int extraPoints(DatesHistory datesHistory) {
        int bonusPoints = 0;
        LocalDateTime realFinish = datesHistory.getRealFinalizationDate();
        LocalDateTime expectedFinish = datesHistory.getExpectedFinalizationDate();
        bonusPoints = getPointsPerDay(realFinish, expectedFinish, ScoreValues.POINTS_PER_DAY_BONUS.getValue());

        return bonusPoints;
    }
}
