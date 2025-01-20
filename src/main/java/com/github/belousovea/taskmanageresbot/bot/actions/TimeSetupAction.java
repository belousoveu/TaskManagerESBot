package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Data
public class TimeSetupAction implements BotAction {
    private String name = "TimeSetup";
    private final KeyboardFactory keyboardFactory;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        dialog.setCurrentState(Dialog.State.GET_USER_TIME);
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(String.format("""
                                Приветствую, %s!
                                Давай сверим часы. У меня сейчас : %s
                                Напиши, какое время показывают твои часы в формате HH:MM""",
                        update.getMessage().getFrom().getUserName(), LocalTime.now().format(formatter)))
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState()==Dialog.State.TIME_SETUP;
    }
}
