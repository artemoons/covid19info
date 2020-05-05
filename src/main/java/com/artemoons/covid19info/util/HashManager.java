package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HashManager {

    private CacheManager cacheManager;

    private HashCounter hashCounter;

    @Autowired
    public HashManager(final CacheManager cacheManager,
                       final HashCounter hashCounter) {
        this.cacheManager = cacheManager;
        this.hashCounter = hashCounter;
    }

    public Long calculateJsonHash(final List<JsonItem> jsonItems) {
        JsonMessage statistic = hashCounter.countRussiaStatistic(jsonItems);
        Long newHash = statistic.getInfectedOverall()
                + statistic.getHealedOverall()
                + statistic.getDeathsOverall();
        log.info("New message hash: {}", newHash);
        return newHash;
    }

    public boolean previousJsonHashIsDifferent(final List<JsonItem> newMessage) {
        return Boolean.FALSE.equals(getJsonHashFromHistoryAndCompareTo(calculateJsonHash(newMessage)));
    }

    private Boolean getJsonHashFromHistoryAndCompareTo(final Long hash) {

        Long cache = Long.valueOf(cacheManager.getCache());
        if (cache.compareTo(hash) == 0 || cache.compareTo(hash) > 0) {
            log.info("New cache is less or equals old cache value.");
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
