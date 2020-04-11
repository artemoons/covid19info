package com.artemoons.covid19info.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SiteParser {

    private StopCoronaVirusRfLoader webPage;

    @Autowired
    public SiteParser(final StopCoronaVirusRfLoader webPage) {
        this.webPage = webPage;
    }

    public StringBuilder loadSite(final StringBuilder websiteUrl) {
        try {
            log.info("Starting to load page {}...", websiteUrl);
            Document htmlPageContent = Jsoup.connect(websiteUrl.toString())
                    .ignoreHttpErrors(true)
                    .timeout(120 * 1000)
                    .get();
            log.debug("Web page content: {}", htmlPageContent.html());
            log.info("Web page successfully loaded!");
            return webPage.parse(htmlPageContent);
        } catch (IOException ex) {
            log.error("Error when trying to load site! Message not sent", ex);
            return new StringBuilder("");
        }
    }
}
