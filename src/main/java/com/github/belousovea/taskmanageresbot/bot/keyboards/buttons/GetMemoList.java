package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GetMemoList extends MainKeyboardButton {

    public GetMemoList() {
        super(3, "\uD83D\uDDD2 Список напоминаний");
    }
}
