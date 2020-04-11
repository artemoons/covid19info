package com.artemoons.covid19info.helper;

public class MarkdownFormatter {

    private MarkdownFormatter() {
    }

    public static String setBold(final String text) {
        return "*" + text + "*";
    }

    public static String setItalic(final String text) {
        return "_" + text + "_";
    }
}
