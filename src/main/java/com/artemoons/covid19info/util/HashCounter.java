package com.artemoons.covid19info.util;

import com.artemoons.covid19info.dto.JsonItem;
import com.artemoons.covid19info.dto.JsonMessage;

import java.util.List;

public interface HashCounter {

    JsonMessage countRussiaStatistic(final List<JsonItem> jsonItems);
}
