package com.github.belousovea.taskmanageresbot.bot.keyboards;

import com.github.belousovea.taskmanageresbot.model.Period;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component("setPeriodKeyboard")
@Getter
public class SetPeriodKeyboard implements BotReplyKeyboard {

    private final InlineKeyboardMarkup replyKeyboard;


    public SetPeriodKeyboard() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (Period period : Period.values()) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(period.getText())
                    .callbackData(period.name())
                    .build();
            rows.add(new InlineKeyboardRow(button));
        }
        this.replyKeyboard = InlineKeyboardMarkup.builder().keyboard(rows).build();
    }
}
