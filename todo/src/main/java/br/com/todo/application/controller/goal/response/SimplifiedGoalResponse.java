package br.com.todo.application.controller.goal.response;

import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SimplifiedGoalResponse {

    private final Long id;

    private final String objective;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private final LocalDateTime expectedFinalizationDate;

    private final Status status;

    private final Difficulty difficulty;

    public SimplifiedGoalResponse(Goal meta) {
        this.id = meta.getId();
        this.objective = meta.getObjective();
        this.expectedFinalizationDate = meta.getDateHistory().getExpectedFinalizationDate();
        this.status = meta.getStatus();
        this.difficulty = meta.getDifficulty();
    }

    public Long getId() {
        return id;
    }

    public String getObjective() {
        return objective;
    }

    public LocalDateTime getExpectedFinalizationDate() {
        return expectedFinalizationDate;
    }

    public Status getStatus() {
        return status;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
