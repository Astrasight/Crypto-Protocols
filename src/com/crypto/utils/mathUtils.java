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

    public static long gcd(long a, long b) {
        if (a == 0)
            return b;                            // НОД(0, b) = b
        if (b == 0)
            return a;                            // НОД(a, 0) = a
        if (a == b)
            return a;                            // НОД(a, a) = a
        if (a == 1 || b == 1)
            return 1;                            // НОД(1, b) = НОД(a, 1) = 1
        if ((a & 1) == 0)                        // Если а — чётное, то…
            return ((b & 1) == 0)
                    ? gcd (a >> 1, b >> 1) << 1       // …если b — чётное, то НОД(a, b) = 2 * НОД(a / 2, b / 2)
                    : gcd (a >> 1, b);                // …если b — нечётное, то НОД(a, b) = НОД(a / 2, b)
        else                                     // Если a — нечётное, то…
            return ((b & 1) == 0)
                    ? gcd (a, b >> 1)                 // …если b — чётное, то НОД(a, b) = НОД(a, b / 2)
                    : gcd (b, a > b ? a - b : b - a); // …если b — нечётное, то НОД(a, b) = НОД(b, |a - b|)
    }
}
