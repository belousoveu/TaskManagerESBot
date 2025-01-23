package com.github.belousovea.taskmanageresbot.model;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum Period {
    ONE_TIME(Literals.PERIOD_ONE_TIME_TEXT),
    MINUTE(Literals.PERIOD_MINUTE_TEXT),
    HOUR(Literals.PERIOD_HOUR_TEXT),
    DAY(Literals.PERIOD_DAY_TEXT),
    WEEK(Literals.PERIOD_WEEK_TEXT),
    MONTH(Literals.PERIOD_MONTH_TEXT),
    YEAR(Literals.PERIOD_YEAR_TEXT);

    private final String text;

    Period(String text) {
        this.text = text;
    }

    public LocalDateTime getNextEventTime(@NotNull LocalDateTime reminderTime) {
        return switch (this) {
            case ONE_TIME -> null;
            case MINUTE -> reminderTime.plusMinutes(1);
            case HOUR -> reminderTime.plusHours(1);
            case DAY -> reminderTime.plusDays(1);
            case WEEK -> reminderTime.plusWeeks(1);
            case MONTH -> reminderTime.plusMonths(1);
            case YEAR -> reminderTime.plusYears(1);
        };
    }
}
