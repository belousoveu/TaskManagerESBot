package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.service.UserService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserTimeActionTest {
    @Mock
    private KeyboardFactory keyboardFactory;

    @Mock
    private UserService userService;

    @InjectMocks
    private GetUserTimeAction action;

    @Test
    void test_isApplicable_withCorrectState() {
        Dialog testDialog = mockDialog(Dialog.State.GET_USER_TIME);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }

    @Test
    void test_isApplicable_whenWrongState() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }


    @Test
    void test_replyMessage_withCorrectAnswer() {

        Dialog testDialog = mockDialog(Dialog.State.GET_USER_TIME);
        Update testUpdate = mockUpdate().user(TEST_USER).text("15:00").build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);
        when((userService.saveUser(any(), anyLong()))).thenReturn(TEST_USER);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertEquals(actual.getText(), Literals.GET_USER_TIME_MESSAGE);
        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.BASIC_STATE, testDialog.getCurrentState());
    }

    @Test
    void test_replyMessage_withInvalidTimeFormat() {

        Dialog testDialog = mockDialog(Dialog.State.GET_USER_TIME);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertEquals(actual.getText(), Literals.INVALID_TIME_FORMAT_MESSAGE);
        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.GET_USER_TIME, testDialog.getCurrentState());
    }

}