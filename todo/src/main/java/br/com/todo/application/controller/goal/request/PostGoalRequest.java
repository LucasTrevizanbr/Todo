package br.com.todo.application.controller.goal.request;

import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class PostGoalRequest {

    @NotNull @Size(min = 10, max = 200)
    private String objective;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime expectedFinalizationDate;

    @NotNull
    private Difficulty difficulty;

    private List<String> descriptionTasks;

    public PostGoalRequest() {
    }

    public Goal convertToGoalModel() {

        Goal goal = new Goal();
        goal.setObjective(this.objective);
        goal.setDateHistory(new DatesHistory(this.expectedFinalizationDate));
        goal.setDifficulty(this.difficulty);

        if(this.descriptionTasks != null) {
            for (String descTarefa : descriptionTasks) {
                goal.addTask(new PostTaskRequest(descTarefa).convertToTaskModel());
            }
        }

        return goal;
    }

    public String getObjective() {
        return objective;
    }

    public LocalDateTime getExpectedFinalizationDate() {
        return expectedFinalizationDate;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<String> getDescriptionTasks() {
        return descriptionTasks;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setExpectedFinalizationDate(LocalDateTime expectedFinalizationDate) {
        this.expectedFinalizationDate = expectedFinalizationDate;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDescriptionTasks(List<String> descriptionTasks) {
        this.descriptionTasks = descriptionTasks;
    }
}
