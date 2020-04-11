package com.artemoons.covid19info.service.impl;

import com.artemoons.covid19info.dto.JsonItems;
import com.artemoons.covid19info.service.MessageFormatter;
import com.artemoons.covid19info.service.RosPotrebNadzorLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RosPotrebNadzor implements RosPotrebNadzorLoader {

    private MessageFormatter messageFormatter;

    @Autowired
    public RosPotrebNadzor(final MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    @Override
    public String parse(final JsonItems jsonItems) {
        return messageFormatter.prepareJsonMessageToSend(jsonItems);
    }
}
