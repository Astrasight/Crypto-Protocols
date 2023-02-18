package com.crypto.signatures;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class elgamalSignatureClass {

    public static BigInteger[] elgamalSignature(BigInteger prime, BigInteger generator, BigInteger privExp, BigInteger doc) {

        BigInteger[] signature = new BigInteger[2];
        Random rng = new SecureRandom();
        // Алиса вычисляет временный ключ
        BigInteger e;
        while(true){
            e = new BigInteger(32, rng);
            if(e.compareTo(BigInteger.ONE) > 0 &&
                    e.compareTo(prime.subtract(BigInteger.ONE)) < 0 &&
                    e.gcd(prime.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
                break;
        }
        //  e = BigInteger.valueOf(213);

        // Алиса вычисляет подпись
        signature[0] = generator.modPow(e, prime);
        signature[1] = (doc.subtract(privExp.multiply(signature[0])))
                .multiply(e.modInverse(prime.subtract(BigInteger.ONE)))
                .mod(prime.subtract(BigInteger.ONE));
        return signature;
    }

    public static boolean elgamalVerification(BigInteger prime, BigInteger generator, BigInteger publicExp, BigInteger doc, BigInteger[] key){

        BigInteger part1 = (publicExp.modPow(key[0], prime).multiply(key[0].modPow(key[1], prime))).mod(prime);
        BigInteger part2 = generator.modPow(doc, prime);

        if(part1.equals(part2))
            return true;
        else
            return false;
    }

}
