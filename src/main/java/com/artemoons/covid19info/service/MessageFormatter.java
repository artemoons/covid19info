package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.Message;
import lombok.extern.slf4j.Slf4j;
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

    private static final String EMPTY_LINE = "";

    public StringBuilder prepareMessageToSend(final Message message) {
        return convertMessageToPlaintext(message);
    }

    private StringBuilder convertMessageToPlaintext(final Message message) {

        int i;
        List<String> messageLines = new ArrayList<>();
        StringBuilder plaintextMessage = new StringBuilder();

        log.info(message.getLastUpdateInformation().get(0).text());

        messageLines.add(setBold(message.getLastUpdateInformation().get(0).text() + " (МСК)"));
        messageLines.add(EMPTY_LINE);
        //todo replace on foreach
        for (i = 0; i < message.getStatisticDescriptions().size(); i++) {
            log.info(message.getStatisticNumbers().get(i).text()
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
            messageLines.add(message.getStatisticNumbers().get(i).text()
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
        }

        messageLines.add(EMPTY_LINE);
        messageLines.add(setItalic(messageFooter));

        for (String item : messageLines) {
            plaintextMessage.append(item).append(System.lineSeparator());
        }
        return returnPlaintextMessageAndSaveHash(plaintextMessage);
    }

    private StringBuilder returnPlaintextMessageAndSaveHash(StringBuilder plaintextMessage) {
        if (previousHashIsDifferent(plaintextMessage)) {
            saveLastParseResultInfoHash(plaintextMessage);
            return plaintextMessage;
        } else {
            log.info("No difference between new and old parse information.");
            return new StringBuilder("");
        }
    }

    private boolean previousHashIsDifferent(StringBuilder plaintextMessage) {
        return Boolean.FALSE.equals(parsedResultEqualsNewResult(plaintextMessage));
    }

    private Boolean parsedResultEqualsNewResult(final StringBuilder message) {

        String savedLastParseResult;

        try {
            if (Paths.get(syncFileName).toFile().exists()) {
                savedLastParseResult = new String(Files.readAllBytes(Paths.get(syncFileName)));
            } else {
                Files.createFile(Paths.get(syncFileName));
                savedLastParseResult = "";
            }
            if (savedLastParseResult.equals(String.valueOf(message.toString().hashCode()))) {
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

    private void saveLastParseResultInfoHash(final StringBuilder message) {
        Path path = Paths.get(syncFileName);
        try {
            Files.write(path, String.valueOf(message.toString().hashCode()).getBytes());
            log.info("Successfully updated last parse result info hash!");
        } catch (IOException ex) {
            log.error("Can't write last parse result information to file.", ex);
        }
    }

    private String setBold(final String text) {
        return "*" + text + "*";
    }

    private String setItalic(final String text) {
        return "_" + text + "_";
    }


}
