package com.crypto.utils;

import static com.crypto.utils.languageUtils.*;

public class arithmeticOperations {
    public static int add(int summand, int addend) {
        int carry	= 0x00;

        while(addend != 0x00)
        {
            carry	= (summand & addend);
            summand	= summand ^ addend;
            addend	= (carry << 1);
        }
        return summand;
    }

    public static int substract(int minuend, int subtrahend) {
        int borrow	= 0x00;

        while(subtrahend != 0x00)
        {
            borrow = ((~minuend) & subtrahend);
            minuend = minuend ^ subtrahend;
            subtrahend = (borrow << 1);
        }
        return minuend;
    }

    public static int multiply(int mul1, int mul2) {
        int result = 0;
        while(mul2 != 0)
        {
            if ((mul2 & 1) == 1)
            {
                result = add(result, mul1);
            }
            mul1 <<=  1;
            mul2 >>>= 1;
        }
        return result;
    }

    public static int divide (int dividend, int divisor) {

        int sign = ((dividend < 0) ^ (divisor < 0)) ? -1 : 1;

        dividend = Math.abs(dividend);
        divisor = Math.abs(divisor);

        int quotient = 0;

        while (dividend >= divisor) {
            dividend -= divisor;
            ++quotient;
        }

        if (sign == -1)
            quotient = -quotient;

        return quotient;
    }

    public static int multiplyKaratsuba(int x, int y) {
        int size1 = getSize(x);
        int size2 = getSize(y);

        int N = Math.max(size1, size2);

        if (N < 10)
            return x * y;

        N = (N / 2) + (N % 2);

        int m = (int)Math.pow(10, N);


        int b = x / m;
        int a = x - (b * m);
        int d = y / m;
        int c = y - (d * N);

        int z0 = multiply(a, c);
        int z1 = multiply(a + b, c + d);
        int z2 = multiply(b, d);

        return z0 + ((z1 - z0 - z2) * m) + (z2 * (int)(Math.pow(10, 2 * N)));
    }
}
