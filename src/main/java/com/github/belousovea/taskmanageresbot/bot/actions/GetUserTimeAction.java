package com.github.belousovea.taskmanageresbot.bot.actions;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@Slf4j
public class GetUserTimeAction implements BotAction {

    private String name = "get_user_time";
    private final UserService userService;
    private final KeyboardFactory keyboardFactory;

    public GetUserTimeAction(final UserService userService, KeyboardFactory keyboardFactory) {
        this.userService = userService;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {

        SendMessage.SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder().chatId(dialog.getChatId());

        try {
            String userTimeString = parseTimeFromMessage(update);
            LocalTime userTime = LocalTime.parse(userTimeString, DateTimeFormatter.ofPattern("HH:mm"));
            long offsetInMinutes = Duration.between(LocalTime.now(), userTime).toMinutes();
            dialog.setUser(userService.saveUser(update.getMessage().getFrom(), offsetInMinutes));
            dialog.setCurrentState(Dialog.State.BASIC_STATE);
            return sendMessageBuilder.text("Ок. Я посчитал разницу и буду учитывать ее при напоминаниях")
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState())).build();
        } catch (ElasticsearchException e) {
            log.error(e.getMessage());
            return sendMessageBuilder.text("Не удалось сохранить данные. Попробуйте еще раз")
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState())).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return sendMessageBuilder
                    .text("Я не смог понять, который у вас час. Пришлите еще раз время в формате ЧЧ:ММ")
                    .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState())).build();
        }
    }

    @Override
    public String getName() {
        return "getUserTime";
    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState()== Dialog.State.GET_USER_TIME;
    }

    private String parseTimeFromMessage(Update update) {
        String userMessage = update.getMessage().getText();
        String regex = "\\b(?:[01]?\\d|2[0-3]):[0-5]\\d\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userMessage);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
