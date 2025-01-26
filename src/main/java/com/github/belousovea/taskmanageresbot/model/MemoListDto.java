package com.github.belousovea.taskmanageresbot.model;

import lombok.Data;

import java.util.List;

@Data
public class MemoListDto {

    private User user;
    private int numberOfMemos;
    private int numberOfPeriodicMemos;
    private String NearestMemoTime;
    private List<Memo> memos;

}
