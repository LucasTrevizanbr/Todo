package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

import java.time.LocalDateTime;

import static br.com.todo.infraestructure.util.ScoreCalculationUtils.getPointsPerDay;

public class CompleteGoalOnTimeStrategy implements ScoreDateStrategy {

    @Override
    public int getScoreBasedOnDate(DatesHistory datesHistory) {
        int points = 0;
        LocalDateTime create = datesHistory.getCreationDate();
        LocalDateTime complete = datesHistory.getRealFinalizationDate();
        points = getPointsPerDay(create, complete, ScoreValues.PER_DAY.getValue());
        return points;
    }
}
