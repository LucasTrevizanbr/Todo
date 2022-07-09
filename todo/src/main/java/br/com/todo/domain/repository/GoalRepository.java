package br.com.todo.domain.repository;

import br.com.todo.domain.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long > {

    Page<Goal> findAllByUserId(Long userId, Pageable pageable);

    @Query("FROM Goal g WHERE g.dateHistory.expectedFinalizationDate BETWEEN :startOfTheDay AND :endOfTheDay " +
            "AND g.status <> 'COMPLETED'" )
    List<Goal> findGoalsInDeadLine(LocalDateTime startOfTheDay, LocalDateTime endOfTheDay);
}
