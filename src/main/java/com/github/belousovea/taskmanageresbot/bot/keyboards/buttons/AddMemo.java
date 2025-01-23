package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AddMemo extends BotKeyboardButton implements MainMenu{

    public AddMemo() {
        super(1, Literals.BUTTON_TITLE_ADD_MEMO);
    }
}
