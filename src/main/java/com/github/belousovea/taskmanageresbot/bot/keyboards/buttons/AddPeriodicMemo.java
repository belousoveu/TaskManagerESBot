package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AddPeriodicMemo extends MainKeyboardButton {

    public AddPeriodicMemo() {
        super(2, "➕ Добавить периодическое напоминание");
    }
}
