package com.github.belousovea.taskmanageresbot.bot;


import com.github.belousovea.taskmanageresbot.bot.commands.MainMenuCommand;
import com.github.belousovea.taskmanageresbot.service.DialogManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final DialogManager dialogManager;

    @Value("${bot.token}")
    private String botToken;

    private TelegramClient client;
    private final Set<MainMenuCommand> commands;

    public Bot(Set<MainMenuCommand> commands, DialogManager dialogManager) {
        this.commands = new TreeSet<>(commands);
        this.dialogManager = dialogManager;
    }

    @PostConstruct
    public void init() {
        this.client = new OkHttpTelegramClient(getBotToken());

        try {
            client.execute(new SetMyCommands(commands.stream().map(MainMenuCommand::getCommand).toList()));
            log.debug("{} commands added", commands.size());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        SendMessage message = dialogManager.getAction(update);

        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
