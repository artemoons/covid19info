package com.artemoons.covid19info.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SiteParser {

    @EventListener(ApplicationReadyEvent.class)
    public String loadSite() throws IOException {
        String url = "http://xn--80aesfpebagmfblc0a.xn--p1ai/";
        try {
            log.info("Starting to load page {}...", url);
//            Document htmlPageContent = Jsoup.connect(url).get();
            String html = "\t\t\t\t<div class=\"cv-countdown\">\n" +
                    "\t\t\t\t\t<div class=\"cv-countdown__item\">\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-value\"><span>>536 тыс.</span></div>\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-label\">Проведено тестов</div>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t<div class=\"cv-countdown__item\">\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-value _accent\"><span>3 548</span></div>\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-label\">Случаев заболевания</div>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t<div class=\"cv-countdown__item\">\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-value _accent\"><span>771</span></div>\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-label\">Случай заболевания<br> за последние сутки</div>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t<div class=\"cv-countdown__item\">\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-value\"><span>235</span></div>\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-label\">\n" +
                    "\t\t\t\t\t\t\tЧеловек\n" +
                    "\t\t\t\t\t\t\tвыздоровело</div>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t<div class=\"cv-countdown__item\">\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-value\"><span>30</span></div>\n" +
                    "\t\t\t\t\t\t<div class=\"cv-countdown__item-label\">\n" +
                    "\t\t\t\t\t\t\tЧеловек\n" +
                    "\t\t\t\t\t\t\tумерло</div>\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t</div>";
            Document htmlPageContent = Jsoup.parse(html);
            log.debug("Web page content: {}", htmlPageContent.html());
            log.info("Web page successfully loaded!");
            return parseInformationFromWebPage(htmlPageContent);
//        } catch (IOException ex) {
        } catch (Exception ex) {
            log.error("Error when trying to load site!", ex);
            //todo do not return null
            return null;
        }
    }


    private String parseInformationFromWebPage(final Document htmlPageContent) {

        Elements statisticNumbers = htmlPageContent.select("div.cv-countdown__item > div.cv-countdown__item-value");
        Elements statisticDescription = htmlPageContent.select("div.cv-countdown__item > div.cv-countdown__item-label");
        for (int i = 0; i < statisticNumbers.size(); i++) {
            log.info(statisticNumbers.get(i).text() + " - " + statisticDescription.get(i).text());
        }
        return "done";
    }

}
