package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AddPeriodicMemo extends BotKeyboardButton implements MainMenu{

    public AddPeriodicMemo() {
        super(2, Literals.BUTTON_TITLE_ADD_PERIODIC_MEMO);
    }
}
