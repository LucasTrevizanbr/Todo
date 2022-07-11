package br.com.todo.infraestructure.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class ScoreCalculationUtils {

    public static int getPointsPerDay(LocalDateTime from, LocalDateTime until, double multiple) {
        double points = 0;

        long daysUntilComplete = Duration.between(from, until).toDays();

        for(int i = 0; i < daysUntilComplete; i++){
            points += multiple;
        }

        return (int) Math.round(points);
    }
}
