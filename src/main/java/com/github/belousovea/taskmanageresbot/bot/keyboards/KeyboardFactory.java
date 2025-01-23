package com.github.belousovea.taskmanageresbot.bot.keyboards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.EnumMap;
import java.util.Map;

import static com.github.belousovea.taskmanageresbot.model.Dialog.State;

@Component
public class KeyboardFactory {

    private final Map<State, BotReplyKeyboard> keyboards;

    @Autowired
    public KeyboardFactory(@Qualifier("mainKeyboard") BotReplyKeyboard mainKeyboard,
                           @Qualifier("cancelKeyboard") BotReplyKeyboard cancelKeyboard,
                           @Qualifier("emptyKeyboard") BotReplyKeyboard emptyKeyboard) {
        this.keyboards = new EnumMap<>(State.class);
        keyboards.put(State.BASIC_STATE, mainKeyboard);
        keyboards.put(State.GET_NEW_MEMO, cancelKeyboard);
        keyboards.put(State.GET_NEW_PERIODIC_MEMO, cancelKeyboard);
        keyboards.put(State.MEMO_LIST, cancelKeyboard);
        keyboards.put(State.TIME_SETUP, emptyKeyboard);
        keyboards.put(State.CALENDAR, cancelKeyboard);
        keyboards.put(State.GET_USER_TIME, emptyKeyboard);

    }

    public ReplyKeyboardMarkup getKeyboard(State state) {
        return keyboards.get(state).getReplyKeyboard();
    }
}
