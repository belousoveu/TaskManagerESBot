package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Cancel extends BotKeyboardButton implements CancelMenu {

    public Cancel() {
        super(10, Literals.BUTTON_TITLE_CANCEL);
    }
}
