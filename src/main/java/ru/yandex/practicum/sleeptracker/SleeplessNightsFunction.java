package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final int NIGHT_START_HOUR = 0;
    private static final int NIGHT_END_HOUR = 6;
    private static final int NOON_HOUR = 12;

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", "0");
        }

        var ordered = SleepSessionsOrdering.sortedByStart(sessions);

        LocalDate minStartDate = ordered.stream()
                .map(s -> s.getSleepStart().toLocalDate())
                .min(LocalDate::compareTo)
                .orElseThrow();

        LocalDate maxEndDate = ordered.stream()
                .map(s -> s.getSleepEnd().toLocalDate())
                .max(LocalDate::compareTo)
                .orElseThrow();

        // Если в твоём проекте действительно есть метод getFirst() — оставь.
        // В стандартном List его нет, поэтому безопаснее использовать get(0).
        LocalDateTime firstSessionStart = ordered.getFirst().getSleepStart();

        LocalDate startNightDate;
        if (firstSessionStart.getHour() >= NOON_HOUR) {
            startNightDate = minStartDate.plusDays(1);
        } else {
            startNightDate = minStartDate.minusDays(1);
        }

        if (startNightDate.isAfter(maxEndDate)) {
            return new SleepAnalysisResult("Количество бессонных ночей", "0");
        }

        long sleeplessCount = LongStream.rangeClosed(0L, ChronoUnit.DAYS.between(startNightDate, maxEndDate))
                .mapToObj(startNightDate::plusDays)
                .filter(date -> isNightSleepless(date, ordered))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", String.valueOf(sleeplessCount));
    }

    private boolean isNightSleepless(LocalDate date, List<SleepSession> sessions) {
        LocalDateTime nightStart = date.atTime(NIGHT_START_HOUR, 0);
        LocalDateTime nightEnd = date.atTime(NIGHT_END_HOUR, 0);

        boolean hasAnySessionCrossingNight = sessions.stream()
                .anyMatch(session -> isOverlap(session, nightStart, nightEnd));

        return !hasAnySessionCrossingNight;
    }

    // Метод-ссылка вместо лямбды: [sStart, sEnd) и [nightStart, nightEnd) пересекаются?
    private boolean isOverlap(SleepSession session, LocalDateTime nightStart, LocalDateTime nightEnd) {
        LocalDateTime sStart = session.getSleepStart();
        LocalDateTime sEnd = session.getSleepEnd();
        return sStart.isBefore(nightEnd) && sEnd.isAfter(nightStart);
    }
}
