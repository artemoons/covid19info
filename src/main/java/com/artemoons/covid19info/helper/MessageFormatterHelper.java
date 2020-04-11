package com.artemoons.covid19info.helper;

import org.jsoup.nodes.Element;

public class MessageFormatterHelper {

    public static String getText(final Element message) {
        return message.text().trim().replace(" ", "");
    }

    public static String setSign(final Long number) {
        return number > 0 ? "+" + number : String.valueOf(number);
    }
}
