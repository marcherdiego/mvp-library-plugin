package com.nerdscorner.mvp.utils;

import static com.intellij.codeInsight.template.impl.TemplateSettings.SPACE_CHAR;

public final class StringUtils {

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static String asCamelCase(String text) {
        text = replaceSpacesWithCamelCase(text);
        if (isEmpty(text)) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
    }

    public static String replaceSpacesWithCamelCase(String text) {
        if (isEmpty(text)) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean shouldCapitalizeLetter = false;
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if (letter == SPACE_CHAR) {
                shouldCapitalizeLetter = true;
            } else if (shouldCapitalizeLetter) {
                shouldCapitalizeLetter = false;
                stringBuilder.append(String.valueOf(letter).toUpperCase());
            } else {
                stringBuilder.append(String.valueOf(letter));
            }
        }
        return stringBuilder.toString();
    }
}
