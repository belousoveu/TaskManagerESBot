package com.github.belousovea.taskmanageresbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Reminder implements Comparable<Reminder> {

    private Memo memo;
    private User user;

    @Override
    public int compareTo(@NotNull Reminder other) {
        return this.getMemo().getReminderTime().compareTo(other.getMemo().getReminderTime());
    }
}
