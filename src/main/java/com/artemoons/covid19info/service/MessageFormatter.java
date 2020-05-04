package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonMessage;
import com.artemoons.covid19info.util.AntirecordTracker;
import com.artemoons.covid19info.util.HashManager;
import com.artemoons.covid19info.util.HistoryTracker;
import com.artemoons.covid19info.util.StatisticCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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

    HashManager hashManager;

    AntirecordTracker antirecordTracker;

    StatisticCounter statisticCounter;

    private static final String EMPTY_LINE = "";

    private static final String TITLE_POSTFIX = " (МСК)";

    @Autowired
    public MessageFormatter(final HistoryTracker historyTracker,
                            final HashManager hashManager,
                            final AntirecordTracker antirecordTracker,
                            final StatisticCounter statisticCounter) {
        this.historyTracker = historyTracker;
        this.hashManager = hashManager;
        this.antirecordTracker = antirecordTracker;
        this.statisticCounter = statisticCounter;
    }

    public List<String> prepareJsonMessageToSend(final List<JsonItem> jsonItems) {

        if (hashManager.previousJsonHashIsDifferent(jsonItems)) {
            hashManager.saveLastParseJsonResultInfoHash(hashManager.calculateJsonHash(jsonItems));
            return convertJsonMessageToPlaintext(jsonItems);
        }
        return Collections.emptyList();
    }

    public List<String> convertJsonMessageToPlaintext(final List<JsonItem> jsonItems) {

        List<String> messageLines = new ArrayList<>();
        List<String> messageWithoutFormatting = new ArrayList<>();
        List<String> message = new ArrayList<>();
        String plaintextMessage = "";
        String plaintextWithoutFormatting = "";
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String dayAndMonth = today.format(DateTimeFormatter.ofPattern("dd MMMM"));

        HistoryRecord oldStatistic = historyTracker.loadPreviousDayStatistic();
        JsonMessage statistic = statisticCounter.countStatistic(jsonItems);
        List<Long> difference = historyTracker.getDifference(statistic, oldStatistic);

        String infectedIncreased = antirecordTracker.infectedNumberIncreased(difference);
        String healedIncreased = antirecordTracker.healedNumberIncreased(difference);
        String deathsIncreased = antirecordTracker.deathsNumberIncreased(difference);

        messageLines.add(setBold("по состоянию на " + dayAndMonth
                + " " + now.minus(4, ChronoUnit.HOURS).format(formatter)
                + TITLE_POSTFIX));
        messageLines.add(EMPTY_LINE);
        messageLines.add(statistic.getInfectedOverall() + " подтверждено "
                + setItalic("(" + setSign(difference.get(0)) + ")") + infectedIncreased);
        messageLines.add(statistic.getHealedOverall() + " выздоровело "
                + setItalic("(" + setSign(difference.get(1)) + ")") + healedIncreased);
        messageLines.add(statistic.getActiveOverall() + " болеют "
                + setItalic("(" + setSign(difference.get(3)) + ")"));
        messageLines.add(statistic.getDeathsOverall() + " смертей "
                + setItalic("(" + setSign(difference.get(2)) + ")") + deathsIncreased);
        messageLines.add(EMPTY_LINE);
        messageLines.add(setItalic(messageFooter));

        messageWithoutFormatting.add(String.valueOf(statistic.getInfectedOverall()));
        messageWithoutFormatting.add(String.valueOf(statistic.getHealedOverall()));
        messageWithoutFormatting.add(String.valueOf(statistic.getActiveOverall()));
        messageWithoutFormatting.add(String.valueOf(statistic.getDeathsOverall()));

        for (String item : messageWithoutFormatting) {
            plaintextWithoutFormatting = plaintextWithoutFormatting.concat(item).concat(System.lineSeparator());
        }

        for (String item : messageLines) {
            plaintextMessage = plaintextMessage.concat(item).concat(System.lineSeparator());
        }

        message.add(plaintextMessage);
        message.add(plaintextWithoutFormatting);

        historyTracker.updateJsonStatistic(statistic);

        return message;
    }
}
