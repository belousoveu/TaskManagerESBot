package com.github.belousovea.taskmanageresbot.service;

import com.github.belousovea.taskmanageresbot.TestData;
import com.github.belousovea.taskmanageresbot.bot.actions.BotAction;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.belousovea.taskmanageresbot.TestData.TEST_MESSAGE;
import static com.github.belousovea.taskmanageresbot.TestData.TEST_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DialogManagerTest {


    @Mock
    private BotAction botAction;

    @Mock
    private BotAction defaultBotAction;

    @Mock
    private UserService userService;

    private DialogManager dialogManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<BotAction> actions = List.of(defaultBotAction, botAction);
        dialogManager = new DialogManager(actions, userService, defaultBotAction);

    }

    @Test
    void test_getAction_withApplicableAction() {

        Update update = TestData.mockUpdate().user(TEST_USER).build();

        when(userService.getUser(1L)).thenReturn(TEST_USER);
        when(botAction.isApplicable(any(Dialog.class), eq(update))).thenReturn(true);
        when(botAction.replyMessage(any(Dialog.class), eq(update))).thenReturn(TEST_MESSAGE);

        SendMessage result = dialogManager.getAction(update);

        assertNotNull(result);
        assertEquals(1, dialogManager.getDialogs().size());

        verify(botAction).isApplicable(any(Dialog.class), eq(update));
        verify(botAction, times(1)).replyMessage(any(Dialog.class), eq(update));
        verify(defaultBotAction, times(0)).replyMessage(any(Dialog.class), eq(update));
        verify(userService).getUser(1L);

    }

    @Test
    void test_getAction_withDefaultAction() {

        Update update = TestData.mockUpdate().user(TEST_USER).build();

        when(userService.getUser(1L)).thenReturn(TEST_USER);
        when(botAction.isApplicable(any(Dialog.class), eq(update))).thenReturn(false);
        when(defaultBotAction.replyMessage(any(Dialog.class), eq(update))).thenReturn(TEST_MESSAGE);

        SendMessage result = dialogManager.getAction(update);

        assertNotNull(result);
        assertEquals(1, dialogManager.getDialogs().size());

        verify(botAction).isApplicable(any(Dialog.class), eq(update));
        verify(botAction, times(0)).replyMessage(any(Dialog.class), eq(update));
        verify(defaultBotAction, times(1)).replyMessage(any(Dialog.class), eq(update));
        verify(userService).getUser(1L);

    }
}