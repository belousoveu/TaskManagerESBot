package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.model.Dialog;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class StartAction implements BotAction {

    private String name = "start";
//    private final UserService userService;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {


        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(String.format("Привет, %s! Я храню для тебя напоминания", dialog.getUser().getUserName()))
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {

        return dialog.getCurrentState() == Dialog.State.BASIC_STATE
                && update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().toLowerCase().startsWith("/start");
    }
}
