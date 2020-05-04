package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.dto.JsonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StatsCounter {

    public static JsonMessage countTotalStatistic(final List<JsonItem> jsonItems) {

        Long confirmed = 0L;

        Long recovered = 0L;

        Long deaths = 0L;

        Long active = 0L;

        for (int i = 0; i < jsonItems.size(); i++) {
            confirmed += Long.parseLong(jsonItems.get(i).getConfirmed());
            recovered += Long.parseLong(jsonItems.get(i).getRecovered());
            deaths += Long.parseLong(jsonItems.get(i).getDeaths());
        }
        active = confirmed - recovered - deaths;

        return new JsonMessage(confirmed, recovered, deaths, active);
    }
}
