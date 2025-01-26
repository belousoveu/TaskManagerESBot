package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.bot.actions.BotAction;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Getter
public class DialogManager {

    private final Map<Long, Dialog> dialogs = new HashMap<>();
    private final List<BotAction> actions;
    private final UserService userService;
    private final BotAction defaultAction;

    public DialogManager(List<BotAction> actions, UserService userService,
                         @Qualifier("unknownAction") BotAction defaultAction) {
        this.actions = actions;
        this.userService = userService;
        this.defaultAction = defaultAction;
        log.debug("Actions loaded: {}", actions.size());
    }

    public SendMessage getAction(Update update) {
        if (update == null) {
            throw new NullPointerException("Update is null");
        }
        Dialog dialog = getDialog(update);

        for (BotAction action : actions) {
            if (action.isApplicable(dialog, update)) {
                return action.replyMessage(dialog, update);
            }
        }

        return defaultAction.replyMessage(dialog, update);
    }

    private Dialog getDialog(Update update) {
        long chatId = getChatId(update);
        long userId = getUserId(update);
        User user = userService.getUser(userId);

        return dialogs.computeIfAbsent(chatId, k -> new Dialog(chatId, user));
    }

    private long getUserId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No user id");
    }

    private long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No chat id");
    }
}
