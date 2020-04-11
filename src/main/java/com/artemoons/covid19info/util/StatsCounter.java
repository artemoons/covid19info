package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.dto.JsonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatsCounter {

    public static JsonMessage countTotalStatistic(final JsonItems jsonItems) {

        Long confirmed = 0L;

        Long recovered = 0L;

        Long deaths = 0L;

        Long tests = 0L;

        for (int i = 0; i < jsonItems.getItems().size(); i++) {
            confirmed += Long.parseLong(jsonItems.getItems().get(i).getConfirmed());
            recovered += Long.parseLong(jsonItems.getItems().get(i).getRecovered());
            deaths += Long.parseLong(jsonItems.getItems().get(i).getDeaths());
        }

        if (jsonItems.getItems().get(39).getLocationName().equals("No region speified")) {
            tests = Long.parseLong(jsonItems.getItems().get(39).getObservations());
            return new JsonMessage(tests, confirmed, recovered, deaths);
        } else {
            log.error("Can't get total amount of tests.");
            return new JsonMessage(0L, 0L, 0L, 0L);
        }


    }

}
