package com.github.belousovea.taskmanageresbot.bot.keyboards;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component(value = "emptyKeyboard")
@Getter
public class EmptyKeyboard implements BotReplyKeyboard {

    private final ReplyKeyboardMarkup replyKeyboard;

    public EmptyKeyboard() {
        replyKeyboard = ReplyKeyboardMarkup.builder().build();
    }

}
