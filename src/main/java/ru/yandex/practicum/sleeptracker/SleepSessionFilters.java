package ru.yandex.practicum.sleeptracker;

public class SleepSessionFilters {

    /**
     * Проверяет, что сессия полная: не null и оба времени заданы.
     */
    public static boolean isComplete(SleepSession session) {
        return session != null
                && session.getSleepStart() != null
                && session.getSleepEnd() != null;
    }
}
