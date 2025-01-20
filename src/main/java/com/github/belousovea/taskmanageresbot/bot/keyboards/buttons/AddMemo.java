package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AddMemo extends MainKeyboardButton {

    public AddMemo() {
        super(1, "➕ Добавить напоминание");
    }
}
