package com.artemoons.covid19info.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class SiteParser {

    @Value("${telegram.sync-file-name}")
    private String syncFileName;

    //    @EventListener(ApplicationReadyEvent.class)
    public String loadSite(final String websiteUrl) {
        try {
            log.info("Starting to load page {}...", websiteUrl);
            Document htmlPageContent = Jsoup.connect(websiteUrl)
                    .ignoreHttpErrors(true)
                    .timeout(120 * 1000)
                    .get();
            log.debug("Web page content: {}", htmlPageContent.html());
            log.info("Web page successfully loaded!");
            return parseInformationFromWebPage(htmlPageContent);
        } catch (IOException ex) {
            log.error("Error when trying to load site!", ex);
            //todo what happens here? (avoid returning null value)
            return new Document(websiteUrl).location();
        }
    }

    private String parseInformationFromWebPage(final Document htmlPageContent) {

        StringBuilder message = new StringBuilder();

        Elements lastUpdateInformation = htmlPageContent.select("div.cv-banner__top > div.cv-banner__description");
        Elements statisticNumbers = htmlPageContent.select("div.cv-countdown__item > div.cv-countdown__item-value");
        Elements statisticDescriptions = htmlPageContent.select("div.cv-countdown__item > div.cv-countdown__item-label");

        log.info(lastUpdateInformation.get(0).text());
        message.append("*")
                .append(lastUpdateInformation.get(0).text())
                .append("*")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (int i = 0; i < statisticNumbers.size(); i++) {
            log.info(statisticNumbers.get(i).text() + " " + statisticDescriptions.get(i).text().toLowerCase());
            message.append(statisticNumbers.get(i).text())
                    .append(" ")
                    .append(statisticDescriptions.get(i).text().toLowerCase())
                    .append(System.lineSeparator());
        }

        if (Boolean.FALSE.equals(parsedResultEqualsNewResult(message))) {
            saveLastParseResultInfo(message);
            return message.toString();
        } else {
            log.info("No difference between new and old parse information.");
            return "";
        }
    }

    private void saveLastParseResultInfo(final StringBuilder message) {
        Path path = Paths.get(syncFileName);
        try {
            Files.write(path, String.valueOf(message.toString().hashCode()).getBytes());
        } catch (IOException ex) {
            log.error("Can't write to file.", ex);
        }
    }

    private Boolean parsedResultEqualsNewResult(final StringBuilder message) {

        String savedLastParseResult;

        try {
            if (Paths.get(syncFileName).toFile().exists()) {
                savedLastParseResult = new String(Files.readAllBytes(Paths.get(syncFileName)));
            } else {
                Files.createFile(Paths.get(syncFileName));
                savedLastParseResult = "";
            }
            if (savedLastParseResult.equals(String.valueOf(message.toString().hashCode()))) {
                log.info("Saved parsed result equals new value.");
                return true;
            }
            log.debug("Last parse result info: {}", savedLastParseResult);
        } catch (IOException ex) {
            log.error("Can't open file with last parsed result", ex);
        }
        log.info("Saved parsed result different from new value.");
        return false;
    }

}
