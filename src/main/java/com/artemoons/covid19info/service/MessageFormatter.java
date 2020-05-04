package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.dto.JsonMessage;
import com.artemoons.covid19info.util.AntirecordTracker;
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

    @Value("${timezone.difference}")
    private Integer tzDifference;

    HistoryTracker historyTracker;

    StatsCounter statsCounter;

    HashManager hashManager;

    AntirecordTracker antirecordTracker;

    private static final String EMPTY_LINE = "";

    private static final String TITLE_POSTFIX = " (МСК)";

    @Autowired
    public MessageFormatter(final HistoryTracker historyTracker,
                            final HashManager hashManager,
                            final StatsCounter statsCounter,
                            final AntirecordTracker antirecordTracker) {
        this.historyTracker = historyTracker;
        this.hashManager = hashManager;
        this.statsCounter = statsCounter;
        this.antirecordTracker = antirecordTracker;
    }

    public String prepareJsonMessageToSend(final List<JsonItem> jsonItems) {

        if (hashManager.previousJsonHashIsDifferent(jsonItems)) {
            hashManager.saveLastParseJsonResultInfoHash(hashManager.calculateJsonHash(jsonItems));
            return convertJsonMessageToPlaintext(jsonItems);
        }
        return "";
    }

    public String convertJsonMessageToPlaintext(final List<JsonItem> jsonItems) {

        List<String> messageLines = new ArrayList<>();
        String plaintextMessage = "";
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String dayAndMonth = today.format(DateTimeFormatter.ofPattern("dd MMMM"));

        HistoryRecord oldMessage = historyTracker.loadPreviousDayStatistic();
        JsonMessage statistic = StatsCounter.countTotalStatistic(jsonItems);
        List<Long> difference = historyTracker.getDifference(statistic, oldMessage);

        String infectedIncreased = antirecordTracker.infectedNumberIncreased(difference);
        String healedIncreased = antirecordTracker.healedNumberIncreased(difference);
        String deathsIncreased = antirecordTracker.deathsNumberIncreased(difference);

        messageLines.add(setBold("по состоянию на " + dayAndMonth
                + " " + now.minus(tzDifference, ChronoUnit.HOURS).format(formatter)
                + TITLE_POSTFIX));
        messageLines.add(EMPTY_LINE);
        messageLines.add(statistic.getInfectedOverall() + " случаев заболевания "
                + setItalic("(" + setSign(difference.get(0)) + ")") + infectedIncreased);
        messageLines.add(statistic.getHealedOverall() + " человек выздоровело "
                + setItalic("(" + setSign(difference.get(1)) + ")") + healedIncreased);
        messageLines.add(statistic.getActiveOverall() + " болеют "
                + setItalic("(" + setSign(difference.get(3)) + ")"));
        messageLines.add(statistic.getDeathsOverall() + " человек умерло "
                + setItalic("(" + setSign(difference.get(2)) + ")") + deathsIncreased);
        messageLines.add(EMPTY_LINE);
        messageLines.add(setItalic(messageFooter));

        for (String item : messageLines) {
            plaintextMessage = plaintextMessage.concat(item).concat(System.lineSeparator());
        }

        historyTracker.updateJsonStatistic(statistic);

        return plaintextMessage;
    }
}
