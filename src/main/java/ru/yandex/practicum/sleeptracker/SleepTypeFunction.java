package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class SleepTypeFunction implements Function<List<SleepSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult("Тип пользователя", UserSleepType.DOVE.getDisplayName());
        }

        Map<UserSleepType, Integer> counts = sessions.stream()
                .filter(SleepSessionFilters::isComplete)
                .filter(this::isNightSession)      // <-- Теперь фильтр работает корректно
                .map(this::determineNightType)
                .collect(groupingBy(Function.identity(), summingInt(x -> 1)));

        UserSleepType resultType = determineDominantType(counts);
        return new SleepAnalysisResult("Тип пользователя", resultType.getDisplayName());
    }

    // ИСПРАВЛЕННЫЙ МЕТОД
    private boolean isNightSession(SleepSession session) {
        LocalDateTime start = session.getSleepStart();
        LocalDateTime end = session.getSleepEnd();

        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();

        // Логика: сон ночной, если начался поздно (после 18:00) ИЛИ закончился рано (до 10:00)
        boolean startedLate = startTime.isAfter(LocalTime.of(18, 0));
        boolean endedEarly = endTime.isBefore(LocalTime.of(10, 0));

        return startedLate || endedEarly;
    }

    private UserSleepType determineNightType(SleepSession session) {
        LocalTime startTime = session.getSleepStart().toLocalTime();
        LocalTime endTime = session.getSleepEnd().toLocalTime();

        boolean isLateSleep = startTime.isAfter(LocalTime.of(23, 0));
        boolean isLateWake = endTime.isAfter(LocalTime.of(9, 0));

        boolean isEarlySleep = startTime.isBefore(LocalTime.of(22, 0));
        boolean isEarlyWake = endTime.isBefore(LocalTime.of(7, 0));

        if (isLateSleep && isLateWake) {
            return UserSleepType.OWL;
        } else if (isEarlySleep && isEarlyWake) {
            return UserSleepType.LARK;
        } else {
            return UserSleepType.DOVE;
        }
    }

    private UserSleepType determineDominantType(Map<UserSleepType, Integer> counts) {
        int owlCount = counts.getOrDefault(UserSleepType.OWL, 0);
        int larkCount = counts.getOrDefault(UserSleepType.LARK, 0);
        int doveCount = counts.getOrDefault(UserSleepType.DOVE, 0);

        int max = Math.max(owlCount, Math.max(larkCount, doveCount));

        int ties = 0;
        if (owlCount == max) ties++;
        if (larkCount == max) ties++;
        if (doveCount == max) ties++;

        if (ties > 1) {
            return UserSleepType.DOVE;
        }

        if (owlCount == max) return UserSleepType.OWL;
        if (larkCount == max) return UserSleepType.LARK;

        return UserSleepType.DOVE;
    }
}
