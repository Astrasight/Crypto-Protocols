package com.crypto.utils;

public class languageUtils {
    public static int findLetterPosition(String key, char c) {

        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == c) {
                return i;
            }

        }
        return 0;
    }
}
