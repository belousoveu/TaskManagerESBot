package com.github.belousovea.taskmanageresbot.bot.commands;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Getter
public abstract class MainMenuCommand {
    private final int order;
    private final BotCommand command;

    public MainMenuCommand(int order, BotCommand command) {
        this.order = order;
        this.command = command;
    }

    public int compareTo(MainMenuCommand command) {
        return this.order - command.order;
    }
}
