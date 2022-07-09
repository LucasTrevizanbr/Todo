package br.com.todo.application.controller.goal.response;

import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class DetailedGoalResponse {

    private Long id;

    private String objective;

    private DatesHistory datesHistory;

    private Integer points;

    private Status status;

    private Difficulty difficulty;

    @JsonIgnoreProperties("goal")
    private List<Task> tasks;

    private String ownerName;

    public DetailedGoalResponse(Goal goal) {
        this.id = goal.getId();
        this.objective = goal.getObjective();
        this.datesHistory = goal.getDateHistory();
        this.points = goal.getPoints();
        this.status = goal.getStatus();
        this.difficulty = goal.getDifficulty();
        this.tasks = goal.getTasks();
        this.ownerName = goal.getUser().getName();
    }

    public DetailedGoalResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getObjective() {
        return objective;
    }

    public DatesHistory getDatesHistory() {
        return datesHistory;
    }

    public Integer getPoints() {
        return points;
    }

    public Status getStatus() {
        return status;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
