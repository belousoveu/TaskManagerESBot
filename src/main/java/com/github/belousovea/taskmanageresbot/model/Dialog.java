package com.github.belousovea.taskmanageresbot.model;

import lombok.Data;

@Data
public class Dialog {
    private State currentState;
    long chatId;
    User user;
    Memo tempMemo;


    public Dialog(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
        if (user == null) {
            currentState = State.TIME_SETUP;

        } else {
            currentState = State.BASIC_STATE;
        }
    }

    public void cleanTempMemo() {
        tempMemo = null;
    }


    public enum State {
        TIME_SETUP,
        GET_USER_TIME,
        BASIC_STATE,
        GET_NEW_MEMO,
        GET_PERIOD,
        INVALID_STATE

    }
}
