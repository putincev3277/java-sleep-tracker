package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

/**
 * Модель одной записи о сне.
 */
public class SleepSession {
    private final LocalDateTime sleepStart;
    private final LocalDateTime sleepEnd;
    private final SleepQuality quality;

    public SleepSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality quality) {

        if (sleepStart != null && sleepEnd != null) {
            if (sleepEnd.isBefore(sleepStart)) {
                throw new IllegalArgumentException(
                        "Время пробуждения не может быть раньше времени засыпания: " +
                                sleepStart + " -> " + sleepEnd
                );
            }
        }

        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}

