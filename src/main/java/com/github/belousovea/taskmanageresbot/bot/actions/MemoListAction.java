package com.github.belousovea.taskmanageresbot.bot.actions;

import com.github.belousovea.taskmanageresbot.bot.keyboards.KeyboardFactory;
import com.github.belousovea.taskmanageresbot.model.Dialog;
import com.github.belousovea.taskmanageresbot.model.MemoListDto;
import com.github.belousovea.taskmanageresbot.service.MemoService;
import com.github.belousovea.taskmanageresbot.utils.Literals;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class MemoListAction implements BotAction {
    private final KeyboardFactory keyboardFactory;
    private final MemoService memoService;

    @Override
    public SendMessage replyMessage(Dialog dialog, Update update) {
        MemoListDto memoListDto = memoService.getMemoList(dialog.getUser().getUserId());
        String text = String.format(Literals.MEMO_LIST_MESSAGE,
                memoListDto.getUser().getUserName(),
                memoListDto.getNumberOfMemos(),
                memoListDto.getNumberOfPeriodicMemos(),
                memoListDto.getNearestMemoTime()) + Literals.formatMemoList(memoListDto.getMemos());
        if (memoListDto.getMemos().isEmpty()) {
            text = String.format(Literals.MEMO_EMPTY_LIST_MESSAGE, memoListDto.getUser().getUserName());
        }
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(text)
                .parseMode(Literals.MARKDOWN_MODE)
                .replyMarkup(keyboardFactory.getKeyboard(dialog.getCurrentState()))
                .build();

    }

    @Override
    public boolean isApplicable(Dialog dialog, Update update) {
        return dialog.getCurrentState() == Dialog.State.BASIC_STATE
                && update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals(Literals.BUTTON_TITLE_MEMO_LIST);
    }
}
