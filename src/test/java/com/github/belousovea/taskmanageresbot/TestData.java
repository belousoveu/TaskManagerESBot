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

public class TestData {

    public final static User TEST_USER = User.builder().userId(1L).userName("testUser").firstName("testFirstName").build();
    public final static User NEW_USER = User.builder().userId(2L).userName("newUser").firstName("testFirstName").build();

    public final static SendMessage TEST_MESSAGE = SendMessage.builder().chatId(1L).text("test").build();


    public static Memo mockMemo(String text) {
        return Memo.builder()
                .reminderTime(LocalDateTime.now())
                .reminderText(text)
                .userId(TEST_USER.getUserId())
                .period(Period.ONE_TIME.name())
                .build();
    }

    public static MockUpdate mockUpdate() {
        return new MockUpdate();
    }

    public static class MockUpdate {
        private final Update update;

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
                    .from(telegramUser)
                    .build());

            CallbackQuery callbackQuery = new CallbackQuery();
            callbackQuery.setFrom(telegramUser);

            update.setCallbackQuery(callbackQuery);
            return this;
        }

        public Update build() {
            return this.update;
        }
    }
}
