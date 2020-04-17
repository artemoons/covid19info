package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.JsonItems;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class SiteLoader {

    private StopCoronaVirusRfLoader webPage;

    @Autowired
    public SiteLoader(final StopCoronaVirusRfLoader webPage) {
        this.webPage = webPage;
    }

    public String loadJson(final String url) {

        log.info("Starting to load data...");
        try (InputStream is = new URL(url).openStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            JsonItems jsonItems = gson.fromJson(reader, JsonItems.class);
            return webPage.parse(jsonItems);
        } catch (IOException ex) {
            log.error("Error occurred when trying to load API URL!", ex);
        }
        return "";
    }
}
