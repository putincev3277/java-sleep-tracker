package ru.yandex.practicum.sleeptracker;

import java.util.Objects;

public class SleepAnalysisResult {
    private final String description;
    private final Object value;

    public SleepAnalysisResult(String description, Object value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SleepAnalysisResult that)) return false;

        if (!description.equals(that.description)) return false;
        return Objects.equals(this.value, that.value);
    }


    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + java.util.Objects.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        return description + ": " + value;
    }
}
