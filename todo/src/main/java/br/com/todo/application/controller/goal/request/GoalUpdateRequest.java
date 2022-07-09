package br.com.todo.application.controller.goal.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GoalUpdateRequest {

    @NotNull @Size(min = 10, max = 200)
    private String objective;

    public String getObjective() {
        return objective;
    }

}
