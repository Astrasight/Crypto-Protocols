package com.crypto.utils;

import java.awt.EventQueue;
import java.io.Console;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.crypto.utils.mathUtils.*;

public class pollardMethod {
    private static final int C = 1;

    private static int f(int x) {
        return x * x + C;
    }

    public static void factor(int N)
    {
        if (N == 1)
            return;
        if (isPrime(N, 8))
        {
            System.out.println(N);
            return;
        }
        int divisor = rho(N);
        factor(divisor);
        factor(N / divisor);
    }

    private static int rho(int n) {
        int x1 = 2, x2 = 2, divisor;

        if (n % 2 == 0) return 2;

        do {
            x1 = f(x1) % n;
            x2 = f(f(x2)) % n;
            divisor = gcd(Math.abs(x1 - x2), n);
        } while (divisor == 1);
        return divisor;
    }


}
