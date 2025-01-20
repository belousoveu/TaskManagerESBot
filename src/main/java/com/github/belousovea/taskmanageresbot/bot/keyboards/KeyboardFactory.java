package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class KeyboardFactory {

    private final List<BotReplyKeyboard> keyboards;

    public KeyboardFactory(List<BotReplyKeyboard> keyboards) {
        this.keyboards = keyboards;
    }

    public ReplyKeyboardMarkup getKeyboard(Dialog.State state) {
        BotReplyKeyboard keyboard= keyboards.stream().filter(k -> k.getState().equals(state)).findFirst().orElse(null);
        return keyboard != null ? keyboard.getReplyKeyboard() : ReplyKeyboardMarkup.builder().build();
    }
}
