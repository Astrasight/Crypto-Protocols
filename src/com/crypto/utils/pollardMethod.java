package com.crypto.utils;

import java.awt.EventQueue;
import java.io.Console;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.crypto.utils.mathUtils.*;

public class pollardMethod {

    private static final int C = 1;
    private static final int p = 1018;
    private static final int x = 2;
    private static final int y = 5;
    private static final int P = p + 1;

    private static int f(int x) {
        return x * x + C;
    }
    public static int getX() { return x; }
    public static int getp() { return p; }

    private static int rhoFact(int n) {
        int x1 = 2, x2 = 2, divisor;

        if (n % 2 == 0) return 2;

        do {
            x1 = f(x1) % n;
            x2 = f(f(x2)) % n;
            divisor = gcd(Math.abs(x1 - x2), n);
        } while (divisor == 1);
        return divisor;
    }

    private static int modPollardRho(int base, int exp, int mod) {
        int res = 1;

        while (exp > 0) {
            if (exp % 2 == 1) res = (res * base) % mod;

            exp = exp >> 1;

            base = (base * base) % mod;
        }
        return res;
    }

    public static int rhoLog(int n) {
        Random randObj = new Random();

        if (n == 1) return n;

        //if number is even then 2
        if (n % 2 == 0) return 2;

        int x = (int)(randObj.nextLong() % (n - 2)) + 2;
        int y = x;

        int c = (int)(randObj.nextLong()) % (n - 1) + 1;

        int d = 1;

        while (d == 1) {

            x = (modPollardRho(x, 2, n) + c + n) % n;

            y = (modPollardRho(y, 2, n) + c + n) % n;
            y = (modPollardRho(y, 2, n) + c + n) % n;

            d = gcd(Math.abs(x - y), n);

            if (d == n) return rhoLog(n);
        }

        return d;
    }

    public static void factorRho(int N)
    {
        if (N == 1)
            return;
        if (isPrime(N, 8))
        {
            System.out.println(N);
            return;
        }
        int divisor = rhoFact(N);
        factorRho(divisor);
        factorRho(N / divisor);
    }
}
