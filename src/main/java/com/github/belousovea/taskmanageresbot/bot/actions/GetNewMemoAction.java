package com.github.belousovea.taskmanageresbot.bot.actions;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.exception.IllegalTimeReminderException;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@Slf4j
public class GetNewMemoAction implements BotAction {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final KeyboardFactory keyboardFactory;


    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        SendMessage.SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder().chatId(dialog.getChatId());

        try {
            Memo newMemo = parseMemoFromMessage(update.getMessage().getText());
            LocalDateTime userReminderTime = newMemo.getReminderTime();
            newMemo.setReminderTime(newMemo.getReminderTime().plusMinutes(dialog.getUser().getTimeOffset()));
            if (newMemo.getReminderTime().isBefore(LocalDateTime.now())) {
                throw new IllegalTimeReminderException(userReminderTime);
            }
            newMemo.setUserId(dialog.getUser().getUserId());
            dialog.setTempMemo(newMemo);
            dialog.setCurrentState(Dialog.State.GET_PERIOD);
            return sendMessageBuilder.text(String.format(Literals.GET_NEW_MEMO_MESSAGE,
                            userReminderTime.format(dateTimeFormatter), newMemo.getReminderText()))
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
        } catch (Exception e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text(Literals.ERROR_MEMO_FORMAT_MESSAGE)
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        }
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState() == Dialog.State.GET_NEW_MEMO
                && update.hasMessage()
                && update.getMessage().hasText()
                && !update.getMessage().getText().equals(Literals.BUTTON_TITLE_CANCEL);
    }

    private Memo parseMemoFromMessage(String text) {

        Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}) (.*)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            return Memo.builder()
                    .reminderTime(LocalDateTime.parse(matcher.group(1), dateTimeFormatter))
                    .reminderText(matcher.group(2))
                    .build();
        }
        throw new IllegalArgumentException(String.format("Строка %s не соответствует формату", text));
    }
}
