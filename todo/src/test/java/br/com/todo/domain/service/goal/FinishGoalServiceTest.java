package br.com.todo.domain.service.goal;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.StoppedGoalException;
import br.com.todo.application.exception.errors.UnfinishedTasksException;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.service.user.UserCrudService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class FinishGoalServiceTest {

    private final static LocalDate COMPLETE_IN_TIME_DATE_GOAL = LocalDate.of(2022, 1, 23);
    private final static LocalDate COMPLETE_GOAL_DELAY_THREE_DAYS = LocalDate.of(2022, 1, 26);
    private final static LocalDate COMPLETE_GOAL_EARLY_THREE_DAYS = LocalDate.of(2022, 1, 20);

    private final Clock fixedClockFinalizationDateGoalOnTime = Clock
            .fixed(
                    COMPLETE_IN_TIME_DATE_GOAL
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(), ZoneId.systemDefault()
            );

    private final Clock fixedClockFinalizationDateGoal3DaysLate = Clock
            .fixed(
                    COMPLETE_GOAL_DELAY_THREE_DAYS
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(), ZoneId.systemDefault()
            );

    private final Clock fixedClockFinalizationDateGoal3DaysEarly = Clock
            .fixed(
                    COMPLETE_GOAL_EARLY_THREE_DAYS
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(), ZoneId.systemDefault()
            );

    @MockBean
    private GoalRepository goalRepository;

    @MockBean
    private UserCrudService userCrudService;

    @MockBean
    private Clock clock;

    @Autowired
    private FinishGoalService finishGoalService;

    @Test
    @DisplayName("Should complete a goal on time with ONGOING status")
    public void completeGoalOnTimeNotLateWithSuccess(){

        Goal goal = buildAGoalOnTimeWorth19Points();

        when(clock.instant()).thenReturn(fixedClockFinalizationDateGoalOnTime.instant());
        when(clock.getZone()).thenReturn(fixedClockFinalizationDateGoalOnTime.getZone());

        when(goalRepository.save(goal)).thenReturn(goal);
        when(userCrudService.save(goal.getUser())).thenReturn(goal.getUser());

        Goal completedGoal = finishGoalService.completeGoal(goal);

        assertThat(completedGoal.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(completedGoal.getPoints()).isEqualTo(19);
        assertThat(completedGoal.getUser().getConclusionPointsGoal()).isEqualTo(19);
        verify(goalRepository, times(1)).save(goal);
        verify(userCrudService, times(1)).save(goal.getUser());

    }

    @Test
    @DisplayName("Should complete a goal that has RETAKEN and late by 3 days. ")
    public void completeGoalLateWithSuccess(){
        Goal goal = buildAGoal3DaysLateWorth15Points();

        when(clock.instant()).thenReturn(fixedClockFinalizationDateGoal3DaysLate.instant());
        when(clock.getZone()).thenReturn(fixedClockFinalizationDateGoal3DaysLate.getZone());

        when(goalRepository.save(goal)).thenReturn(goal);
        when(userCrudService.save(goal.getUser())).thenReturn(goal.getUser());

        Goal lateCompleteGoal = finishGoalService.completeGoal(goal);

        assertThat(lateCompleteGoal.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(lateCompleteGoal.getPoints()).isEqualTo(15);
        assertThat(lateCompleteGoal.getUser().getConclusionPointsGoal()).isEqualTo(15);
        verify(goalRepository, times(1)).save(goal);
        verify(userCrudService, times(1)).save(goal.getUser());
    }

    @Test
    @DisplayName("Should complete a goal 3 days early.")
    public void completeGoalEarlyWithSuccess(){
        Goal goal = buildAGoal3DaysEarlyWorth24Points();

        when(clock.instant()).thenReturn(fixedClockFinalizationDateGoal3DaysEarly.instant());
        when(clock.getZone()).thenReturn(fixedClockFinalizationDateGoal3DaysEarly.getZone());

        when(goalRepository.save(goal)).thenReturn(goal);
        when(userCrudService.save(goal.getUser())).thenReturn(goal.getUser());

        Goal earlyCompleteGoal = finishGoalService.completeGoal(goal);

        assertThat(earlyCompleteGoal.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(earlyCompleteGoal.getPoints()).isEqualTo(24);
        assertThat(earlyCompleteGoal.getUser().getConclusionPointsGoal()).isEqualTo(24);
        verify(goalRepository, times(1)).save(goal);
        verify(userCrudService, times(1)).save(goal.getUser());
    }

    @Test
    @DisplayName("Should return a exception with a message error TG101")
    public void completeGoalStoppedErrorTest(){
        Goal goal = new Goal();
        goal.setStatus(Status.STOPPED);

        assertThrows(StoppedGoalException.class,() -> finishGoalService.completeGoal(goal),
                ApiError.TG101.getMessageError());

        Mockito.verify(goalRepository, Mockito.times(0)).save(goal);
        Mockito.verify(userCrudService, Mockito.times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return a exception with a message error TG102")
    public void completeGoalUnfinishedTasksErrorTest(){
        Goal goal = new Goal();
        Task unfinishedTask = new Task();
        goal.addTask(unfinishedTask);

        assertThrows(UnfinishedTasksException.class,() -> finishGoalService.completeGoal(goal),
                ApiError.TG102.getMessageError());

        verify(goalRepository, Mockito.times(0)).save(goal);
        verify(userCrudService, Mockito.times(0)).save(any(User.class));
    }

    private Goal buildAGoalOnTimeWorth19Points() {
        DatesHistory datesHistory = buildDatesHistoryFor10DaysGoalDeadlineOnTime();
        Goal goal = new Goal();;
        User user = new User();
        Task finishedTask = new Task();

        finishedTask.setCompleted(true);
        user.setConclusionPointsGoal(0);
        goal.addTask(finishedTask);
        goal.setUser(user);
        goal.setDateHistory(datesHistory);
        goal.setDifficulty(Difficulty.HARD);
        goal.setStatus(Status.ONGOING);
        goal.setPoints(0);

        return goal;
    }

    private Goal buildAGoal3DaysLateWorth15Points() {
        DatesHistory datesHistory = buildDatesHistoryFor10DaysGoalDeadlineWith3DaysOfDelayToComplete();
        Goal goal = new Goal();;
        User user = new User();

        user.setConclusionPointsGoal(0);
        goal.setUser(user);
        goal.setDateHistory(datesHistory);
        goal.setDifficulty(Difficulty.HARD);
        goal.setStatus(Status.RETAKEN);
        goal.setPoints(0);

        return goal;
    }

    private Goal buildAGoal3DaysEarlyWorth24Points() {
        DatesHistory datesHistory = buildDatesHistoryFor10DaysGoalDeadlineWith3DaysOfDelayToComplete();
        Goal goal = new Goal();;
        User user = new User();

        user.setConclusionPointsGoal(0);
        goal.setUser(user);
        goal.setDateHistory(datesHistory);
        goal.setDifficulty(Difficulty.HARD);
        goal.setStatus(Status.ONGOING);
        goal.setPoints(0);

        return goal;
    }

    private DatesHistory buildDatesHistoryFor10DaysGoalDeadlineOnTime() {
        LocalDate goalCreationDate = LocalDate.of(2022,1,13);
        LocalDate goalExpectedFinalizationDate = LocalDate.of(2022,1,23);

        LocalDateTime expectedFinalizationDate = goalExpectedFinalizationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime creationDate = goalCreationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        DatesHistory datesHistory = new DatesHistory();
        datesHistory.setCreationDate(creationDate);
        datesHistory.setExpectedFinalizationDate(expectedFinalizationDate);
        datesHistory.setRealFinalizationDate(expectedFinalizationDate);

        return datesHistory;
    }

    private DatesHistory buildDatesHistoryFor10DaysGoalDeadlineWith3DaysOfDelayToComplete() {
        LocalDate goalCreationDate = LocalDate.of(2022,1,13);
        LocalDate goalExpectedFinalizationDate = LocalDate.of(2022,1,23);
        LocalDate realFinalizationDate = LocalDate.of(2022,1,26);

        LocalDateTime creationDate = goalCreationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime expectedFinalizationDate = goalExpectedFinalizationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime realFinalizationDateWith3DayDelay =realFinalizationDate
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime();

        DatesHistory datesHistory = new DatesHistory();
        datesHistory.setCreationDate(creationDate);
        datesHistory.setExpectedFinalizationDate(expectedFinalizationDate);
        datesHistory.setRealFinalizationDate(realFinalizationDateWith3DayDelay);

        return datesHistory;
    }

}