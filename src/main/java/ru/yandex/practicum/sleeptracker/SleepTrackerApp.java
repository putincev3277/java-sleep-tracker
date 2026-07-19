package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.ROOT);

    // Храним все аналитические функции в одном списке
    private static final List<Function<List<SleepSession>, SleepAnalysisResult>> ANALYSIS_FUNCTIONS = List.of(
            new TotalSessionsCountFunction(),
            new MinDurationFunction(),
            new MaxDurationFunction(),
            new AvgDurationFunction(),
            new BadQualityCountFunction(),
            new SleeplessNightsFunction(),
            new SleepTypeFunction()
            // Сюда легко добавить новые функции, например:

    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Ошибка: не указан путь к файлу лога сна.");
            System.err.println("Использование: SleepTrackerApp sleep_log.txt");
            return;
        }

        String filePath = args[0];

        List<SleepSession> records;
        try {
            records = loadSleepLog(filePath);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка формата данных в файле: " + e.getMessage());
            return;
        }

        if (records.isEmpty()) {
            System.out.println("Файл пуст или не содержит корректных записей.");
            return;
        }

        System.out.println("Загружено записей: " + records.size());
        System.out.println();

        // ЕДИНСТВЕННОЕ место вывода: в main перебираем все функции
        ANALYSIS_FUNCTIONS.forEach(func -> System.out.println(func.apply(records)));

    }

    private static List<SleepSession> loadSleepLog(String filePath) throws IOException {
        List<SleepSession> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                SleepSession record = parseSleepSession(line, lineNumber);
                result.add(record);
            }
        }
        return result;
    }

    private static SleepSession parseSleepSession(String line, int lineNumber) {
        String[] parts = line.split(";", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Неверный формат строки на строке " + lineNumber + ": \"" + line + "\""
            );
        }

        String sleepStartStr = parts[0].trim();
        String sleepEndStr = parts[1].trim();
        String qualityStr = parts[2].trim().toUpperCase();

        LocalDateTime sleepStart, sleepEnd;
        try {
            sleepStart = LocalDateTime.parse(sleepStartStr, FORMATTER);
            sleepEnd = LocalDateTime.parse(sleepEndStr, FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Ошибка парсинга даты/времени на строке " + lineNumber, e
            );
        }

        SleepQuality quality;
        try {
            quality = SleepQuality.valueOf(qualityStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Недопустимое значение качества сна на строке " + lineNumber +
                            ": \"" + qualityStr + "\". Допустимые значения: GOOD, NORMAL, BAD."
            );
        }

        if (!sleepEnd.isAfter(sleepStart)) {
            throw new IllegalArgumentException(
                    "Время пробуждения не может быть раньше или равно времени засыпания на строке " + lineNumber
            );
        }

        return new SleepSession(sleepStart, sleepEnd, quality);
    }
}
