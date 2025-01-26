package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.belousovea.taskmanageresbot.TestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AboutActionTest {

    @Mock
    private KeyboardFactory keyboardFactory;

    @InjectMocks
    private AboutAction action;

    @Test
    void test_isApplicable_true() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("/about and any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }

    @Test
    void test_isApplicable_whenAnyState() {
        Dialog testDialog = mockDialog(Dialog.State.INVALID_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("/about and any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertTrue(actual);
    }


    @Test
    void test_isApplicable_whenWrongMessageText() {
        Dialog testDialog = mockDialog(Dialog.State.BASIC_STATE);
        Update testUpdate = mockUpdate().user(TEST_USER).text("any text").build();

        boolean actual = action.isApplicable(testDialog, testUpdate);

        assertFalse(actual);
    }


}