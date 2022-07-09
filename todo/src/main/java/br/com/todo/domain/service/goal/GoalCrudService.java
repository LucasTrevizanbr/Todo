package br.com.todo.domain.service.goal;

import br.com.todo.application.controller.goal.request.GoalUpdateRequest;
import br.com.todo.application.controller.goal.request.PostTaskRequest;
import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.*;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.service.score.strategy.ScoreValues;
import br.com.todo.domain.service.task.CrudTaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.todo.infraestructure.util.ScoreCalculationUtils.getPointsPerDay;

@Service
public class GoalCrudService {

    private final GoalRepository goalRepository;
    private final CrudTaskService taskService;

    public GoalCrudService(GoalRepository goalRepository, CrudTaskService taskService) {
        this.goalRepository = goalRepository;
        this.taskService = taskService;
    }

    public Goal findById(Long goalId){
        Optional<Goal> goal = goalRepository.findById(goalId);

        if(goal.isEmpty()){
            throw new NotFoundException(ApiError.TG001.getMessageError(), ApiError.TG001.name());
        }

        return goal.get();
    }

    @Transactional
    public Goal saveGoal(User user, Goal goal) {
        goal.setUser(user);
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal stopGoal(Goal goal) {
        DatesHistory datas = goal.getDateHistory();
        datas.setStopDate(LocalDateTime.now());

        goal.setDateHistory(datas);
        goal.setStatus(Status.STOPPED);

        return goalRepository.save(goal);
    }

    @Transactional
    public Goal resumeGoal(Goal goal) {
        goal.getDateHistory().setRetakenDate(LocalDateTime.now());

        LocalDateTime stoppedDate = goal.getDateHistory().getStopDate();
        LocalDateTime retakenDate =  goal.getDateHistory().getRetakenDate();

        int pointsAfterPenalty =
                goal.getPoints() - getPointsPerDay(stoppedDate, retakenDate, ScoreValues.PER_DAY_PENALTY.getValue());

        goal.setPoints(pointsAfterPenalty);
        goal.setStatus(Status.RETAKEN);

        return goalRepository.save(goal);
    }

    @Transactional
    public Goal updateGoal(Goal goal, GoalUpdateRequest form) {
        goal.setObjective(form.getObjective());
        return goalRepository.save(goal);
    }

    public void deleteGoalById(Long id) {
        findById(id);
        goalRepository.deleteById(id);
    }

    @Transactional
    public Goal createTask(Goal goal, Task task) {
        goal.addTask(task);
        return goalRepository.save(goal);
    }

    public Goal completeTask(Goal goal, Long taskId) {
        return taskService.completeTask(goal, taskId);
    }

    public Goal updateTask(Goal goal, Long taskId, PostTaskRequest newDescription) {
        return taskService.updateTask(goal, taskId, newDescription);
    }

    public Goal deleteTask(Goal goal, Long taskId) {
        return taskService.deleteTask(goal, taskId);
    }

    public Page<Goal> findAllByUserId(Long userId, Pageable pageable) {
        return goalRepository.findAllByUserId(userId, pageable);
    }
}
