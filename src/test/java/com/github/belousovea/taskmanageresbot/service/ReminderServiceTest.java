package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.model.Reminder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private MemoService memoService;
    @InjectMocks
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_getCurrentDayReminders() {

        when(memoService.getCurrentDayMemos(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(FIRST_MEMO, PERIODIC_MEMO, LAST_MEMO));
        when(userService.getUsers(anySet())).thenReturn(List.of(TEST_USER));

        List<Reminder> expected = List.of(new Reminder(FIRST_MEMO, TEST_USER),
                new Reminder(PERIODIC_MEMO, TEST_USER),
                new Reminder(LAST_MEMO, TEST_USER));

        List<Reminder> actual = reminderService.getCurrentDayReminders();

        assertNotNull(actual);
        assertIterableEquals(expected, actual);
    }

    @Test
    void test_createReminder() {
        when(userService.getUser(anyLong())).thenReturn(TEST_USER);
        Reminder expected = new Reminder(FIRST_MEMO, TEST_USER);
        Reminder actual = reminderService.createReminder(FIRST_MEMO);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}