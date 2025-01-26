package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class StartAction implements BotAction {

    private final KeyboardFactory keyboardFactory;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {


        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(String.format(Literals.COMMAND_START_MESSAGE, dialog.getUser().getUserName()))
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .parseMode(Literals.MARKDOWN_MODE)
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {

        return dialog.getCurrentState() == Dialog.State.BASIC_STATE
                && update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().toLowerCase().startsWith(Literals.COMMAND_START);
    }
}
