package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

@Getter
public abstract class MainKeyboardButton {

    private final int order;
    private final KeyboardButton keyboardButton;

    public MainKeyboardButton(int order, String keyboardTitleText) {
        this.order = order;
        this.keyboardButton = new KeyboardButton(keyboardTitleText);
    }

    public int compareTo(MainKeyboardButton mainKeyboardButton) {
        return this.order - mainKeyboardButton.order;
    }
}
