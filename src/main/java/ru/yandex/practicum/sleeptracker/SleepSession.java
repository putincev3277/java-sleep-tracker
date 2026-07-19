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
        if (sleepStart == null || sleepEnd == null) {
            throw new IllegalArgumentException("Время начала и конца не могут быть null");
        }
        if (sleepEnd.isBefore(sleepStart)) {
            // Это покрывает и случай «заснул в 23:00, проснулся в 05:00» — он НЕ будет отклонён,
            // потому что 05:00 следующего дня > 23:00 текущего.
            throw new IllegalArgumentException(
                    "Время пробуждения не может быть раньше времени засыпания: " +
                            sleepStart + " -> " + sleepEnd
            );
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

