package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class SleepSessionsOrdering {

    private SleepSessionsOrdering() {
    }


    public static List<SleepSession> sortedByStart(List<SleepSession> sessions) {
        if (sessions == null) {
            return List.of();
        }
        return sessions.stream()
                .sorted(Comparator.comparing(SleepSession::getSleepStart))
                .collect(Collectors.toList());
    }
}