package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.Message;
import com.artemoons.covid19info.util.HashManager;
import com.artemoons.covid19info.util.HistoryTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.artemoons.covid19info.helper.MessageFormatterHelper.getText;
import static com.artemoons.covid19info.helper.MessageFormatterHelper.setSign;
import static com.artemoons.covid19info.helper.MarkdownFormatter.setBold;
import static com.artemoons.covid19info.helper.MarkdownFormatter.setItalic;

@Service
@Slf4j
public class MessageFormatter {

    @Value("${message.footer}")
    private String messageFooter;

    HistoryTracker historyTracker;

    HashManager hashManager;

    private static final String EMPTY_LINE = "";

    private static final String TITLE_POSTFIX = " (МСК)";

    private static final int TITLE_MESSAGE_INDEX = 0;

    @Autowired
    public MessageFormatter(final HistoryTracker historyTracker,
                            final HashManager hashManager) {
        this.historyTracker = historyTracker;
        this.hashManager = hashManager;
    }

    public StringBuilder prepareMessageToSend(final Message message) {
        String newMessageHash = hashManager.calculateHash(message);
        if (hashManager.previousHashIsDifferent(newMessageHash)) {
            hashManager.saveLastParseResultInfoHash(newMessageHash);
            return convertMessageToPlaintext(message);
        }
        return new StringBuilder("");
    }

    private StringBuilder convertMessageToPlaintext(final Message message) {

        int i;
        List<String> messageLines = new ArrayList<>();
        StringBuilder plaintextMessage = new StringBuilder();

        Message newMessage = historyTracker.parseNumbersForDeltas(message);
        HistoryRecord oldMessage = historyTracker.loadPreviousDayStatistic();
        List<Long> difference = historyTracker.getDifference(newMessage, oldMessage);

        log.info(message.getLastUpdateInformation().get(TITLE_MESSAGE_INDEX).text());

        messageLines.add(setBold(message.getLastUpdateInformation().get(TITLE_MESSAGE_INDEX).text() + TITLE_POSTFIX));
        messageLines.add(EMPTY_LINE);
        //todo replace on foreach
        for (i = 0; i < message.getStatisticDescriptions().size(); i++) {
            log.info(getText(message.getStatisticNumbers().get(i))
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
            if (!message.getStatisticDescriptions().get(i).text().contains("сутки")) {
                if (message.getStatisticDescriptions().get(i).text().contains("тест")) {
                    messageLines.add(message.getStatisticNumbers().get(i).text().trim()
                            + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
                } else {
                    messageLines.add(getText(message.getStatisticNumbers().get(i))
                            + " " + message.getStatisticDescriptions().get(i).text().toLowerCase()
                            + " " + setItalic("(" + setSign(difference.get(i)) + ")"));
                }
            }
        }

        messageLines.add(EMPTY_LINE);
        messageLines.add(setItalic(messageFooter));

        for (String item : messageLines) {
            plaintextMessage.append(item).append(System.lineSeparator());
        }

        historyTracker.updateStatistic(message);

        return plaintextMessage;
    }
}
