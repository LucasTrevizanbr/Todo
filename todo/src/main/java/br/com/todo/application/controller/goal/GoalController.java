package br.com.todo.application.controller.goal;

import br.com.todo.application.controller.goal.response.DetailedGoalResponse;
import br.com.todo.application.controller.goal.response.SimplifiedGoalResponse;
import br.com.todo.application.controller.goal.request.GoalUpdateRequest;
import br.com.todo.application.controller.goal.request.PostGoalRequest;
import br.com.todo.application.controller.goal.request.PostTaskRequest;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.User;
import br.com.todo.domain.service.goal.GoalCrudService;
import br.com.todo.domain.service.goal.FinishGoalService;
import br.com.todo.domain.service.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final UserCrudService userService;
    private final GoalCrudService goalService;
    private final FinishGoalService finishingGoalService;

    public GoalController(UserCrudService userService, GoalCrudService goalService, FinishGoalService finishingGoalService) {
        this.userService = userService;
        this.goalService = goalService;
        this.finishingGoalService = finishingGoalService;
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<Page<SimplifiedGoalResponse>> findPageGoal(@PathVariable Long userId,
                                                                     @PageableDefault(sort = "id", direction = Sort.Direction.ASC,page = 0, size = 10) Pageable pageable){
        userService.findById(userId);

        Page<Goal> goals = goalService.findAllByUserId(userId, pageable);
        Page<SimplifiedGoalResponse> metasDto = goals.map(SimplifiedGoalResponse::new);

        return ResponseEntity.ok(metasDto);
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<DetailedGoalResponse> findGoal(@PathVariable Long goalId) {
        Goal goal = goalService.findById(goalId);
        return ResponseEntity.ok(new DetailedGoalResponse(goal));
    }

    @PostMapping("/{idUser}")
    public ResponseEntity<DetailedGoalResponse> createGoal(@PathVariable Long idUser, @RequestBody @Valid PostGoalRequest metaForm,
                                                             @Autowired UriComponentsBuilder uriBuilder) {

        User user = userService.findById(idUser);
        Goal goal = goalService.saveGoal(user, metaForm.convertToGoalModel());
        DetailedGoalResponse goalResponse = new DetailedGoalResponse(goal);

        URI uri = uriBuilder.path("/api/goals/{id}").buildAndExpand(goalResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(goalResponse);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<DetailedGoalResponse> updateGoal(@PathVariable Long goalId,
                                                           @RequestBody @Valid GoalUpdateRequest form){
        Goal goal = goalService.findById(goalId);
        Goal updatedGoal = goalService.updateGoal(goal, form);

        return ResponseEntity.ok(new DetailedGoalResponse(updatedGoal));
    }

    @PutMapping("/complete/{goalId}")
    public ResponseEntity<DetailedGoalResponse> completeGoal(@PathVariable Long goalId) {

        Goal goal = goalService.findById(goalId);
        Goal completedGoal = finishingGoalService.completeGoal(goal);

        return ResponseEntity.ok().body(new DetailedGoalResponse(completedGoal));
    }

    @PutMapping("/stop/{goalId}")
    public ResponseEntity<DetailedGoalResponse> stopGoal(@PathVariable Long goalId) {

        Goal goal = goalService.findById(goalId);
        Goal stoppedGoal = goalService.stopGoal(goal);

        return ResponseEntity.ok().body(new DetailedGoalResponse(stoppedGoal));
    }

    @PutMapping("/resume/{goalId}")
    public ResponseEntity<DetailedGoalResponse> resumeGoal(@PathVariable Long goalId) {

        Goal goal = goalService.findById(goalId);
        Goal retakenGoal = goalService.resumeGoal(goal);

        return ResponseEntity.ok().body(new DetailedGoalResponse(retakenGoal));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {

        goalService.deleteGoalById(goalId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{goalId}/task")
    public ResponseEntity<DetailedGoalResponse> createTask(@PathVariable Long id,
                                                           @RequestBody @Valid PostTaskRequest form){
        Goal goal = goalService.findById(id);
        Goal updatedGoal = goalService.createTask(goal, form.convertToTaskModel());

        return ResponseEntity.status(HttpStatus.CREATED).body(new DetailedGoalResponse(updatedGoal));
    }

    @PutMapping("{goalId}/complete/task/{taskId}")
    public ResponseEntity<DetailedGoalResponse> completeTask(@PathVariable Long goalId, @PathVariable Long taskId) {

        Goal goal = goalService.findById(goalId);
        Goal updatedGoal = goalService.completeTask(goal, taskId);

        return ResponseEntity.ok(new DetailedGoalResponse(updatedGoal));

    }

    @PutMapping("{goalId}/update/task/{taskId}")
    public ResponseEntity<DetailedGoalResponse> updateTask(@PathVariable Long goalId, @PathVariable Long taskId,
                                                           @RequestBody @Valid PostTaskRequest form) {
        Goal goal = goalService.findById(goalId);
        Goal updatedGoal = goalService.updateTask(goal, taskId, form);

        return ResponseEntity.ok(new DetailedGoalResponse(updatedGoal));
    }

    @DeleteMapping("{goalId}/delete/task/{taskId}")
    public ResponseEntity<DetailedGoalResponse> deleteTask(@PathVariable Long goalId, @PathVariable Long taskId) {

        Goal goal = goalService.findById(goalId);
        Goal updatedGoal = goalService.deleteTask(goal, taskId);

        return ResponseEntity.ok(new DetailedGoalResponse(updatedGoal));
    }

}
