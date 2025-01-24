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
public class CancelButtonAction implements BotAction {
    private final String name = "cancel";
    private final KeyboardFactory keyboardFactory;


    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        dialog.setCurrentState(Dialog.State.BASIC_STATE);
        dialog.cleanTempMemo();
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(Literals.CANCEL_MESSAGE)
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().equals(Literals.BUTTON_TITLE_CANCEL);
    }
}
