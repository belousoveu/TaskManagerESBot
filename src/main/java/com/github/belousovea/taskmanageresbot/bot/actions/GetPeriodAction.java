package com.github.belousovea.taskmanageresbot.bot.actions;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.exception.IllegalTimeReminderException;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.Period;
import com.github.belousovea.taskmanageresbot.service.MemoService;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@Data
public class GetPeriodAction implements BotAction {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final KeyboardFactory keyboardFactory;
    private final MemoService memoService;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        SendMessage.SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder().chatId(dialog.getChatId());

        Memo newMemo = dialog.getTempMemo();
        LocalDateTime userReminderTime = newMemo.getReminderTime();
        Period newMemoPeriod = Period.valueOf(update.getCallbackQuery().getData());
        newMemo.setPeriod(newMemoPeriod.name());
        try {
            if (newMemo.getReminderTime().isBefore(LocalDateTime.now())) {
                throw new IllegalTimeReminderException(userReminderTime.minusMinutes(dialog.getUser().getTimeOffset()));
            }
            memoService.save(newMemo);
            dialog.setCurrentState(Dialog.State.BASIC_STATE);
            dialog.setTempMemo(null);
            return sendMessageBuilder.text(String.format(Literals.GET_PERIOD_MESSAGE,
                            userReminderTime.minusMinutes(dialog.getUser().getTimeOffset()).format(dateTimeFormatter),
                            newMemo.getReminderText(),
                            newMemoPeriod.getText()))
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .parseMode(Literals.MARKDOWN_MODE)
                    .build();
        } catch (IllegalTimeReminderException e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text(String.format(Literals.INVALID_TIME_REMINDER_MESSAGE,
                            e.getUserReminderTime().format(dateTimeFormatter)))
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        } catch (ElasticsearchException e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text(Literals.ERROR_DATABASE_MESSAGE)
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        }

    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState() == Dialog.State.GET_PERIOD
                && update.hasCallbackQuery();
    }
}

