package br.com.todo.domain.model;

import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objective;

    @Embedded
    private DatesHistory dateHistory;

    private Integer points = 0;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ONGOING;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "goal")
    @JsonIgnoreProperties("goal")
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Goal(String objective, DatesHistory dateHistory, Status status,
                User user, Difficulty difficulty) {
        this.objective = objective;
        this.dateHistory = dateHistory;
        this.status = status;
        this.user = user;
        this.difficulty = difficulty;
    }

    public Goal(){}

    public void addTask(Task tarefa){
        tarefa.setGoal(this);
        this.tasks.add(tarefa);
    }

    public Long getId() {
        return id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public DatesHistory getDateHistory() {
        return dateHistory;
    }

    public void setDateHistory(DatesHistory dateHistory) {
        this.dateHistory = dateHistory;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return id.equals(goal.id) && user.equals(goal.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
