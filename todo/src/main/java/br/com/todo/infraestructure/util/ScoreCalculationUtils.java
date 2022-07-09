package br.com.todo.infraestructure.util;

import java.time.Duration;
import java.time.LocalDate;

public class ScoreCalculationUtils {

    public static int getPointsPerDay(LocalDate from, LocalDate until, double multiple) {
        int points = 0;
        long daysUntilComplete = Duration.between(from, until).toDays();

        for(int i = 0; i < daysUntilComplete; i++){
            points += multiple;
        }

        return points;
    }
}
