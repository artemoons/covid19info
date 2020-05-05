package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.MaxValues;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AntirecordTracker {

    public static final String DD_MM_YYYY = "dd/MM/yyyy";

    @Value("${stats.max-values-name}")
    String maxValuesFilename;

    @Value("${stats.root-dir}")
    private String rootDir;

    @Value("${operation.mode}")
    private String operationMode;

    private String maxesFileLocation;

    String maxValuesRecord;

    String updateSymbol = "⚡";

    List<MaxValues> maxValuesList = new ArrayList<>();

    String today = LocalDate.now()
            .format(DateTimeFormatter.ofPattern(DD_MM_YYYY));

    public String infectedNumberIncreased(final List<Long> difference) {

        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(0) > maxValues.get(0).getConfirmedMaxValue() ||
                maxValues.get(0).getDate().equals(today) && difference.get(0).equals(maxValues.get(0).getConfirmedMaxValue())) {
            maxValues.get(0).setConfirmedMaxValue(difference.get(0));
            maxValues.get(0).setDate(today);
            maxValuesList.get(0).setConfirmedMaxValue(difference.get(0));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for infected field.");
            return updateSymbol;
        }
        return "";
    }

    public String healedNumberIncreased(final List<Long> difference) {

        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(1) > maxValues.get(0).getHealedMaxValue() ||
                maxValues.get(0).getDate().equals(today) && difference.get(1).equals(maxValues.get(0).getHealedMaxValue())) {
            maxValues.get(0).setHealedMaxValue(difference.get(1));
            maxValues.get(0).setDate(today);
            maxValuesList.get(0).setHealedMaxValue(difference.get(1));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for healed field.");
            return updateSymbol;
        }
        return "";
    }

    public String deathsNumberIncreased(final List<Long> difference) {

        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(2) > maxValues.get(0).getDeathsMaxValue() ||
                maxValues.get(0).getDate().equals(today) && difference.get(2).equals(maxValues.get(0).getDeathsMaxValue())) {
            maxValues.get(0).setDeathsMaxValue(difference.get(2));
            maxValues.get(0).setDate(today);
            maxValuesList.get(0).setDeathsMaxValue(difference.get(2));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for deaths field.");
            return updateSymbol;
        }
        return "";
    }

    private List<MaxValues> getMaxValues() {
        if (maxValuesList.isEmpty()) {
            maxValuesList = loadMaxValues();
        }
        return maxValuesList;
    }

    public List<MaxValues> loadMaxValues() {

        maxesFileLocation = rootDir + "/" + operationMode + "/" + maxValuesFilename;

        try {
            if (Paths.get(maxesFileLocation).toFile().exists()) {
                maxValuesRecord = new String(Files.readAllBytes(Paths.get(maxesFileLocation)));
                MaxValues[] maxValues = new Gson().fromJson(maxValuesRecord, MaxValues[].class);
                log.info("Successfully loaded maximum value information.");
                return Arrays.asList(maxValues);
            } else {
                Files.createFile(Paths.get(maxesFileLocation));
                return Collections.emptyList();
            }
        } catch (JsonSyntaxException | IOException ex) {
            log.error("Error occurred when trying to load JSON file with maximum values.", ex);
            return Collections.emptyList();
        }
    }

    public void writeMaxValues(final List<MaxValues> newMaxValues) {

        Path path = Paths.get(maxesFileLocation);
        Gson gson = new GsonBuilder().setDateFormat(DD_MM_YYYY).setPrettyPrinting().create();
        String json = gson.toJson(newMaxValues);
        List<String> lines = Arrays.asList(json.split(System.lineSeparator()));
        try {
            Files.write(path, lines);
        } catch (IOException ex) {
            log.error("Can't write max values to JSON.");
        }
    }
}
