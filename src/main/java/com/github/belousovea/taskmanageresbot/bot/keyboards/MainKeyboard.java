package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.bot.keyboards.buttons.MainKeyboardButton;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@Getter
public class MainKeyboard implements BotReplyKeyboard {

    private final List<MainKeyboardButton> buttons;
    private final ReplyKeyboardMarkup replyKeyboard;
    private final Dialog.State state = Dialog.State.BASIC_STATE;

    public MainKeyboard(List<MainKeyboardButton> buttons) {
        buttons.sort(MainKeyboardButton::compareTo);
        this.buttons = buttons;
        List<KeyboardRow> keyboardRows = buttons
                .stream()
                .map(b -> new KeyboardRow(b.getKeyboardButton()))
                .toList();
        replyKeyboard = new ReplyKeyboardMarkup(keyboardRows);
    }

}
