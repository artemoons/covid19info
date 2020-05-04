package com.artemoons.covid19info.service;

import com.artemoons.covid19info.timer.SlowMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

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

    private Integer messageId = 0;

    private List<String> messageText;

    private String sentMessage;

    SlowMode slowMode;

    @Autowired
    public BotInitializer(final DefaultBotOptions options,
                          final SiteLoader siteLoader,
                          final SlowMode slowMode) {
        super(options);
        this.siteLoader = siteLoader;
        this.slowMode = slowMode;
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

        try {
            messageText = siteLoader.loadJson(url);
            if (!messageText.isEmpty() && !slowMode.isEnabled()) {
                SendMessage response = new SendMessage();
                response.setChatId(chatId)
                        .setText(messageText.get(0))
                        .enableMarkdown(true);
                messageId = execute(response).getMessageId();
                log.info("Sent message \"{}\" with id {} to {}", messageText.get(0), messageId, chatId);
                slowMode.createNewTimerTask();
                sentMessage = messageText.get(1);
            } else {
                if (!messageText.isEmpty() && messagesNotEqual()) {
                    log.info("Edit message {} since we are in slow mode...", messageId);
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(chatId)
                            .setMessageId(messageId)
                            .setText(messageText.get(0))
                            .enableMarkdown(true);
                    execute(editMessageText);
                    log.info("Sent edited message {}: \"{}\" in {}", messageId, messageText.get(0), chatId);
                    sentMessage = messageText.get(1);
                }
            }
        } catch (TelegramApiException ex) {
            log.error("Failed to send message \"{}\" to {} due to error: {}", messageText, chatId, ex.getMessage());
        }
    }

    private boolean messagesNotEqual() {
        return !messageText.get(1)
                .equals(sentMessage);
    }
}
