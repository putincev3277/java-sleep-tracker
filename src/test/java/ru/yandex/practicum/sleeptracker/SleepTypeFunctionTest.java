package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTypeFunctionTest {

    private final SleepTypeFunction function = new SleepTypeFunction();

    // Компактные заготовки сессий (чтобы не дублировать LocalDateTime.of)
    private static final List<SleepSession> LARK_SESSIONS = List.of(
            new SleepSession(
                    LocalDateTime.of(2024, 5, 1, 21, 0),
                    LocalDateTime.of(2024, 5, 2, 6, 30),
                    SleepQuality.GOOD
            ),
            new SleepSession(
                    LocalDateTime.of(2024, 5, 3, 20, 30),
                    LocalDateTime.of(2024, 5, 4, 5, 0),
                    SleepQuality.NORMAL
            )
    );

    private static final List<SleepSession> OWL_SESSIONS = List.of(
            new SleepSession(
                    LocalDateTime.of(2024, 5, 1, 23, 1),
                    LocalDateTime.of(2024, 5, 2, 10, 0),
                    SleepQuality.BAD
            ),
            new SleepSession(
                    LocalDateTime.of(2024, 5, 3, 23, 30),
                    LocalDateTime.of(2024, 5, 4, 11, 15),
                    SleepQuality.NORMAL
            )
    );


    @Test
    void testAllLark_returnsLark() {
        var result = function.apply(LARK_SESSIONS);
        assertEquals("Тип пользователя", result.getDescription());
        assertEquals("Жаворонок", result.getValue());
    }

    @Test
    void testAllOwl_returnsOwl() {
        var result = function.apply(OWL_SESSIONS);
        assertEquals("Тип пользователя", result.getDescription());
        assertEquals("Сова", result.getValue());
    }

    @Test
    void testTieBetweenTypes_returnsDove() {
        var sessions = List.of(LARK_SESSIONS.getFirst(), OWL_SESSIONS.getFirst());
        var result = function.apply(sessions);
        assertEquals("Тип пользователя", result.getDescription());
        assertEquals("Голубь", result.getValue());
    }

    @Test
    void testEmptyList_returnsDoveByDefault() {
        var result = function.apply(List.of());
        assertEquals("Тип пользователя", result.getDescription());
        assertEquals("Голубь", result.getValue());
    }
}
