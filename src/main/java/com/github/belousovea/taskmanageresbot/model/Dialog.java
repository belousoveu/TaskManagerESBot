package com.github.belousovea.taskmanageresbot.model;

import lombok.Data;

@Data
public class Dialog {
    private State currentState;
    long chatId;
    UserDetails user;


    public Dialog(long chatId) {
        this.chatId = chatId;
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
