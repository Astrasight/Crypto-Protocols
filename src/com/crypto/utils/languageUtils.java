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

    public static int[] stringToArray(String str) {
        String[] sarr = str.split(",");
        int[] out = new int[sarr.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = Integer.valueOf(sarr[i]);
        }
        return out;
    }
}
