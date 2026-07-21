package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AvgDurationFunction implements Function<List<SleepSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сна", "0 минут");
        }

        double avgMinutes = sessions.stream()
                .filter(SleepSessionFilters::isComplete)
                .mapToLong(s -> Duration.between(s.getSleepStart(), s.getSleepEnd()).toMinutes())
                .average()
                .orElse(0.0);

        // Форматируем до одного знака после запятой
        return new SleepAnalysisResult(
                "Средняя продолжительность сна",
                String.format("%.1f минут", avgMinutes)
        );
    }
}
