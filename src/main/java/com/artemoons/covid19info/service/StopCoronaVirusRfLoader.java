package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.JsonItems;

public interface StopCoronaVirusRfLoader {

    String parse(final JsonItems jsonItems);

}
