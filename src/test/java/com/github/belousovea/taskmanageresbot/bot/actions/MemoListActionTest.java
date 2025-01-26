package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.MemoListDto;
import com.github.belousovea.taskmanageresbot.service.MemoService;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemoListActionTest {

    @Mock
    private KeyboardFactory keyboardFactory;

    @Mock
    private MemoService memoService;

    @InjectMocks
    private MemoListAction action;

    @Test
    void test_isApplicable_true() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(Literals.BUTTON_TITLE_MEMO_LIST).build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }

    @Test
    void test_isApplicable_whenWrongState() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(Literals.BUTTON_TITLE_MEMO_LIST).build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }


    @Test
    void test_isApplicable_whenWrongMessageText() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }

    @Test
    void test_replyMessage_whenMemoListIsEmpty() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(Literals.BUTTON_TITLE_MEMO_LIST).build();

        MemoListDto expected = mockMemoListDto(TEST_USER, new ArrayList<>());
        when(memoService.getMemoList(TEST_USER.getUserId())).thenReturn(expected);
        when(keyboardFactory.getKeyboard(testDialog.getCurrentState())).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        assertNotNull(actual);
        assertEquals(String.format(Literals.MEMO_EMPTY_LIST_MESSAGE, TEST_USER.getUserName()), actual.getText());
        assertEquals(Dialog.State.BASIC_STATE, testDialog.getCurrentState());

    }

    @Test
    void test_replyMessage_whenMemoListNotEmpty() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text(Literals.BUTTON_TITLE_MEMO_LIST).build();

        MemoListDto expected = mockMemoListDto(TEST_USER, getTestMemos());
        when(memoService.getMemoList(TEST_USER.getUserId())).thenReturn(expected);
        when(keyboardFactory.getKeyboard(testDialog.getCurrentState())).thenReturn(TEST_KEYBOARD);

        SendMessage actual = action.replyMessage(testDialog, testUpdate);

        String expectedText = String.format(Literals.MEMO_LIST_MESSAGE,
                expected.getUser().getUserName(),
                expected.getNumberOfMemos(),
                expected.getNumberOfPeriodicMemos(),
                expected.getNearestMemoTime()) + Literals.formatMemoList(expected.getMemos());

        assertNotNull(actual);
        assertEquals(expectedText, actual.getText());
        assertEquals(Dialog.State.BASIC_STATE, testDialog.getCurrentState());

    }
}