package com.crypto.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;

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

    public static boolean isPrime(int n, int k) {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        while (k > 0) {
            int a = 2 + (int)(Math.random() % (n - 4));

            if (power(a, n - 1, n) != 1) return false;

            --k;
        }
        return true;
    }

    public static boolean isPrimeMiller(int n, int k) {
        if (n <= 1 || n == 4)
            return false;
        if (n <= 3)
            return true;

        int d = n - 1;

        while (d % 2 == 0)
            d /= 2;

        for (int i = 0; i < k; i++)
            if (!millerRabin(d, n))
                return false;

        return true;
    }

    public static int power(int a, int n, int p) {
        int res = 1;
        a = a % p;

        while (n > 0) {
            if ((n & 1) == 1) res = (res * a) % p;

            n = n >> 1;
            a = (a * a) % p;
        }
        return res;
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

    public static int[] extendedBinary_gcd(int a, int b) {
        if (a == 0) return new int[] {1, 1, b};
        if (b == 0) return new int[] {1, 1, a};
        if ((a & 1) == 0 && (b & 1) == 0) {
            int[] res = extendedBinary_gcd(a >> 1, b >> 1);
            return new int[] {res[0], res[1], res[2] << 1};
        } else if ((a & 1) == 0) {
            int[] res = extendedBinary_gcd(a >> 1, b);
            if ((res[0] & 1) == 1) {
                return new int[] {(res[0] - b) >> 1, res[1] + (a >> 1), res[2]};
            } else {
                return new int[] {res[0] >> 1, res[1], res[2]};
            }
        } else if ((b & 1) == 0) {
            int[] res = extendedBinary_gcd(a, b >> 1);
            if ((res[1] & 1) == 1) {
                return new int[] {res[0] + (b >> 1), (res[1] - a) >> 1, res[2]};
            } else {
                return new int[] {res[0], res[1] >> 1, res[2]};
            }
        } else if (b > a) {
            int[] res = extendedBinary_gcd(a, b - a);
            return new int[] {res[0] - res[1], res[1], res[2]};
        } else {
            int[] res = extendedBinary_gcd(a - b, b);
            return new int[] {res[0], res[1] - res[0], res[2]};
        }
    }

    public static int extended_gcd(int a, int b, AtomicInteger x, AtomicInteger y) {
        if (a == 0) {
            x.set(1);
            y.set(1);
            return b;
        } else if (b == 0) {
            x.set(1);
            y.set(1);
            return a;
        }

        AtomicInteger _x = new AtomicInteger(), _y = new AtomicInteger();
        int gcd = extended_gcd(b % a, a, _x, _y);

        x.set(_y.get() - (b/a) * _x.get());
        y.set(_x.get());

        return gcd;
    }

    public static int calculateJacobi(int a, int n) {
        assert(n > a && a > 0 && n % 2 == 1);
        int t = 1;
        while (a != 0) {
            while (a % 2 == 0) {
                a /= 2;
                int r = n % 8;
                if (r == 3 || r == 5) {
                    t = -t;
                }
            }
            int temp = a;
            a = n;
            n = temp;
            if (a % 4 == n % 4 && n % 4 == 3) {
                t = -t;
            }
            a %= n;
        }
        if (n == 1) {
            return t;
        } else {
            return 0;
        }
    }

    public static boolean solovayStrassen(int p, int iteration) {
        if (p < 2) return false;
        if (p != 2 && p % 2 == 0) return false;

        Random rand = new Random();

        for (int i = 0; i < iteration; ++i) {
            int r = Math.abs(rand.nextInt());
            int a = r % (p - 1) + 1;
            int jacobian = (p + calculateJacobi(a, p)) % p;
            int mod = power(a, (p - 1) / 2, p);

            if (jacobian == 0 || mod != jacobian) return false;
        }
        return true;
    }

    public static boolean millerRabin(int d, int n) {
        int a = 2 + (int)(Math.random() % (n - 4));
        int x = power(a, d, n);

        if (x == 1 || x == n - 1) return true;

        while (d != n - 1) {
            x = (x * x) % n;
            d *= 2;

            if (x == 1)
                return false;
            if (x == n - 1)
                return true;
        }
        return false;
    }
}