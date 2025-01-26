package com.github.belousovea.taskmanageresbot.events;

import com.github.belousovea.taskmanageresbot.model.Memo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddedNewMemoEvent extends ApplicationEvent {
    private final Memo memo;

    public AddedNewMemoEvent(Object source, Memo memo) {
        super(source);
        this.memo = memo;
    }

}
