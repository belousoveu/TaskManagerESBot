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
public class AboutAction implements BotAction {
    private final KeyboardFactory keyboardFactory;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(Literals.COMMAND_ABOUT_MESSAGE)
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .build();
    }


    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().toLowerCase().startsWith(Literals.COMMAND_ABOUT);
    }
}
