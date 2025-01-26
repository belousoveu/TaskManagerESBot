package com.github.belousovea.taskmanageresbot.bot.commands;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class AboutCommand extends MainMenuCommand {
    public AboutCommand() {
        super(3, new BotCommand(Literals.COMMAND_ABOUT, Literals.COMMAND_ABOUT_DESCRIPTION));
    }
}
