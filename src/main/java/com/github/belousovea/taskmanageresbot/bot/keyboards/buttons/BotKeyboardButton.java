package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

@Getter
public abstract class BotKeyboardButton implements Comparable<BotKeyboardButton> {

    private final int order;
    private final KeyboardButton keyboardButton;

    public BotKeyboardButton(int order, String keyboardTitleText) {
        this.order = order;
        this.keyboardButton = new KeyboardButton(keyboardTitleText);
    }

    @Override
    public int compareTo(BotKeyboardButton mainKeyboardButton) {
        return this.order - mainKeyboardButton.order;
    }
}
