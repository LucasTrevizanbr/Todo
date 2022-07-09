package br.com.todo.domain.service.score.strategy;

import br.com.todo.domain.model.DatesHistory;

public interface ScoreDateStrategy {

    int getScoreBasedOnDate(DatesHistory datesHistory);

}
