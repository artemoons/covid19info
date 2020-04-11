package com.artemoons.covid19info.helper;

public class MessageFormatterHelper {

    public static String setSign(final Long number) {
        return number > 0 ? "+" + number : String.valueOf(number);
    }
}
