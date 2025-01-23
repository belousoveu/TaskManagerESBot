package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GetCalendar extends BotKeyboardButton implements MainMenu{

    public GetCalendar() {
        super(4, Literals.BUTTON_TITLE_CALENDAR);
    }
}
