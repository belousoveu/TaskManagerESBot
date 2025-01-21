package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GetCalendar extends BotKeyboardButton implements MainMenu{

    public GetCalendar() {
        super(4, "\uD83D\uDCC6 Календарь");
    }
}
