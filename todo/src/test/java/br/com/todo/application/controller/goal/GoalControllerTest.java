package br.com.todo.application.controller.goal;

import br.com.todo.application.controller.goal.request.PostGoalRequest;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.Task;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.repository.TaskRepository;
import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.service.goal.FinishGoalService;
import br.com.todo.domain.service.goal.GoalCrudService;
import br.com.todo.domain.service.user.UserCrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithMockUser(username="admin",roles={"USER","ADMIN"})
class GoalControllerTest {

    private final String GOAL_URI = "/api/goals";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  UserCrudService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private  GoalCrudService goalService;

    @Autowired
    private  FinishGoalService finishingGoalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void cleanDatabase(){
        taskRepository.deleteAll();
        goalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void tearDown(){
        taskRepository.deleteAll();
        goalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a goal with success")
    public void createGoalSuccessTest() throws Exception {
        User user = this.buildAndSaveAUser();
        PostGoalRequest goalRequest = this.buildValidGoalPostRequest();

        String goalPostRequestJson = objectMapper.writeValueAsString(goalRequest);

        mockMvc.perform(post(GOAL_URI +"/"+ user.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalPostRequestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("objective").value("Learn Java"))
                .andExpect(jsonPath("tasks").isNotEmpty())
                .andExpect(jsonPath("datesHistory").isNotEmpty())
                .andExpect(jsonPath("points").isNumber())
                .andExpect(jsonPath("status").value("ONGOING"))
                .andExpect(jsonPath("difficulty").value("HARD"))
                .andExpect(jsonPath("ownerName").value("Japa polo olsen"));

        List<Goal> savedGoal = goalRepository.findAll();
        assertEquals("Learn Java", savedGoal.get(0).getObjective());
        assertEquals(Difficulty.HARD, savedGoal.get(0).getDifficulty());
    }

    @Test
    @DisplayName("Should complete a goal with success")
    public void completeGoalSuccessTest() throws Exception {
        Goal goal = buildAndSaveGoal();

        mockMvc.perform(put(GOAL_URI +"/complete/"+ goal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("objective").value("Learn TypeScript"))
                .andExpect(jsonPath("datesHistory.realFinalizationDate").isNotEmpty())
                .andExpect(jsonPath("points").isNumber())
                .andExpect(jsonPath("status").value("FINISHED"))
                .andExpect(jsonPath("difficulty").value("HARD"))
                .andExpect(jsonPath("ownerName").value("Japa polo olsen"));

        List<Goal> savedGoal = goalRepository.findAll();
        assertEquals("Learn TypeScript", savedGoal.get(0).getObjective());
        assertEquals(Difficulty.HARD, savedGoal.get(0).getDifficulty());
        assertEquals(Status.FINISHED, savedGoal.get(0).getStatus());
    }

    @Test
    @DisplayName("Should stop a goal with success")
    public void stopGoalSuccessTest() throws Exception {
        Goal goal = buildAndSaveGoal();

        mockMvc.perform(put(GOAL_URI +"/stop/"+ goal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("objective").value("Learn TypeScript"))
                .andExpect(jsonPath("datesHistory.stopDate").isNotEmpty())
                .andExpect(jsonPath("points").isNumber())
                .andExpect(jsonPath("status").value("STOPPED"))
                .andExpect(jsonPath("difficulty").value("HARD"))
                .andExpect(jsonPath("ownerName").value("Japa polo olsen"));

        List<Goal> savedGoal = goalRepository.findAll();
        assertEquals("Learn TypeScript", savedGoal.get(0).getObjective());
        assertEquals(Difficulty.HARD, savedGoal.get(0).getDifficulty());
        assertEquals(Status.STOPPED, savedGoal.get(0).getStatus());
    }

    @Test
    @DisplayName("Should resume a goal with success")
    public void resumeGoalSuccessTest() throws Exception {
        Goal goal = buildAndSaveAStoppedGoal();

        mockMvc.perform(put(GOAL_URI +"/resume/"+ goal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("objective").value("Learn MongoDB"))
                .andExpect(jsonPath("datesHistory.stopDate").isNotEmpty())
                .andExpect(jsonPath("datesHistory.retakenDate").isNotEmpty())
                .andExpect(jsonPath("points").isNumber())
                .andExpect(jsonPath("status").value("RETAKEN"))
                .andExpect(jsonPath("difficulty").value("HARD"))
                .andExpect(jsonPath("ownerName").value("Japa polo olsen"));

        List<Goal> savedGoal = goalRepository.findAll();
        assertEquals("Learn MongoDB", savedGoal.get(0).getObjective());
        assertEquals(Difficulty.HARD, savedGoal.get(0).getDifficulty());
        assertEquals(Status.RETAKEN, savedGoal.get(0).getStatus());
    }

    @Test
    @DisplayName("Should find a goal with success")
    public void findGoalSuccessTest() throws Exception {
        Goal goal = buildAndSaveGoal();

        mockMvc.perform(get(GOAL_URI +"/"+ goal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("objective").value("Learn TypeScript"))
                .andExpect(jsonPath("datesHistory").isNotEmpty())
                .andExpect(jsonPath("points").isNumber())
                .andExpect(jsonPath("status").value("ONGOING"))
                .andExpect(jsonPath("difficulty").value("HARD"))
                .andExpect(jsonPath("ownerName").value("Japa polo olsen"));

        List<Goal> savedGoal = goalRepository.findAll();
        assertEquals("Learn TypeScript", savedGoal.get(0).getObjective());
        assertEquals(Difficulty.HARD, savedGoal.get(0).getDifficulty());
    }

    @Test
    @DisplayName("Should delete a goal with success")
    public void deleteGoalSuccessTest() throws Exception {
        Goal goal = buildAndSaveGoal();

        mockMvc.perform(delete(GOAL_URI +"/"+ goal.getId()))
                .andExpect(status().isNoContent());

        List<Goal> savedGoal = goalRepository.findAll();
        assertThat(savedGoal).isEmpty();
    }

    private Goal buildAndSaveAStoppedGoal() {
        DatesHistory datesHistory = new DatesHistory();
        datesHistory.setRealFinalizationDate(LocalDateTime.now());
        datesHistory.setStopDate(LocalDateTime.now());

        Goal goal = new Goal();
        goal.setObjective("Learn MongoDB");
        goal.setStatus(Status.STOPPED);
        goal.setDifficulty(Difficulty.HARD);
        goal.setTasks(List.of(new Task("Learn documented oriented database")));
        goal.setDateHistory(datesHistory);
        goal.setUser(buildAndSaveAUser());

        return goalRepository.save(goal);
    }

    private Goal buildAndSaveGoal() {
        Goal goal = new Goal();
        goal.setObjective("Learn TypeScript");
        goal.setStatus(Status.ONGOING);
        goal.setDifficulty(Difficulty.HARD);
        goal.setTasks(List.of(new Task("Learn OOP")));
        goal.setDateHistory(new DatesHistory(LocalDateTime.now()));
        goal.setUser(buildAndSaveAUser());

        return goalRepository.save(goal);
    }

    private PostGoalRequest buildValidGoalPostRequest() {
        LocalDate localDate = LocalDate.of(2022,12,19);
        LocalTime localTime = LocalTime.of(2,11,00);
        List<String> tasks = Arrays.asList("Learn OOP","Learn Generics");

        PostGoalRequest postGoalRequest = new PostGoalRequest();
        postGoalRequest.setObjective("Learn Java");
        postGoalRequest.setDifficulty(Difficulty.HARD);
        postGoalRequest.setExpectedFinalizationDate(LocalDateTime.of(localDate, localTime));
        postGoalRequest.setDescriptionTasks(tasks);

        return postGoalRequest;
    }

    private User buildAndSaveAUser() {
        User user = new User();
        user.setName("Japa polo olsen");
        user.setEmail("test@test.com");
        user.setNickName("Senior test guy");

        return userRepository.save(user);
    }
}