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
public class AddMemoAction implements BotAction {
    private String name = "memo";
    private final KeyboardFactory keyboardFactory;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        dialog.setCurrentState(Dialog.State.GET_NEW_MEMO);
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(Literals.ADD_MEMO_MESSAGE)
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .parseMode(Literals.MARKDOWN_MODE)
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState() == Dialog.State.BASIC_STATE
                && update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals(Literals.BUTTON_TITLE_ADD_MEMO);
    }
}
