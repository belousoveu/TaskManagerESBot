package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.bot.keyboards.buttons.BotKeyboardButton;
import com.github.belousovea.taskmanageresbot.bot.keyboards.buttons.MainMenu;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component("mainKeyboard")
@Getter
public class MainKeyboard implements BotReplyKeyboard {

    private final List<MainMenu> buttons;
    private final ReplyKeyboardMarkup replyKeyboard;

    public MainKeyboard(List<MainMenu> buttons) {
        this.buttons = buttons.stream()
                .sorted((a, b) -> ((BotKeyboardButton) a).compareTo(((BotKeyboardButton) b))).toList();
        List<KeyboardRow> keyboardRows = buttons
                .stream()
                .map(b -> new KeyboardRow(((BotKeyboardButton) b).getKeyboardButton()))
                .toList();
        replyKeyboard = ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

}
