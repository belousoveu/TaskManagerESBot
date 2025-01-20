package com.github.belousovea.taskmanageresbot.model;

import lombok.Data;

@Data
public class Dialog {
    private State currentState;
    long chatId;
    User user;


    public Dialog(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
        if (user == null) {
            currentState = State.TIME_SETUP;

        } else {
            currentState = State.BASIC_STATE;
        }
    }

    public enum State {
        START,
        TIME_SETUP,
        GET_USER_TIME,
        BASIC_STATE,
        ADD_NEW_MEMO,
        ADD_NEW_PERIODIC_MEMO,
        MEMO_LIST,
        CALENDAR

    }
}
