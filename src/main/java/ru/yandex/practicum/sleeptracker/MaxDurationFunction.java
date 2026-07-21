package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaxDurationFunction implements Function<List<SleepSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная продолжительность сна", "0 минут");
        }

        long maxMinutes = sessions.stream()
                .filter(SleepSessionFilters::isComplete)
                .mapToLong(s -> Duration.between(s.getSleepStart(), s.getSleepEnd()).toMinutes())
                .max()
                .orElse(0L);

        return new SleepAnalysisResult("Максимальная продолжительность сна", maxMinutes + " минут");
    }
}
