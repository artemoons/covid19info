package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.dto.JsonMessage;
import com.artemoons.covid19info.util.HashManager;
import com.artemoons.covid19info.util.HistoryTracker;
import com.artemoons.covid19info.util.StatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.artemoons.covid19info.helper.MarkdownFormatter.setBold;
import static com.artemoons.covid19info.helper.MarkdownFormatter.setItalic;
import static com.artemoons.covid19info.helper.MessageFormatterHelper.setSign;

@Service
@Slf4j
public class MessageFormatter {

    @Value("${message.footer}")
    private String messageFooter;

    HistoryTracker historyTracker;

    StatsCounter statsCounter;

    HashManager hashManager;

    private static final String EMPTY_LINE = "";

    private static final String TITLE_POSTFIX = " (МСК)";

    @Autowired
    public MessageFormatter(final HistoryTracker historyTracker,
                            final HashManager hashManager,
                            final StatsCounter statsCounter) {
        this.historyTracker = historyTracker;
        this.hashManager = hashManager;
        this.statsCounter = statsCounter;
    }

    public String prepareJsonMessageToSend(final JsonItems jsonItems) {

        if (hashManager.previousJsonHashIsDifferent(jsonItems)) {
            hashManager.saveLastParseJsonResultInfoHash(hashManager.calculateJsonHash(jsonItems));
            return convertJsonMessageToPlaintext(jsonItems);
        }
        return "";
    }

    public String convertJsonMessageToPlaintext(final JsonItems jsonItems) {

        List<String> messageLines = new ArrayList<>();
        String plaintextMessage = "";
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String dayAndMonth = today.format(DateTimeFormatter.ofPattern("dd MMMM"));

        HistoryRecord oldMessage = historyTracker.loadPreviousDayStatistic();
        JsonMessage statistic = StatsCounter.countTotalStatistic(jsonItems);
        List<Long> difference = historyTracker.getDifference(statistic, oldMessage);

        if (oldMessage.getDeathsOverall() < statistic.getDeathsOverall()
                || oldMessage.getHealedOverall() < statistic.getDeathsOverall()) {

            messageLines.add(setBold("по состоянию на " + dayAndMonth
                    + " " + now.minus(4, ChronoUnit.HOURS).format(formatter)
                    + TITLE_POSTFIX));
            messageLines.add(EMPTY_LINE);
            messageLines.add(statistic.getTestsOverall() + " проведено тестов "
                    + setItalic("(" + setSign(difference.get(0)) + ")"));
            messageLines.add(statistic.getInfectedOverall() + " случаев заболевания "
                    + setItalic("(" + setSign(difference.get(1)) + ")"));
            messageLines.add(statistic.getHealedOverall() + " человек выздоровело "
                    + setItalic("(" + setSign(difference.get(2)) + ")"));
            messageLines.add(statistic.getDeathsOverall() + " человек умерло "
                    + setItalic("(" + setSign(difference.get(3)) + ")"));
            messageLines.add(EMPTY_LINE);
            messageLines.add(setItalic(messageFooter));

            for (String item : messageLines) {
                plaintextMessage = plaintextMessage.concat(item).concat(System.lineSeparator());
            }

            historyTracker.updateJsonStatistic(statistic);

            return plaintextMessage;
        } else {
            log.warn("Previous stats values are smaller than new, message not sent.");
            return "";
        }
    }
}
