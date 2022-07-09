package br.com.todo.domain.service.goal;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.StoppedGoalException;
import br.com.todo.application.exception.errors.UnfinishedTasksException;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.service.score.strategy.CompleteGoalOnTimeStrategy;
import br.com.todo.domain.service.score.strategy.LateCompleteGoalStrategy;
import br.com.todo.domain.service.score.strategy.ScoreDateStrategy;
import br.com.todo.domain.service.user.UserCrudService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinishGoalService {

    private final GoalRepository goalRepository;
    private final UserCrudService userCrudService;

    private ScoreDateStrategy scoreDateStrategy;

    public FinishGoalService(GoalRepository goalRepository, UserCrudService userCrudService) {
        this.goalRepository = goalRepository;
        this.userCrudService = userCrudService;
    }

    public Goal completeGoal(Goal goal) {

        if(this.isGoalStopped(goal)){
            throw new StoppedGoalException(ApiError.TG001.getMessageError(), ApiError.TG101.name());
        }

        if(!this.isTasksCompleted(goal)){
            throw new UnfinishedTasksException(ApiError.TG102.getMessageError(), ApiError.TG102.name());
        }

        goal = this.setDateStrategyScoreForThisGoal(goal);
        goal = this.setPointsReceivableForThisGoal(goal);
        goal.setStatus(Status.FINISHED);

        User user = this.setPointsReceivableForThisUser(goal);

        userCrudService.save(user);
        return  goalRepository.save(goal);
    }

    private boolean isGoalStopped(Goal goal) {
        return goal.getStatus() == Status.STOPPED;
    }

    private boolean isTasksCompleted(Goal goal){

        List<Task> tasks;

        if(goal.getTasks().isEmpty()) {
            return true;
        }else{
            tasks = goal.getTasks();
            return tasks.stream().allMatch(Task::isCompleted);
        }
    }

    private Goal setDateStrategyScoreForThisGoal(Goal goal) {

        LocalDateTime dateTimeNow = LocalDateTime.now();

        LocalDate expectedFinish = goal.getDateHistory().getExpectedFinalizationDate().toLocalDate();
        LocalDate realFinish = dateTimeNow.toLocalDate();

        if(expectedFinish.isEqual(realFinish)) {
            this.scoreDateStrategy = new CompleteGoalOnTimeStrategy();
        }else{
            this.scoreDateStrategy = new LateCompleteGoalStrategy();
        }

        Goal goalWithFinalizationDateSet = goal;
        goalWithFinalizationDateSet.getDateHistory().setRealFinalizationDate(dateTimeNow);

        return goalWithFinalizationDateSet;
    }

    private Goal setPointsReceivableForThisGoal(Goal goal) {
        int accumulator = 0;
        accumulator += goal.getDifficulty().getPoints();
        accumulator += scoreDateStrategy.getScoreBasedOnDate(goal.getDateHistory());
        accumulator += getScoreByStatus(goal.getStatus());

        int totalPoints = goal.getPoints() + accumulator;

        Goal goalWithPointsApplied = goal;
        goalWithPointsApplied.setPoints(totalPoints);

        return goalWithPointsApplied;
    }

    private User setPointsReceivableForThisUser(Goal goal) {
        User user = goal.getUser();
        Integer userPoints = user.getConclusionPointsGoal();
        user.setConclusionPointsGoal(userPoints + goal.getPoints());

        return user;
    }

    private int getScoreByStatus(Status status) {
        if(status != Status.RETAKEN){
            return 3;
        }else{
            return 1;
        }
    }
}
