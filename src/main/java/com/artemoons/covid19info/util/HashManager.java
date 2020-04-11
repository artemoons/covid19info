package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HashManager {

    private CacheManager cacheManager;

    @Autowired
    public HashManager(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String calculateHash(final Message message) {
        List<String> messageLines = new ArrayList<>();
        for (int i = 0; i < message.getStatisticDescriptions().size(); i++) {
            messageLines.add(message.getStatisticNumbers().get(i).text()
                    + " " + message.getStatisticDescriptions().get(i).text().toLowerCase());
        }
        return String.valueOf(messageLines.hashCode());
    }

    public boolean previousHashIsDifferent(final String message) {
        return Boolean.FALSE.equals(getHashFromHistoryAndCompareTo(message));
    }

    private Boolean getHashFromHistoryAndCompareTo(final String message) {

        String cache = cacheManager.getCache();
        if (cache.equals(message)) {
            log.info("Saved cache equals new value.");
            return true;
        }
        log.debug("Last parse result info: {}", cache);
        log.info("Saved cache different from new value.");
        return false;
    }

    public void saveLastParseResultInfoHash(final String message) {
        cacheManager.updateCache(message);
    }

}
