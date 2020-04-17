package com.artemoons.covid19info.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@EnableScheduling
public class BotInitializer extends TelegramLongPollingBot {

    private SiteLoader siteLoader;

    @Value("${json.url}")
    private String url;

    @Value("${telegram.channel-name}")
    private String chatId;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Autowired
    public BotInitializer(final DefaultBotOptions options, final SiteLoader siteLoader) {
        super(options);
        this.siteLoader = siteLoader;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Method onUpdateReceived fired: {}", update.getMessage().getText());
    }

    @PostConstruct
    public void start() {
        log.info("username: {}, token: {}", username, token);
    }

    @Scheduled(fixedDelayString = "${scheduler.delay}")
    public void onSiteUpdateReceived() {

        String messageText = siteLoader.loadJson(url);
        SendMessage response = new SendMessage();
        response.setChatId(chatId)
                .setText(messageText)
                .enableMarkdown(true);

        try {
            if (messageText.length() != 0) {
                execute(response);
                log.info("Sent message \"{}\" to {}", messageText, chatId);
            }
        } catch (TelegramApiException ex) {
            log.error("Failed to send message \"{}\" to {} due to error: {}", messageText, chatId, ex.getMessage());
        }
    }
}
