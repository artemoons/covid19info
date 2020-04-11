package com.artemoons.covid19info.service.impl;

import com.artemoons.covid19info.dto.Message;
import com.artemoons.covid19info.service.MessageFormatter;
import com.artemoons.covid19info.service.StopCoronaVirusRfLoader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StopCoronaVirusRf implements StopCoronaVirusRfLoader {

    private Message message = new Message();

    private MessageFormatter messageFormatter;

    @Value("${parser.last-update-info}")
    private String lastUpdateInfoString;

    @Value("${parser.statistic-number}")
    private String statisticNumberString;

    @Value("${parser.statistic-description}")
    private String statisticDescriptionString;

    @Autowired
    public StopCoronaVirusRf(final MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    @Override
    public StringBuilder parse(final Document htmlPageContent) {
        try {
            message.setLastUpdateInformation(
                    htmlPageContent.select(lastUpdateInfoString));
            message.setStatisticNumbers(
                    htmlPageContent.select(statisticNumberString));
            message.setStatisticDescriptions(
                    htmlPageContent.select(statisticDescriptionString));
        } catch (Selector.SelectorParseException ex) {
            log.error("Error occurred while parsing web page contents.", ex);
        }
        return messageFormatter.prepareMessageToSend(message);
    }
}
