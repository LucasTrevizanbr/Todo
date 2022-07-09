package br.com.todo.domain.service.goal;

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
import java.util.List;

@Service
public class FinishingGoalService {

    private final GoalRepository goalRepository;
    private final UserCrudService userCrudService;

    public FinishingGoalService(GoalRepository goalRepository, UserCrudService userCrudService) {
        this.goalRepository = goalRepository;
        this.userCrudService = userCrudService;
    }

    public Goal completeGoal(Goal goal) {

        isGoalStopped(goal);
        isTasksCompleted(goal);

        ScoreDateStrategy scoreDateStrategy;

        LocalDate expectedFinish = goal.getDateHistory().getExpectedFinalizationDate().toLocalDate();
        LocalDate realFinish = goal.getDateHistory().getRealFinalizationDate().toLocalDate();

        if(expectedFinish.equals(realFinish)) {
            scoreDateStrategy = new CompleteGoalOnTimeStrategy();
        }else{
            scoreDateStrategy = new LateCompleteGoalStrategy();
        }

        int totalPoints = 0;
        totalPoints += goal.getDifficulty().getPoints();
        totalPoints += scoreDateStrategy.getScoreBasedOnDate(goal.getDateHistory());
        totalPoints += getScoreByStatus(goal.getStatus());

        totalPoints = goal.getPoints() + totalPoints;

        goal.setPoints(totalPoints);
        goal.setStatus(Status.FINISHED);

        User user = goal.getUser();
        Integer userPoints = user.getConclusionPointsGoal();
        user.setConclusionPointsGoal(userPoints + totalPoints);

        userCrudService.save(user);

        return  goalRepository.save(goal);
    }

    private int getScoreByStatus(Status status) {
        if(status != Status.STOPPED){
            return 3;
        }else{
            return 1;
        }
    }

    private boolean isGoalStopped(Goal goal) {
        return goal.getStatus() == Status.STOPPED;
    }

    private boolean isTasksCompleted(Goal goal){

        List<Task> tasks;

        if(!goal.getTasks().isEmpty()) {
            tasks = goal.getTasks();
        }else{
           return true;
        }

        return tasks.stream().allMatch(Task::isCompleted);
    }
}
