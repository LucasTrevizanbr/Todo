package br.com.todo.domain.service.task;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.repository.TaskRepository;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @DisplayName("Should return a not found exception with code and message TG001 of the ApiError enum")
    public void findByIdError(){
        Optional<Task> task = Optional.empty();

        when(taskRepository.findById(anyLong())).thenReturn(task);

        assertThrows(NotFoundException.class, () -> taskCrudService.findById(anyLong()),
                ApiError.TG001.getMessageError());
        verify(taskRepository, Mockito.times(1)).findById(anyLong());
    }

}