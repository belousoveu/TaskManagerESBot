package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetNewMemoActionTest {


    @Mock
    private KeyboardFactory keyboardFactory;

    @InjectMocks
    private GetNewMemoAction action;


    @Test
    void test_isApplicable_true() {
        Dialog testDialog = mockDialog(Dialog.State.GET_NEW_MEMO);
        Update testUpdate = mockUpdate().user(TEST_USER).text("Any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }

    @Test
    void test_isApplicable_whenWrongState() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("Any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }


    @Test
    void test_isApplicable_whenWrongMessageText() {
        Dialog testDialog = mockDialog(Dialog.State.GET_NEW_MEMO);
        Update testUpdate = mockUpdate().user(TEST_USER).text(Literals.BUTTON_TITLE_CANCEL).build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }

    @Test
    void test_replyMessage_withCorrectAnswer() {

        Dialog testDialog = mockDialog(Dialog.State.GET_NEW_MEMO);
        Update testUpdate = mockUpdate().user(TEST_USER).text(TEST_CORRECT_MEMO_ANSWER).build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertNotNull(testDialog.getTempMemo());
        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.GET_PERIOD, testDialog.getCurrentState());
    }

    @Test
    void test_replyMessage_withPastTimeReminderAnswer() {

        Dialog testDialog = mockDialog(Dialog.State.GET_NEW_MEMO);
        Update testUpdate = mockUpdate().user(TEST_USER).text(TEST_PAST_TIME_MEMO_ANSWER).build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.GET_NEW_MEMO, testDialog.getCurrentState());
    }

    @Test
    void test_replyMessage_withInvalidAnswerFormat() {

        Dialog testDialog = mockDialog(Dialog.State.GET_NEW_MEMO);
        Update testUpdate = mockUpdate().user(TEST_USER).text(TEST_INVALID_MEMO_ANSWER).build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertEquals(actual.getText(), Literals.ERROR_MEMO_FORMAT_MESSAGE);
        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.GET_NEW_MEMO, testDialog.getCurrentState());
    }

}