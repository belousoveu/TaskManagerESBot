package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static com.github.belousovea.taskmanageresbot.utils.Literals.BUTTON_TITLE_CANCEL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelButtonActionTest {

    @Mock
    private KeyboardFactory keyboardFactory;

    @InjectMocks
    private CancelButtonAction action;

    @Test
    void test_isApplicable_true() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(BUTTON_TITLE_CANCEL).build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }


    @Test
    void test_isApplicable_whenWrongMessageText() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }


    @Test
    void test_replyMessage() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(BUTTON_TITLE_CANCEL).build();

        when(keyboardFactory.getKeyboard(any(Dialog.State.class))).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertEquals(Long.parseLong(actual.getChatId()), TEST_USER.getUserId());
        assertEquals(Dialog.State.BASIC_STATE, testDialog.getCurrentState());
    }
}