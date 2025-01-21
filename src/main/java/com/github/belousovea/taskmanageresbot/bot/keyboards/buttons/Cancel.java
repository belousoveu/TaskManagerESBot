package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Cancel extends BotKeyboardButton implements CancelMenu {

    public Cancel() {
        super(10, "Возврат в главное меню");
    }
}
