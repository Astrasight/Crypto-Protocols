package com.crypto.signatures;

import java.math.*;
import java.security.SecureRandom;
import java.util.Random;

public class RSASignatureClass {

    public static BigInteger[] RSAsignature(BigInteger m, BigInteger d, BigInteger n) {

        BigInteger key = m.modPow(d,n);
        // Алиса вычисляет подпись

        BigInteger[] signature = new BigInteger[2];
        signature[0] = m;
        signature[1] = key;

        return signature;
    }

    public static boolean RSAsignatureVerification(BigInteger message, BigInteger prototype) {

        if (message.equals(prototype)) return true;
        return false;

    }
}
