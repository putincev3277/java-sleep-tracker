package ru.yandex.practicum.sleeptracker;

/**
 * Тип суточной активности пользователя на основе паттернов сна.
 * OWL — Сова
 * LARK — Жаворонок
 * DOVE — Голубь
 */
enum UserSleepType {
    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    private final String displayName;

    UserSleepType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает понятное название типа для вывода пользователю.
     */
    public String getDisplayName() {
        return displayName;
    }
}
