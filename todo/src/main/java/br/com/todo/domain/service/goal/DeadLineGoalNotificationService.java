package br.com.todo.domain.service.goal;

import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeadLineGoalNotificationService {

    private static final String CRON_DEAD_LINE_GOAL = "0 0 0 * * *";

    private final LocalDateTime today = LocalDateTime.now();

    @Value("${application.mail.meta-dia-vencimento.mensagem}")
    private String message;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private SendMailService emailService;

    @Scheduled(cron = CRON_DEAD_LINE_GOAL)
    public void senMailNotificationForDeadLineGoal(){

        LocalDateTime startOfDay = today.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = today.with(LocalTime.of(23, 59, 59));

        List<Goal> goalsInDeadLineDay = goalRepository.findGoalsInDeadLine(startOfDay, endOfDay);

        List<String> emails = goalsInDeadLineDay
                .stream()
                .map(goal -> goal.getUser().getEmail())
                .collect(Collectors.toList());

        emailService.sendMails(message, emails);
    }

}
