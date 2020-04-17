package com.artemoons.covid19info.service.impl;

import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.service.MessageFormatter;
import com.artemoons.covid19info.service.StopCoronaVirusRfLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StopCoronaVirusRf implements StopCoronaVirusRfLoader {

    private MessageFormatter messageFormatter;

    @Autowired
    public StopCoronaVirusRf(final MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    @Override
    public String parse(final JsonItems jsonItems) {
        return messageFormatter.prepareJsonMessageToSend(jsonItems);
    }
}