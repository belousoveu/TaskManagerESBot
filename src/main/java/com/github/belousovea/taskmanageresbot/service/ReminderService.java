package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.exception.NotFoundUserException;
import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.Reminder;
import com.github.belousovea.taskmanageresbot.model.User;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Data
public class ReminderService {

    private final UserService userService;
    private final MemoService memoService;


    public List<Reminder> getCurrentDayReminders() {
        LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = startTime.plusDays(1);
        List<Memo> memos = memoService.getCurrentDayMemos(startTime, endTime);
        Set<Long> userIdSet = memos.stream().map(Memo::getUserId).collect(java.util.stream.Collectors.toSet());
        List<User> users = userService.getUsers(userIdSet);
        return memos.stream()
                .filter(m -> m.getReminderTime().isAfter(LocalDateTime.now().withSecond(0)))
                .map(m -> new Reminder(m, users.stream()
                        .filter(u -> u.getUserId() == m.getUserId()).findFirst()
                        .orElseThrow(() -> new NotFoundUserException(m.getUserId()))))
                .collect(java.util.stream.Collectors.toList());

    }

    public Reminder createReminder(Memo memo) {
        User user = userService.getUser(memo.getUserId());
        return new Reminder(memo, user);
    }
}
