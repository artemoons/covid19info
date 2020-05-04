package com.artemoons.covid19info.service;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonItems;

import java.util.List;

public interface StopCoronaVirusRfLoader {

    String parse(final List<JsonItem> jsonItems);

}
