package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.HistoryRecord;
import com.artemoons.covid19info.dto.JsonMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class HistoryTracker {

    @Value("${json.date-format}")
    private String dateFormat;

    @Value("${json.history.file-name}")
    private String historyFilename;

    String historyRecords;

    public HistoryRecord loadPreviousDayStatistic() {
        List<HistoryRecord> statisticFile = readStatisticFile();
        return statisticFile.get(statisticFile.size() - 1);
    }

    private List<HistoryRecord> readStatisticFile() {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).setPrettyPrinting().create();
        Type collectionType = new TypeToken<List<HistoryRecord>>() {}.getType();
        try {
            if (Paths.get(historyFilename).toFile().exists()) {
                historyRecords = new String(Files.readAllBytes(Paths.get(historyFilename)));
                List<HistoryRecord> historyLog = gson.fromJson(historyRecords, collectionType);
                log.info("Successfully loaded history stats information.");
                return historyLog;
            } else {
                Files.createFile(Paths.get(historyFilename));
                historyRecords = "[{}]";
                return Collections.emptyList();
            }
        } catch (JsonSyntaxException | IOException ex) {
            log.error("Error occurred when trying to load JSON file with history stats.", ex);
            return Collections.emptyList();
        }
    }

    public void saveTodayJsonStatistic(final Long infectedO,
                                       final Long healedO,
                                       final Long deathO) {
        UUID uuid = UUID.randomUUID();
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
        List<HistoryRecord> previousStatistic;
        //todo thats a second call
        previousStatistic = readStatisticFile();

        HistoryRecord todayStats = new HistoryRecord();
        todayStats.setRecordId(uuid.toString());
        todayStats.setDate(dateFormat.format(new Date()));
        todayStats.setInfectedOverall(infectedO);
        todayStats.setHealedOverall(healedO);
        todayStats.setDeathsOverall(deathO);

        previousStatistic.add(todayStats);
        writeStatisticFile(previousStatistic);

    }

    private void writeStatisticFile(final List<HistoryRecord> statisticData) {
        Path path = Paths.get(historyFilename);
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).setPrettyPrinting().create();
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
        return difference;
    }

    public void updateJsonStatistic(final JsonMessage newStatistic) {
        saveTodayJsonStatistic(newStatistic.getInfectedOverall(),
                newStatistic.getHealedOverall(),
                newStatistic.getDeathsOverall());
    }
}
