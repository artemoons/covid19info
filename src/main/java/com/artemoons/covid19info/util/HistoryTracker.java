package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.JsonMessage;
import com.artemoons.covid19info.timer.SlowMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class HistoryTracker {

    //todo: externalize
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    String historyRecords;
    String historyFilename = "history-tracker.json";

    SlowMode slowMode;

    @Autowired
    public HistoryTracker(final SlowMode slowMode) {
        this.slowMode = slowMode;
    }


    public HistoryRecord loadPreviousDayStatistic() {
        List<HistoryRecord> statisticFile = readStatisticFile();

        if (!statisticFile.isEmpty()) {
            String yesterday = LocalDate.now()
                    .minusDays(1)
                    .format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
            for (HistoryRecord historyRecord : statisticFile) {
                if (historyRecord.getDate().equals(yesterday)) {
                    return historyRecord;
                }
            }
        }
        log.info("Previous day statistic is empty.");
        return new HistoryRecord("0", "0", 0L, 0L, 0L, 0L);
    }

    private List<HistoryRecord> readStatisticFile() {
        Gson gson = new GsonBuilder().setDateFormat(DD_MM_YYYY).setPrettyPrinting().create();
        Type collectionType = new TypeToken<List<HistoryRecord>>() {
        }.getType();
        try {
            if (Paths.get(historyFilename).toFile().exists()) {
                historyRecords = new String(Files.readAllBytes(Paths.get(historyFilename)));
                List<HistoryRecord> historyLog = gson.fromJson(historyRecords, collectionType);
                log.info("Successfully loaded history stats information.");
                return historyLog;
            } else {
                Files.createFile(Paths.get(historyFilename));
                return Collections.emptyList();
            }
        } catch (JsonSyntaxException | IOException ex) {
            log.error("Error occurred when trying to load JSON file with history stats.", ex);
            return Collections.emptyList();
        }
    }

    public void saveTodayJsonStatistic(final Long infectedO,
                                       final Long healedO,
                                       final Long deathO,
                                       final Long activeO) {
        Boolean isFound = false;
        UUID uuid = UUID.randomUUID();
        List<HistoryRecord> previousStatistic = readStatisticFile();
        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
        //todo thats a second call

        HistoryRecord todayStats = new HistoryRecord(uuid.toString(),
                today,
                infectedO,
                healedO,
                deathO,
                activeO);

        for (int i = 0; i < previousStatistic.size(); i++) {
            if (previousStatistic.get(i).getDate().equals(today)) {
                previousStatistic.set(i, todayStats);
                isFound = true;
            }
        }

        if (Boolean.FALSE.equals(isFound)) {
            previousStatistic.add(todayStats);
        }

        writeStatisticFile(previousStatistic);
    }

    private void writeStatisticFile(final List<HistoryRecord> statisticData) {
        Path path = Paths.get(historyFilename);
        Gson gson = new GsonBuilder().setDateFormat(DD_MM_YYYY).setPrettyPrinting().create();
        String json = gson.toJson(statisticData);
        List<String> lines = Arrays.asList(json.split(System.lineSeparator()));
        try {
            Files.write(path, lines);
        } catch (IOException ex) {
            log.error("Can't write new value to JSON.");
        }
    }

    public List<Long> getDifference(final JsonMessage newMessage, final HistoryRecord oldMessage) {
        List<Long> difference = new ArrayList<>();
        difference.add(newMessage.getInfectedOverall() - oldMessage.getInfectedOverall());
        difference.add(newMessage.getHealedOverall() - oldMessage.getHealedOverall());
        difference.add(newMessage.getDeathsOverall() - oldMessage.getDeathsOverall());
        difference.add(newMessage.getActiveOverall() - oldMessage.getActiveOverall());
        return difference;
    }

    public void updateJsonStatistic(final JsonMessage newStatistic) {
        saveTodayJsonStatistic(
                newStatistic.getInfectedOverall(),
                newStatistic.getHealedOverall(),
                newStatistic.getDeathsOverall(),
                newStatistic.getActiveOverall());
    }
}
