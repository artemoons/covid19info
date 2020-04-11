package com.artemoons.covid19info.service;

import org.jsoup.nodes.Document;

public interface StopCoronaVirusRfLoader {

    StringBuilder parse(final Document document);

}
