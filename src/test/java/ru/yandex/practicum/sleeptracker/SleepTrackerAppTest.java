package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SleepTrackerAppTest {

    // --- Тесты для TotalSessionsCountFunction (количество всех сессий) ---

    @Test
    void shouldReturnCorrectCountForNonEmptyList() {
        // Arrange: готовим тестовые данные
        var formatter = java.time.format.DateTimeFormatter.ofPattern(
                "dd.MM.yy HH:mm", java.util.Locale.ROOT
        );

        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", formatter),
                LocalDateTime.parse("02.10.25 06:00", formatter),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 23:00", formatter),
                LocalDateTime.parse("03.10.25 07:00", formatter),
                SleepQuality.NORMAL
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 00:30", formatter),
                LocalDateTime.parse("03.10.25 05:30", formatter),
                SleepQuality.BAD
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new TotalSessionsCountFunction();

        // Act: вызываем тестируемый метод
        var result = function.apply(sessions);

        // Assert: проверяем результат
        assertEquals("Количество сессий сна", result.getDescription());
        assertEquals("3", result.getValue().toString());
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        // Arrange
        List<SleepSession> sessions = Collections.emptyList();
        var function = new TotalSessionsCountFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Количество сессий сна", result.getDescription());
        assertEquals("0", result.getValue().toString());
    }


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.ROOT);


    // --- Тесты для MinDurationFunction (минимальная продолжительность сессии) ---

    @Test
    void shouldReturnCorrectMinDurationForDifferentLengths() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // 8 часов = 480 минут
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // 5 ч 30 мин = 330 минут
                LocalDateTime.parse("02.10.25 05:30", FORMATTER),
                SleepQuality.BAD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 01:00", FORMATTER), // 2 ч 15 мин = 135 минут
                LocalDateTime.parse("03.10.25 03:15", FORMATTER),
                SleepQuality.NORMAL
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new MinDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Минимальная продолжительность сна", result.getDescription());
        assertEquals("135 минут", result.getValue().toString());
    }

    @Test
    void shouldReturnCorrectMinWhenAllSessionsHaveSameDuration() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // ровно 6 часов = 360 минут
                LocalDateTime.parse("02.10.25 04:00", FORMATTER),
                SleepQuality.NORMAL
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 02:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("03.10.25 08:00", FORMATTER),
                SleepQuality.BAD
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new MinDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Минимальная продолжительность сна", result.getDescription());
        assertEquals("360 минут", result.getValue().toString());
    }


    // --- Тесты для MaxDurationFunction (максимальная продолжительность сессии) ---

    @Test
    void shouldReturnCorrectMaxDurationForDifferentLengths() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // 8 часов = 480 минут
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // 5 ч 30 мин = 330 минут
                LocalDateTime.parse("02.10.25 05:30", FORMATTER),
                SleepQuality.BAD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 01:00", FORMATTER), // 2 ч 15 мин = 135 минут
                LocalDateTime.parse("03.10.25 03:15", FORMATTER),
                SleepQuality.NORMAL
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new MaxDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Максимальная продолжительность сна", result.getDescription());
        assertEquals("480 минут", result.getValue().toString());
    }

    @Test
    void shouldReturnCorrectMaxWhenAllSessionsHaveSameDuration() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // ровно 6 часов = 360 минут
                LocalDateTime.parse("02.10.25 04:00", FORMATTER),
                SleepQuality.NORMAL
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 02:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("03.10.25 08:00", FORMATTER),
                SleepQuality.BAD
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new MaxDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Максимальная продолжительность сна", result.getDescription());
        assertEquals("360 минут", result.getValue().toString());
    }


    // --- Тесты для AvgDurationFunction (средняя продолжительность сессии) ---

    @Test
    void shouldCalculateAverageCorrectlyForDifferentDurations() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // 8 часов = 480 мин
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // 5 ч 30 мин = 330 мин
                LocalDateTime.parse("02.10.25 05:30", FORMATTER),
                SleepQuality.BAD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 01:00", FORMATTER), // 2 ч 15 мин = 135 мин
                LocalDateTime.parse("03.10.25 03:15", FORMATTER),
                SleepQuality.NORMAL
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new AvgDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Средняя продолжительность сна", result.getDescription());

        String actualValue = result.getValue().toString().replace(',', '.');
        assertEquals("315.0 минут", actualValue);
    }


    @Test
    void shouldReturnSameValueWhenAllDurationsAreEqual() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER), // ровно 6 часов = 360 мин
                LocalDateTime.parse("02.10.25 04:00", FORMATTER),
                SleepQuality.NORMAL
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 02:00", FORMATTER), // тоже 6 часов
                LocalDateTime.parse("03.10.25 08:00", FORMATTER),
                SleepQuality.BAD
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new AvgDurationFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Средняя продолжительность сна", result.getDescription());

        String actualValue = result.getValue().toString().replace(',', '.');
        assertEquals("360.0 минут", actualValue);
    }


    // --- Тесты для BadQualityCountFunction (количество сессий с плохим качеством сна) ---

    @Test
    void shouldCountBadSessionsCorrectlyWhenMixedQualities() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER),
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER),
                LocalDateTime.parse("02.10.25 05:30", FORMATTER),
                SleepQuality.BAD
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 01:00", FORMATTER),
                LocalDateTime.parse("03.10.25 03:15", FORMATTER),
                SleepQuality.BAD
        );
        SleepSession s4 = new SleepSession(
                LocalDateTime.parse("04.10.25 00:30", FORMATTER),
                LocalDateTime.parse("04.10.25 07:30", FORMATTER),
                SleepQuality.NORMAL
        );

        List<SleepSession> sessions = List.of(s1, s2, s3, s4);
        var function = new BadQualityCountFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Количество сессий с плохим качеством сна", result.getDescription());
        assertEquals("2", result.getValue().toString());
    }

    @Test
    void shouldReturnZeroWhenNoBadSessions() {
        // Arrange
        SleepSession s1 = new SleepSession(
                LocalDateTime.parse("01.10.25 22:00", FORMATTER),
                LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                SleepQuality.GOOD
        );
        SleepSession s2 = new SleepSession(
                LocalDateTime.parse("02.10.25 00:00", FORMATTER),
                LocalDateTime.parse("02.10.25 05:30", FORMATTER),
                SleepQuality.NORMAL
        );
        SleepSession s3 = new SleepSession(
                LocalDateTime.parse("03.10.25 01:00", FORMATTER),
                LocalDateTime.parse("03.10.25 03:15", FORMATTER),
                SleepQuality.GOOD
        );

        List<SleepSession> sessions = List.of(s1, s2, s3);
        var function = new BadQualityCountFunction();

        // Act
        var result = function.apply(sessions);

        // Assert
        assertEquals("Количество сессий с плохим качеством сна", result.getDescription());
        assertEquals("0", result.getValue().toString());
    }
}
