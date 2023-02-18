package com.crypto.utils;

public class mathUtils {

    public static boolean isEven (int n) {
        return n % 2 == 0;
    }

    public static boolean isSquare(int n) {
        int i;
        for (i = 0; i <= n; ++i) {
            if (n == i * i) return true;
        }
        return false;
    }

    public static int nextSquare(int n) {
        int i;
        for (i = n;; ++i) {
            if (isSquare(i)) return i;
        }
    }

}
