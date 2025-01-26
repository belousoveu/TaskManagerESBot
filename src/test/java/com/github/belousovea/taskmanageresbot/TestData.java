package com.github.belousovea.taskmanageresbot;

import com.github.belousovea.taskmanageresbot.model.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class TestData {

    public final static User TEST_USER = User.builder().userId(1L).userName("testUser").firstName("testFirstName").build();
    public final static User NEW_USER = User.builder().userId(2L).userName("newUser").firstName("testFirstName").build();

    public final static SendMessage TEST_MESSAGE = SendMessage.builder().chatId(1L).text("test").build();

    public final static String TEST_CORRECT_MEMO_ANSWER = dateTimeString(LocalDateTime.now().plusMinutes(5L)) + " reminder text";
    public final static String TEST_INVALID_MEMO_ANSWER = "Any invalid text";
    public final static String TEST_PAST_TIME_MEMO_ANSWER = dateTimeString(LocalDateTime.now().minusMinutes(5L)) + " reminder text";


    private static String dateTimeString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    public final static ReplyKeyboard TEST_KEYBOARD = ReplyKeyboardMarkup.builder().build();

    public final static Memo PAST_MEMO = Memo.builder()
            .reminderTime(LocalDateTime.now().minusMinutes(5L))
            .reminderText("past reminder")
            .userId(TEST_USER.getUserId())
            .period(Period.ONE_TIME.name())
            .build();

    public final static Memo FIRST_MEMO = Memo.builder()
            .reminderTime(LocalDateTime.now().plusMinutes(5L))
            .reminderText("first reminder")
            .userId(TEST_USER.getUserId())
            .period(Period.ONE_TIME.name())
            .build();

    public final static Memo PERIODIC_MEMO = Memo.builder()
            .reminderTime(LocalDateTime.now().plusMinutes(10L))
            .reminderText("periodic reminder")
            .userId(TEST_USER.getUserId())
            .period(Period.DAY.name())
            .build();

    public final static Memo LAST_MEMO = Memo.builder()
            .reminderTime(LocalDateTime.now().plusMinutes(15L))
            .reminderText("last reminder")
            .userId(TEST_USER.getUserId())
            .period(Period.ONE_TIME.name())
            .build();

    public static Memo mockMemo(String text) {
        return Memo.builder()
                .reminderTime(LocalDateTime.now())
                .reminderText(text)
                .userId(TEST_USER.getUserId())
                .period(Period.ONE_TIME.name())
                .build();
    }

    public static Memo mockDailyMemo(String text) {
        return Memo.builder()
                .reminderTime(LocalDateTime.now())
                .reminderText(text)
                .userId(TEST_USER.getUserId())
                .period(Period.DAY.name())
                .build();
    }

    public static Dialog mockDialog(Dialog.State state) {
        Dialog dialog = new Dialog(TEST_USER.getUserId(), TEST_USER);
        dialog.setCurrentState(state);
        return dialog;
    }

    public static MockUpdate mockUpdate() {
        return new MockUpdate();
    }

    public static org.telegram.telegrambots.meta.api.objects.User getMockTelegramUser() {
        return org.telegram.telegrambots.meta.api.objects.User.builder()
                .id(TEST_USER.getUserId())
                .userName(TEST_USER.getUserName())
                .firstName(TEST_USER.getFirstName())
                .isBot(false)
                .build();
    }

    public static MemoListDto mockMemoListDto(User user, List<Memo> memos) {
        MemoListDto dto = new MemoListDto();
        dto.setUser(user);
        dto.setMemos(memos);
        if (!memos.isEmpty()) {
            dto.setNumberOfPeriodicMemos((int) memos.stream().filter(Memo::isRepetitive).count());
            dto.setNumberOfMemos(memos.size() - dto.getNumberOfPeriodicMemos());
            dto.setNearestMemoTime(memos.stream().min(Comparator.comparing(Memo::getReminderTime)).get().getReminderTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        return dto;
    }

    public static CallbackQuery mockCallbackQuery(String text) {
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(getMockTelegramUser());
        callbackQuery.setData(text);
        return callbackQuery;
    }

    public static List<Memo> getTestMemos() {
        return List.of(PAST_MEMO, FIRST_MEMO, PERIODIC_MEMO, LAST_MEMO);
    }

    public static class MockUpdate {
        private final Update update;
        private String messageText;

        public MockUpdate() {
            this.update = new Update();
            this.update.setUpdateId(1);
        }

        public MockUpdate user(User user) {
            org.telegram.telegrambots.meta.api.objects.User telegramUser =
                    org.telegram.telegrambots.meta.api.objects.User.builder()
                            .id(user.getUserId())
                            .userName(user.getUserName())
                            .firstName(user.getFirstName())
                            .isBot(false)
                            .build();
            this.update.setMessage(Message.builder()
                    .messageId(1)
                    .chat(Chat.builder().id(user.getUserId()).type("private").build())
                    .text("text")
                    .from(telegramUser)
                    .build());

            CallbackQuery callbackQuery = new CallbackQuery();
            callbackQuery.setFrom(telegramUser);

            update.setCallbackQuery(callbackQuery);
            return this;
        }

        public MockUpdate text(String text) {
            this.messageText = text;
            return this;
        }

        public Update build() {
            if (messageText != null) {
                this.update.getMessage().setText(messageText);
            }
            return this.update;
        }
    }
}
