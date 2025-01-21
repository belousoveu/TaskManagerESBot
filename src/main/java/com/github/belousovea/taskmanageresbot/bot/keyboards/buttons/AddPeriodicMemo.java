package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AddPeriodicMemo extends BotKeyboardButton implements MainMenu{

    public AddPeriodicMemo() {
        super(2, "➕ Добавить периодическое напоминание");
    }
}
