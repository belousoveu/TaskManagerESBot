package com.github.belousovea.taskmanageresbot.utils;

import com.github.belousovea.taskmanageresbot.model.Memo;
import com.github.belousovea.taskmanageresbot.model.Period;

import java.util.List;

public class Literals {

    public static final String BUTTON_TITLE_CANCEL = "Возврат в главное меню";
    public static final String BUTTON_TITLE_ADD_MEMO = "➕ Добавить напоминание";
    public static final String BUTTON_TITLE_MEMO_LIST = "\uD83D\uDDD2 Список напоминаний";

    public static final String PERIOD_ONE_TIME_TEXT = "Разовое напоминание";
    public static final String PERIOD_MINUTE_TEXT = "Ежеминутно";
    public static final String PERIOD_HOUR_TEXT = "Каждый час";
    public static final String PERIOD_DAY_TEXT = "Ежедневно";
    public static final String PERIOD_WEEK_TEXT = "Еженедельно";
    public static final String PERIOD_MONTH_TEXT = "Ежемесячно";
    public static final String PERIOD_YEAR_TEXT = "Ежегодно";

    public static final String COMMAND_ABOUT = "/about";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_START = "/start";

    public static final String COMMAND_ABOUT_DESCRIPTION = "О программе";
    public static final String COMMAND_HELP_DESCRIPTION = "Помощь по работе с ботом";
    public static final String COMMAND_START_DESCRIPTION = "Начать работу";

    public static final String COMMAND_START_MESSAGE = """
            Привет *%s*, я бот для управления напоминаниями.
            Напиши мне все, что боишься забыть,
            а я вовремя напомню тебе об этом""";

    public static final String COMMAND_ABOUT_MESSAGE = """
            Версия 1.0""";

    public static final String COMMAND_HELP_MESSAGE = """
            Я умею запоминать сообщения и напоминать тебе о них в нужное время.
            
            Чтобы попробовать, как это работает нажми на кнопку
             `➕ Добавить напоминание`
            
            Список своих заметок всегда можно посмотреть нажав
            `\uD83D\uDDD2 Список напоминаний`.""";

    public static final String TIME_SETUP_MESSAGE = """
            Приветствую, %s!
            Давай сверим часы. У меня сейчас : %s
            Напиши, какое время показывают твои часы в формате HH:MM""";

    public static final String ADD_MEMO_MESSAGE = """
            Введите строку в формате:
            *ДД.ММ.ГГГГ ЧЧ:ММ Текст напоминания*
            """;

    public static final String MEMO_LIST_MESSAGE = """
            *%s*, сейчас я храню для тебя:
            Простых напоминаний       - %s
            Периодических напоминаний - %s
            Ближайшее сообщение ты получишь *%s*
            """;

    public static final String MEMO_EMPTY_LIST_MESSAGE = """
            *%s*, пока твой список напоминаний пуст.
            Может самое время что-нибудь добавить?
            """;

    public static final String GET_NEW_MEMO_MESSAGE = """
            Подготовлено новое напоминание:
            Дата и время: %s
            Текст: %s
            Выберете, как часто напоминать об этом событии?
            """;

    public static final String GET_PERIOD_MESSAGE = """
            Сохранил для тебя новое напоминание:
            ```
            Дата и время: %s
            Текст: %s
            Периодичность: %s
            ```
            """;

    public static final String INVALID_TIME_REMINDER_MESSAGE = """
            Указанное время %s уже прошло.
            Я не смогу напомнить об уже прошедшем событии. Попробуйте еще раз
            """;

    public static final String GET_USER_TIME_MESSAGE = "Ок. Я посчитал разницу и буду учитывать ее при напоминаниях";

    public static final String INVALID_TIME_FORMAT_MESSAGE = "Я не смог понять, который у вас час. Пришлите еще раз время в формате ЧЧ:ММ";

    public static final String REMINDER_MESSAGE = """
            Привет, *%s*!
            Ты просил меня напомнить о
            *%s*
            """;

    public static final String ERROR_DATABASE_MESSAGE = "Ошибка при записи в базу данных. Попробуйте еще раз";

    public static final String ERROR_MEMO_FORMAT_MESSAGE = "Данные отсутствуют или не соответствуют формату";

    public static final String CANCEL_MESSAGE = "Выберете дальнейшее действие";

    public static final String UNKNOWN_ACTION_MESSAGE = "Я не понял, что вы хотели. Может /help поможет вам разобраться?";

    public static final String MARKDOWN_MODE = "Markdown";

    public static String formatMemoList(List<Memo> memos) {
        StringBuilder sb = new StringBuilder("\n```\n");
        for (Memo memo : memos) {
            sb.append(memo.isRepetitive() ? "\uD83D\uDCC5" : "⏰")
                    .append(" ")
                    .append(memo.getReminderTime().toString().replace("T", " "))
                    .append(" ")
                    .append(memo.getReminderText())
                    .append(", ")
                    .append(Period.valueOf(memo.getPeriod()).getText())
                    .append("\n");
        }
        sb.append("```");
        return sb.toString();
    }
}
