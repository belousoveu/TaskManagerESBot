package com.github.belousovea.taskmanageresbot.bot.commands;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class HelpCommand extends MainMenuCommand {
    public HelpCommand() {
        super(2, new BotCommand(Literals.COMMAND_HELP, Literals.COMMAND_HELP_DESCRIPTION));
    }
}
