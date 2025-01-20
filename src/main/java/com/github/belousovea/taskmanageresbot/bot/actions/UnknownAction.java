package com.github.belousovea.taskmanageresbot.bot.actions;


import com.github.belousovea.taskmanageresbot.model.Dialog;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class UnknownAction implements BotAction {
    private String name = "unknown";

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text("Unknown action")
                .build();
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return false;
    }
}
