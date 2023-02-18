package com.crypto.utils;

import java.awt.EventQueue;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class RSAutils {

    private final static BigInteger one = new BigInteger("1");
    private final static BigInteger zero = new BigInteger("0");
    private final static SecureRandom random = new SecureRandom();

    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        return phi;
    }

    public static BigInteger RSAgcd(BigInteger a, BigInteger b) {
        if (b.equals(zero)) {
            return a;
        } else {
            return RSAgcd(b, a.mod(b));
        }
    }

    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(zero)) return new BigInteger[] {
                a, one, zero
        }; // { a, 1, 0 }
        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger d = vals[0];
        BigInteger p = vals[2];
        BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
        return new BigInteger[] {
                d, p, q
        };
    }

    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e = new BigInteger(128, rand);
        do {
            e = new BigInteger(128, rand);
            while (e.min(phi).equals(phi)) { // пока phi меньше, чем e, вычисляем новый e
                e = new BigInteger(128, rand);
            }
        } while (!gcd(e, phi).equals(one)); // если gcd(e,phi) не равен 1, цикл будет далее выполняться
        return e;
    }

    public static BigInteger encrypt (BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public static BigInteger decrypt (BigInteger message, BigInteger d, BigInteger n) {
        return message.modPow(d, n);
    }

    public static BigInteger n (BigInteger p, BigInteger q) { return p.multiply(q); }

}
