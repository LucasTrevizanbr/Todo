package br.com.todo.domain.service.goal;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.repository.GoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class GoalCrudServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalCrudService goalCrudService;

    @Test
    @DisplayName("Should return a goal with success")
    public void findByIdSuccess(){
        Optional<Goal> goal = Optional.of(new Goal());

        when(goalRepository.findById(anyLong())).thenReturn(goal);
        Goal existentGoal = goalCrudService.findById(anyLong());

        assertThat(existentGoal.equals(goal.get()));
        verify(goalRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should return a not found exception with code and message TG001 of the ApiError enum")
    public void findByIdError(){
        Optional<Goal> goal = Optional.empty();

        when(goalRepository.findById(anyLong())).thenReturn(goal);

        assertThrows(NotFoundException.class, () -> goalCrudService.findById(anyLong()),
                ApiError.TG001.getMessageError());
        verify(goalRepository, Mockito.times(1)).findById(anyLong());
    }


}