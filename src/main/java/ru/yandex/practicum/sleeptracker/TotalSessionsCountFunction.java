package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class TotalSessionsCountFunction implements Function<List<SleepSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepSession> records) {
        // Для List размер хранится внутри — это O(1), а stream().count() — лишняя обёртка
        long count = records.size();
        return new SleepAnalysisResult("Количество сессий сна", String.valueOf(count));
    }
}
