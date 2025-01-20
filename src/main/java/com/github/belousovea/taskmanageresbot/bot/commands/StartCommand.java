package com.github.belousovea.taskmanageresbot.bot.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class StartCommand extends MainMenuCommand {
    public StartCommand() {
        super(1, new BotCommand("start", "Начало работы с ботом"));
    }
}
