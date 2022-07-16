package br.com.todo.application.controller.goal.request;

import br.com.todo.domain.model.Task;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostTaskRequest {

    @NotNull
    @Size(min = 10, max = 200)
    private String description;

    public PostTaskRequest() {
    }

    public PostTaskRequest(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Task convertToTaskModel() {
        Task tarefa = new Task();
        tarefa.setDescription(this.description);

        return tarefa;
    }
}
