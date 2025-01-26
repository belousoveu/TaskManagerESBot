package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.bot.keyboards.buttons.BotKeyboardButton;
import com.github.belousovea.taskmanageresbot.bot.keyboards.buttons.CancelMenu;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component("cancelKeyboard")
@Getter
public class CancelKeyboard implements BotReplyKeyboard {

    private final List<CancelMenu> buttons;
    private final ReplyKeyboardMarkup replyKeyboard;

    public CancelKeyboard(List<CancelMenu> buttons) {
        this.buttons = buttons.stream()
                .sorted((a, b) -> ((BotKeyboardButton) a).compareTo(((BotKeyboardButton) b))).toList();
        List<KeyboardRow> keyboardRows = buttons
                .stream()
                .map(b -> new KeyboardRow(((BotKeyboardButton) b).getKeyboardButton()))
                .toList();
        replyKeyboard = ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

}
