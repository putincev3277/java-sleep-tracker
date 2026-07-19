package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinDurationFunction implements Function<List<SleepSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная продолжительность сна", "0 минут");
        }

        long minMinutes = sessions.stream()
                .mapToLong(s -> Duration.between(s.getSleepStart(), s.getSleepEnd()).toMinutes())
                .min()
                .orElse(0L);

        return new SleepAnalysisResult("Минимальная продолжительность сна", minMinutes + " минут");
    }
}
