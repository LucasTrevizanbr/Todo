package br.com.todo.domain.service.goal;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.GoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class GoalCrudServiceTest {

    private final static LocalDate RETAKEN_DATE_3_DAYS_STOPPED = LocalDate.of(2022, 1, 13);

    private final Clock fixedClockRetakenDate = Clock
            .fixed(
                    RETAKEN_DATE_3_DAYS_STOPPED
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(), ZoneId.systemDefault()
            );

    @MockBean
    private GoalRepository goalRepository;

    @MockBean
    private Clock clock;

    @Autowired
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

    @Test
    @DisplayName("Should change the goal status to STOPPED and record a stop date")
    public void stopGoalTest(){
        Goal goal = new Goal();
        goal.setStatus(Status.ONGOING);
        goal.setDateHistory(new DatesHistory());

        when(goalRepository.save(goal)).thenReturn(goal);

        Goal stoppedGoal = goalCrudService.stopGoal(goal);

        assertThat(stoppedGoal.getStatus()).isEqualTo(Status.STOPPED);
        assertThat(stoppedGoal.getDateHistory().getStopDate()).isNotNull();
        verify(goalRepository, Mockito.times(1)).save(goal);
    }

    @Test
    @DisplayName("Should change the goal status to RETAKEN, record a retaken date and apply penalty points")
    public void resumeGoalTest(){
        Goal goal = new Goal();
        goal.setStatus(Status.RETAKEN);
        goal.setDateHistory(buildDatesHistoryWithStopDate());

        when(clock.instant()).thenReturn(fixedClockRetakenDate.instant());
        when(clock.getZone()).thenReturn(fixedClockRetakenDate.getZone());
        when(goalRepository.save(goal)).thenReturn(goal);

        Goal resumedGoal = goalCrudService.resumeGoal(goal);

        assertThat(resumedGoal.getStatus()).isEqualTo(Status.RETAKEN);
        assertThat(resumedGoal.getDateHistory().getRetakenDate()).isNotNull();
        assertThat(resumedGoal.getPoints()).isEqualTo(-2);
        verify(goalRepository, Mockito.times(1)).save(goal);
    }

    @Test
    @DisplayName("Should create a task and link him to a goal")
    public void createTaskTest(){
        Goal goal = new Goal();
        Task task = new Task();

        when(goalRepository.save(goal)).thenReturn(goal);

        Goal goalWithTask = goalCrudService.createTask(goal, task);

        assertThat(goalWithTask.getTasks()).hasSize(1);
        assertThat(goalWithTask.getTasks().get(0).getGoal()).isNotNull();
    }

    private DatesHistory buildDatesHistoryWithStopDate(){
        LocalDate stopDate = LocalDate.of(2022,1,10);
        LocalDateTime stoppedDateTime = stopDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        DatesHistory datesHistory = new DatesHistory();
        datesHistory.setStopDate(stoppedDateTime);

        return datesHistory;
    }


}