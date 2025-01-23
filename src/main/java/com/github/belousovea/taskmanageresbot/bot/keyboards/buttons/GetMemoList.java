package com.github.belousovea.taskmanageresbot.bot.keyboards.buttons;

import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GetMemoList extends BotKeyboardButton implements MainMenu {

    public GetMemoList() {
        super(3, Literals.BUTTON_TITLE_MEMO_LIST);
    }
}
