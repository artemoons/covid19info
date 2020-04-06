package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SiteParser {

    private Message message = new Message();

    private MessageFormatter messageFormatter;

    @Value("${parser.last-update-info}")
    private String lastUpdateInfoString;

    @Value("${parser.statistic-number}")
    private String statisticNumberString;

    @Value("${parser.statistic-description}")
    private String statisticDescriptionString;

    @Autowired
    public SiteParser(final MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
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
            return parseWebPage(htmlPageContent);
        } catch (IOException ex) {
            log.error("Error when trying to load site! Message not sent", ex);
            return new StringBuilder("");
        }
    }

    private StringBuilder parseWebPage(final Document htmlPageContent) {

        message.setLastUpdateInformation(
                htmlPageContent.select(lastUpdateInfoString));
        message.setStatisticNumbers(
                htmlPageContent.select(statisticNumberString));
        message.setStatisticDescriptions(
                htmlPageContent.select(statisticDescriptionString));

        return messageFormatter.prepareMessageToSend(message);
    }
}
