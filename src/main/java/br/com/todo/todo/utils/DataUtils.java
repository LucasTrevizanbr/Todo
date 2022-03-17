package br.com.todo.todo.utils;

import java.time.Duration;
import java.time.LocalDateTime;


public class DataUtils {

    public static int diasEntreDatas(LocalDateTime de, LocalDateTime ate) {

        Long diasPassados = Duration.between(de, ate).toDays();

        return diasPassados.intValue();
    }
}
