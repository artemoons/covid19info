package com.artemoons.covid19info.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
public class CacheManager {

    @Value("${telegram.sync-file-name}")
    private String syncFileName;

    private String cachedValue;

    public String getCache() {

        if (!hasText(cachedValue)) {
            cachedValue = loadCache();
        }
        return cachedValue;
    }

    private String loadCache() {

        try {
            if (Paths.get(syncFileName).toFile().exists()) {
                cachedValue = new String(Files.readAllBytes(Paths.get(syncFileName)));
                log.info("Loaded cache from file.");
            } else {
                Files.createFile(Paths.get(syncFileName));
                log.info("Cache file not found. Created new one.");
            }
        } catch (IOException ex) {
            log.error("Can't load cache file.", ex);
        }
        return cachedValue;
    }

    public void updateCache(final String message) {

        Path path = Paths.get(syncFileName);

        try {
            Files.write(path, message.getBytes());
            cachedValue = message;
            log.info("Successfully updated cache file!");
        } catch (IOException ex) {
            log.error("Can't write cache file.", ex);
        }
    }

}
