package com.github.belousovea.taskmanageresbot.bot.actions;


import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotAction {

    SendMessage replyMessage(Dialog dialog, Update update);

    boolean isApplicable(Dialog dialog, Update update);
}
