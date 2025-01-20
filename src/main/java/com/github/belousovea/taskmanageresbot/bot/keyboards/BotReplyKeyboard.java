package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface BotReplyKeyboard {
    Dialog.State getState();

    ReplyKeyboardMarkup getReplyKeyboard();
}
