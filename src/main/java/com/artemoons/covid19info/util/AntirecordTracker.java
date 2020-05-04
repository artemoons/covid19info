package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.MaxValues;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AntirecordTracker {

    String maxValuesFilename = "max-value-info.json";

    String maxValuesRecord;

    String updateSymbol = "⚡";

    List<MaxValues> maxValuesList = new ArrayList<>();

    public List<MaxValues> loadMaxValues() {

        try {
            if (Paths.get(maxValuesFilename).toFile().exists()) {
                maxValuesRecord = new String(Files.readAllBytes(Paths.get(maxValuesFilename)));
                MaxValues[] maxValues = new Gson().fromJson(maxValuesRecord, MaxValues[].class);
                log.info("Successfully loaded maximum value information.");
                return Arrays.asList(maxValues);
            } else {
                Files.createFile(Paths.get(maxValuesFilename));
                return Collections.emptyList();
            }
        } catch (JsonSyntaxException | IOException ex) {
            log.error("Error occurred when trying to load JSON file with maximum values.", ex);
            return Collections.emptyList();
        }
    }

    public void writeMaxValues(final List<MaxValues> newMaxValues) {

        Path path = Paths.get(maxValuesFilename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(newMaxValues);
        List<String> lines = Arrays.asList(json.split(System.lineSeparator()));
        try {
            Files.write(path, lines);
        } catch (IOException ex) {
            log.error("Can't write max values to JSON.");
        }
    }

    private List<MaxValues> getMaxValues() {
        if (maxValuesList.isEmpty()) {
            maxValuesList = loadMaxValues();
        }
        return maxValuesList;
    }

    public String infectedNumberIncreased(final List<Long> difference) {
        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(0) > maxValues.get(0).getConfirmedMaxValue()) {
            maxValues.get(0).setConfirmedMaxValue(difference.get(0));
            maxValuesList.get(0).setConfirmedMaxValue(difference.get(0));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for infected field.");
            return updateSymbol;
        }
        return "";
    }

    public String healedNumberIncreased(final List<Long> difference) {
        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(1) > maxValues.get(0).getHealedMaxValue()) {
            maxValues.get(0).setHealedMaxValue(difference.get(1));
            maxValuesList.get(0).setHealedMaxValue(difference.get(1));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for healed field.");
            return updateSymbol;
        }
        return "";
    }

    public String deathsNumberIncreased(final List<Long> difference) {
        List<MaxValues> maxValues = getMaxValues();
        if (difference.get(2) > maxValues.get(0).getDeathsMaxValue()) {
            maxValues.get(0).setDeathsMaxValue(difference.get(2));
            maxValuesList.get(0).setDeathsMaxValue(difference.get(2));
            writeMaxValues(maxValues);
            log.info("Updated maximum value for deaths field.");
            return updateSymbol;
        }
        return "";
    }
}
