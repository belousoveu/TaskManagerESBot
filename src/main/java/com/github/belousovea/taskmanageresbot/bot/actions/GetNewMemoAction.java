package com.github.belousovea.taskmanageresbot.bot.actions;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.exception.IllegalTimeReminderException;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.service.MemoService;
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
public class GetNewMemoAction implements BotAction{
    private String name = "new_memo";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final KeyboardFactory keyboardFactory;
    private final MemoService memoService;


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
            memoService.save(newMemo);
            dialog.setCurrentState(Dialog.State.BASIC_STATE);
            return sendMessageBuilder.text(String.format("""
                                    Добавлено новое напоминание:
                                    Дата и время: %s
                                    Текст: %s
                                    Периодичность: разовое""",
                            newMemo.getReminderTime().format(dateTimeFormatter), newMemo.getReminderText()))
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        } catch (IllegalTimeReminderException e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text(String.format("Указанное время %s уже прошло\n" +
                            "Я не смогу напомнить об уже прошедшем событии. Попробуйте еще раз",
                            e.getUserReminderTime().format(dateTimeFormatter)))
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        } catch (ElasticsearchException e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text("Ошибка при записи в базу данных. Попробуйте еще раз")
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text("Данные отсутствуют или не соответствуют формату")
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                    .build();
        }
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState() == Dialog.State.ADD_NEW_MEMO;
    }

    private Memo parseMemoFromMessage(String text) {

        Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}) (.*)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            Memo memo = new Memo();
            memo.setReminderTime(LocalDateTime.parse(matcher.group(1), dateTimeFormatter));
            memo.setReminderText(matcher.group(2));
            memo.setPeriodicityMinutes(0);
            return memo;
        }
        throw new IllegalArgumentException(String.format("Строка %s не соответствует формату",text));
    }
}
