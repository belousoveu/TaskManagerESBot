package com.github.belousovea.taskmanageresbot.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IllegalTimeReminderException extends RuntimeException {

    private final LocalDateTime userReminderTime;

    public IllegalTimeReminderException(LocalDateTime localDateTime) {

        super(String.format("The specified time %s has already passed", localDateTime));
        this.userReminderTime = localDateTime;
    }
}
