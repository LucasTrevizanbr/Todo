package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

import java.time.LocalDate;

import static br.com.todo.infraestructure.util.ScoreCalculationUtils.getPointsPerDay;

public class CompleteGoalOnTimeStrategy implements ScoreDateStrategy {

    @Override
    public int getScoreBasedOnDate(DatesHistory datesHistory) {
        int points = 0;
        LocalDate create = datesHistory.getCreationDate().toLocalDate();
        LocalDate complete = datesHistory.getRealFinalizationDate().toLocalDate();
        points = getPointsPerDay(create, complete, ScoreValues.PER_DAY.getValue());
        return points;
    }
}
