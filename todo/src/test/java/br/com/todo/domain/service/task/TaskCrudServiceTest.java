package br.com.todo.domain.service.task;

import br.com.todo.application.controller.goal.request.PostTaskRequest;
import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class TaskCrudServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskCrudService taskCrudService;

    @Test
    @DisplayName("Should return a task with success")
    public void findByIdSuccess(){
        Optional<Task> task = Optional.of(new Task());

        when(taskRepository.findById(anyLong())).thenReturn(task);

        Task existentTask = taskCrudService.findById(1L);

        assertThat(existentTask).isEqualTo(task.get());
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should set a task completed atribute as true if his value is false")
    public void completeTaskTest(){
        Task task = new Task();
        task.setCompleted(false);
        Goal goal = new Goal();
        goal.addTask(task);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Goal completedTask = taskCrudService.completeTask(goal,1L);

        assertThat(completedTask.getTasks().get(0)).isNotNull();
        assertThat(completedTask.getTasks().get(0).isCompleted()).isTrue();
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
        verify(taskRepository, Mockito.times(1)).save(task);

    }

    @Test
    @DisplayName("Should set a task completed atribute as false if his value is true")
    public void incompleteTaskTest(){
        Task task = new Task();
        task.setCompleted(true);
        Goal goal = new Goal();
        goal.addTask(task);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Goal completedTask = taskCrudService.completeTask(goal,1L);

        assertThat(completedTask.getTasks().get(0)).isNotNull();
        assertThat(completedTask.getTasks().get(0).isCompleted()).isFalse();
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
        verify(taskRepository, Mockito.times(1)).save(task);

    }

    @Test
    @DisplayName("Should update a task description")
    public void updateTaskTest(){
        Task task = new Task();
        task.setCompleted(true);
        task.setDescription("Before the update");
        Goal goal = new Goal();
        goal.addTask(task);

        PostTaskRequest postTaskRequest = new PostTaskRequest("After the update");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Goal completedTask = taskCrudService.updateTask(goal,1L, postTaskRequest );

        assertThat(completedTask.getTasks()).hasSize(1);
        assertThat(completedTask.getTasks().get(0).getDescription()).isEqualTo("After the update");
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
        verify(taskRepository, Mockito.times(1)).save(task);

    }

    @Test
    @DisplayName("Should delete a task")
    public void deleteTaskTest(){
        Task task = new Task();
        task.setCompleted(true);
        Goal goal = new Goal();
        goal.addTask(task);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Goal completedTask = taskCrudService.deleteTask(goal,1L);

        assertThat(completedTask.getTasks()).isEmpty();
        assertThat(completedTask.getTasks()).hasSize(0);
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
        verify(taskRepository, Mockito.times(1)).deleteById(anyLong());

    }

    @Test
    @DisplayName("Should return a not found exception with code and message TG001 of the ApiError enum")
    public void findByIdError(){
        Optional<Task> task = Optional.empty();

        when(taskRepository.findById(anyLong())).thenReturn(task);

        assertThrows(NotFoundException.class, () -> taskCrudService.findById(anyLong()),
                ApiError.TG001.getMessageError());
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
    }

}