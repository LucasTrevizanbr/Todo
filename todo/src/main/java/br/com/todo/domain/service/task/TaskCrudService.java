package br.com.todo.domain.service.task;

import br.com.todo.application.controller.goal.request.PostTaskRequest;
import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskCrudService {

    private final TaskRepository taskRepository;

    public TaskCrudService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task findById(Long taskId){

        Optional<Task> task = taskRepository.findById(taskId);

        if(task.isEmpty()){
            throw new NotFoundException(ApiError.TG001.getMessageError(),ApiError.TG001.name());
        }

        return task.get();
    }

    @Transactional
    public Goal completeTask(Goal goal, Long taskId) {

        Task task = this.findById(taskId);

        task.setCompleted(!task.isCompleted());

        taskRepository.save(task);

        return goal;
    }

    @Transactional
    public Goal updateTask(Goal goal, Long taskId, PostTaskRequest form){

        Task task = this.findById(taskId);

        task.setDescription(form.getDescription());

        taskRepository.save(task);

        return goal;
    }

    public Goal deleteTask(Goal goal, Long taskId){

        Task task = this.findById(taskId);

        goal.getTasks().remove(task);

        taskRepository.deleteById(taskId);

        return goal;
    }
}
