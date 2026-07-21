package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsFunctionTest {

    private static final SleeplessNightsFunction FUNCTION = new SleeplessNightsFunction();

    @Test
    void testMixedSessions_countsOnlySleeplessNights() {
        var sessions = createMixedSessions();
        var expected = new SleepAnalysisResult("Количество бессонных ночей", "0");
        assertEquals(expected, FUNCTION.apply(sessions));
    }

    @Test
    void testEmptyAndNull_returnsZero() {
        assertEquals("0", FUNCTION.apply(List.of()).getValue());
        assertEquals("0", FUNCTION.apply(null).getValue());
    }

    @Test
    void testAllNightsCovered_returnsZero() {
        var sessions = createCoveredNights();
        assertEquals("0", FUNCTION.apply(sessions).getValue());
    }

    @Test
    void testSparseSessionsWithBoundaryShift_correctCount() {
        // Ожидаем 2 бессонные ночи (11.05 и 12.05). 13.05 покрыта.
        var sessions = createSparseSessions();
        assertEquals("2", FUNCTION.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsAcrossMonthAndYearBoundaries() {
        // Ожидаем 1 бессонную ночь: 01.01.2024
        var sessions = createYearBoundarySessions();
        assertEquals("1", FUNCTION.apply(sessions).getValue(),
                "Должна быть ровно 1 бессонная ночь: 01.01.2024");
    }

    // --- Вспомогательные методы для компактности тестов ---

    private List<SleepSession> createMixedSessions() {
        return List.of(
                new SleepSession(of(2024, 5, 8, 21, 0), of(2024, 5, 8, 23, 0), SleepQuality.GOOD),
                new SleepSession(of(2024, 5, 9, 1, 0),  of(2024, 5, 9, 7, 0),  SleepQuality.BAD)
        );
    }

    private List<SleepSession> createCoveredNights() {
        return List.of(
                new SleepSession(of(2024, 5, 9, 22, 0),   of(2024, 5, 10, 5, 0),    SleepQuality.NORMAL),
                new SleepSession(of(2024, 5, 10, 23, 30), of(2024, 5, 11, 4, 30),  SleepQuality.GOOD),
                new SleepSession(of(2024, 5, 11, 21, 0),  of(2024, 5, 12, 5, 30),  SleepQuality.BAD)
        );
    }

    private List<SleepSession> createSparseSessions() {
        return List.of(
                // 10.05 14:00 – 22:00
                new SleepSession(of(2024, 5, 10, 14, 0), of(2024, 5, 10, 22, 0), SleepQuality.NORMAL),
                // 13.05 02:00 – 07:00 (покрывает ночь 13.05)
                new SleepSession(of(2024, 5, 13, 2, 0),  of(2024, 5, 13, 7, 0),  SleepQuality.GOOD)
        );
    }

    private List<SleepSession> createYearBoundarySessions() {
        return List.of(
                // Покрывает ночь 31.12
                new SleepSession(of(2023, 12, 30, 22, 0), of(2023, 12, 31, 5, 0), SleepQuality.GOOD),
                // Покрывает ночь 02.01
                new SleepSession(of(2024, 1, 2, 2, 0),    of(2024, 1, 2, 8, 0),   SleepQuality.NORMAL)
        );
    }
    @Test
    void shouldIgnoreIncompleteSessionsWhenDeterminingUserType() {
        // Валидная ночная сессия (для совы)
        var owlSession = new SleepSession(
                LocalDateTime.of(2025, 10, 1, 23, 30),
                LocalDateTime.of(2025, 10, 2, 9, 30),
                SleepQuality.GOOD
        );

        // Битая сессия (null вместо времени)
        var brokenSession = new SleepSession(null, null, SleepQuality.BAD);

        var sessions = List.of(owlSession, brokenSession);
        var result = new SleepTypeFunction().apply(sessions);

        // Функция не должна упасть с NPE.
        // Результат должен определяться только по owlSession.
        assertEquals("Тип пользователя", result.getDescription());
        assertEquals(UserSleepType.OWL.getDisplayName(), result.getValue());
    }

}
