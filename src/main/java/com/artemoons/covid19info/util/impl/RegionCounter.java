package com.artemoons.covid19info.util.impl;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonMessage;
import com.artemoons.covid19info.util.StatisticCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("Counter")
@ConditionalOnProperty(prefix = "operation", name = "mode", havingValue = "region")
public class RegionCounter implements StatisticCounter {

    @Value("${operation.region-name}")
    private String regionName;

    public JsonMessage countStatistic(final List<JsonItem> jsonItems) {

        Long confirmed = 0L;

        Long recovered = 0L;

        Long deaths = 0L;

        Long active = 0L;

        for (int i = 0; i < jsonItems.size(); i++) {
            if (jsonItems.get(i).getLocationName().equals(regionName)) {
                confirmed = Long.parseLong(jsonItems.get(i).getConfirmed());
                recovered = Long.parseLong(jsonItems.get(i).getRecovered());
                deaths = Long.parseLong(jsonItems.get(i).getDeaths());
                active = confirmed - recovered - deaths;
                return new JsonMessage(confirmed, recovered, deaths, active);
            }
        }
        log.error("Can't get stats for city. Check region index.");
        return new JsonMessage(0L, 0L, 0L, 0L);
    }

}
