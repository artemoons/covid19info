package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.JsonItem;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SiteLoader {

    private RosPotrebNadzorLoader webPage;

    @Autowired
    public SiteLoader(final RosPotrebNadzorLoader webPage) {
        this.webPage = webPage;
    }

    public List<String> loadJson(final String url) {

        log.info("Starting to load data...");
        try (InputStream is = new URL(url).openStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            JsonItem[] jsonItems = gson.fromJson(reader, JsonItem[].class);
            List<JsonItem> jsonItemList = Arrays.asList(jsonItems);
            return webPage.parse(jsonItemList);
        } catch (IOException ex) {
            log.error("Error occurred when trying to load API URL!", ex);
        }
        return Collections.emptyList();
    }
}
