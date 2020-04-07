package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageFormatter {

    @Value("${telegram.sync-file-name}")
    private String syncFileName;

    @Value("${message.footer}")
    private String messageFooter;

    HistoryTracker historyTracker;

    private static final String EMPTY_LINE = "";

    private static final String TITLE_POSTFIX = " (МСК)";

    private static final int TITLE_MESSAGE_INDEX = 0;

    @Autowired
    public MessageFormatter(final HistoryTracker historyTracker) {
        this.historyTracker = historyTracker;
    }

    public StringBuilder prepareMessageToSend(final Message message) {
        String newMessageHash = calculateHash(message);
        if (previousHashIsDifferent(newMessageHash)) {
            saveLastParseResultInfoHash(newMessageHash);
            return convertMessageToPlaintext(message);
        }
        return new StringBuilder("");
    }

    private String calculateHash(final Message message) {
        List<String> messageLines = new ArrayList<>();
        for (int i = 0; i < message.getStatisticDescriptions().size(); i++) {
            messageLines.add(message.getStatisticNumbers().get(i).text()
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
        }
        return String.valueOf(messageLines.hashCode());
    }

    private boolean previousHashIsDifferent(String message) {
        return Boolean.FALSE.equals(getHashFromHistoryAndCompareTo(message));
    }

    private Boolean getHashFromHistoryAndCompareTo(final String message) {

        String savedLastParseResult;

        try {
            if (Paths.get(syncFileName).toFile().exists()) {
                savedLastParseResult = new String(Files.readAllBytes(Paths.get(syncFileName)));
            } else {
                Files.createFile(Paths.get(syncFileName));
                savedLastParseResult = "";
            }
            if (savedLastParseResult.equals(message)) {
                log.info("Saved parsed result equals new value.");
                return true;
            }
            log.debug("Last parse result info: {}", savedLastParseResult);
        } catch (IOException ex) {
            log.error("Can't open file with last parsed result", ex);
        }
        log.info("Saved parsed result different from new value.");
        return false;
    }

    private void saveLastParseResultInfoHash(final String message) {
        Path path = Paths.get(syncFileName);
        try {
            Files.write(path, message.getBytes());
            log.info("Successfully updated last parse result info hash!");
        } catch (IOException ex) {
            log.error("Can't write last parse result information to file.", ex);
        }
    }

    private StringBuilder convertMessageToPlaintext(final Message message) {

        int i;
        List<String> messageLines = new ArrayList<>();
        StringBuilder plaintextMessage = new StringBuilder();

        Message newMessage = parseNumbersForDeltas(message);
        HistoryRecord oldMessage = historyTracker.loadPreviousDayStatistic();
        List<Long> difference = historyTracker.getDifference(newMessage, oldMessage);

        log.info(message.getLastUpdateInformation().get(TITLE_MESSAGE_INDEX).text());

        messageLines.add(setBold(message.getLastUpdateInformation().get(TITLE_MESSAGE_INDEX).text() + TITLE_POSTFIX));
        messageLines.add(EMPTY_LINE);
        //todo replace on foreach
        for (i = 0; i < message.getStatisticDescriptions().size(); i++) {
            log.info(message.getStatisticNumbers().get(i).text()
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
            if (!message.getStatisticDescriptions().get(i).text().contains("сутки")) {
                if (message.getStatisticDescriptions().get(i).text().contains("тест")) {
                    messageLines.add(message.getStatisticNumbers().get(i).text()
                            + " " + message.getStatisticDescriptions().get(i).text().toLowerCase()
                            + " " + setItalic("(" + setSign(difference.get(i)) + " тыс." + ")"));
                } else {
                    messageLines.add(message.getStatisticNumbers().get(i).text()
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

        updateStatistic(message);

        return plaintextMessage;
    }

    private Message parseNumbersForDeltas(final Message message) {

        List<String> statisticNumbers = new ArrayList<>();

        for (int i = 0; i < message.getStatisticNumbers().size(); i++) {
            statisticNumbers.add(message.getStatisticNumbers()
                    .get(i)
                    .text()
                    .trim()
                    .replaceAll("\\D", ""));
        }
        message.setTestsOverall(Long.valueOf(statisticNumbers.get(0)));
        message.setInfectedOverall(Long.valueOf(statisticNumbers.get(1)));
        message.setInfectedLastDay(Long.valueOf(statisticNumbers.get(2)));
        message.setHealedOverall(Long.valueOf(statisticNumbers.get(3)));
        message.setDeathsOverall(Long.valueOf(statisticNumbers.get(4)));
        return message;
    }

    private void updateStatistic(Message message) {
        historyTracker.saveTodayStatistic(message.getTestsOverall(),
                message.getInfectedOverall(),
                message.getInfectedLastDay(),
                message.getHealedOverall(),
                message.getDeathsOverall());
    }

    private String setSign(final Long number) {
        return number > 0 ? "+" + number : String.valueOf(number);
    }

    private String setBold(final String text) {
        return "*" + text + "*";
    }

    private String setItalic(final String text) {
        return "_" + text + "_";
    }
}
