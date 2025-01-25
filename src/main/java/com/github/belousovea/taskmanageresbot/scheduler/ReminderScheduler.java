package com.github.belousovea.taskmanageresbot.scheduler;

import com.github.belousovea.taskmanageresbot.bot.Bot;
import com.github.belousovea.taskmanageresbot.events.AddedNewMemoEvent;
import com.github.belousovea.taskmanageresbot.model.Reminder;
import com.github.belousovea.taskmanageresbot.service.MemoService;
import com.github.belousovea.taskmanageresbot.service.ReminderService;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@Data
public class ReminderScheduler {

    private final PriorityQueue<Reminder> reminders = new PriorityQueue<>();

    private final Bot telegramBot;
    private final TaskScheduler reminderScheduler;
    private final ReminderService reminderService;
    private final MemoService memoService;

    private LocalDateTime currentReminderTime;
    private ScheduledFuture<?> currentReminderFuture;

    @PostConstruct
    public void init() {
        loadRemindersFromDatabase();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void loadRemindersFromDatabase() {
        List<Reminder> reminderList = reminderService.getCurrentDayReminders();
        reminders.clear();
        reminders.addAll(reminderList);
        scheduleNextReminder();
    }

    private void scheduleNextReminder() {
        if (currentReminderFuture != null) {
            currentReminderFuture.cancel(false);
        }
        Reminder currentReminder = reminders.peek();

        if (currentReminder != null) {
            currentReminderTime = currentReminder.getMemo().getReminderTime();
            long delay = LocalDateTime.now().until(currentReminderTime, ChronoUnit.MILLIS);
            if (delay > 0) {
                currentReminderFuture = reminderScheduler
                        .schedule(this::sendReminder, currentReminderTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                sendReminder();
            }
        }
    }

    private void sendReminder() {
        Reminder reminder = reminders.poll();
        if (reminder != null) {
            telegramBot.sendMessage(
                    SendMessage.builder()
                            .chatId(reminder.getUser().getUserId())
                            .text(String.format(Literals.REMINDER_MESSAGE,
                                    reminder.getUser().getUserName(), reminder.getMemo().getReminderText()))
                            .parseMode(Literals.MARKDOWN_MODE)
                            .build());
            log.debug("Sent reminder: {}", reminder);
            if (reminder.getMemo().isRepetitive()) {
                memoService.createNewRepetitiveMemo(reminder.getMemo());
                return;
            }
            scheduleNextReminder();
        }
    }

    @EventListener
    @Profile("!test")
    private void addReminder(AddedNewMemoEvent event) {
        if (event.getMemo().getReminderTime().getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
            log.debug("Added reminder: {}", event);
            reminders.add(reminderService.createReminder(event.getMemo()));
            scheduleNextReminder();
        }
    }


}
