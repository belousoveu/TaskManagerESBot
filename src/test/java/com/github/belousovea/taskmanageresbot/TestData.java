package com.github.belousovea.taskmanageresbot;

import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.Period;
import com.github.belousovea.taskmanageresbot.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.LocalDateTime;
import java.util.List;

public class TestData {

    public final static User TEST_USER = User.builder().userId(1L).userName("testUser").firstName("testFirstName").build();
    public final static User NEW_USER = User.builder().userId(2L).userName("newUser").firstName("testFirstName").build();

    public final static SendMessage TEST_MESSAGE = SendMessage.builder().chatId(1L).text("test").build();

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
