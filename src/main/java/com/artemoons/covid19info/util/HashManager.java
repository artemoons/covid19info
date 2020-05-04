package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.dto.JsonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HashManager {

    private CacheManager cacheManager;

    @Autowired
    public HashManager(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Long calculateJsonHash(final List<JsonItem> jsonItems) {
        JsonMessage statistic = StatsCounter.countTotalStatistic(jsonItems);
        Long newHash = statistic.getInfectedOverall()
                + statistic.getHealedOverall()
                + statistic.getDeathsOverall();
        log.info("New message hash: {}", newHash);
        return newHash;
    }

    public boolean previousJsonHashIsDifferent(final List<JsonItem> newHash) {
        return Boolean.FALSE.equals(getJsonHashFromHistoryAndCompareTo(calculateJsonHash(newHash)));
    }

    private Boolean getJsonHashFromHistoryAndCompareTo(final Long hash) {

        Long cache = Long.valueOf(cacheManager.getCache());
        if (cache.compareTo(hash) == 0 || cache.compareTo(hash) > 0) {
            log.info("Saved cache equals or more than new value.");
            return true;
        }
        log.info("Last parse result info: cache - {}, hash - {}", cache, hash);
        log.info("Saved cache different from new value.");
        return false;
    }

    public void saveLastParseJsonResultInfoHash(final Long message) {
        cacheManager.updateJsonCache(message);
    }

}
