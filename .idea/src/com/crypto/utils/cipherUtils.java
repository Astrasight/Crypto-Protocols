package com.crypto.utils;

import java.math.BigInteger;
import java.math.*;
import java.util.Random;

public class cipherUtils {

    public static BigInteger stringCipher(String message) {
        message = message.toUpperCase();
        String cipherString = "";
        int i = 0;
        while (i < message.length()) {
            int ch = (int) message.charAt(i);
            cipherString = cipherString + ch;
            i++;
        }
        BigInteger cipherBig = new BigInteger(String.valueOf(cipherString));
        return cipherBig;
    }

    public static String cipherToString(BigInteger message) {
        String cipherString = message.toString();
        String output = "";
        int i = 0;
        while (i < cipherString.length()) {
            int temp = Integer.parseInt(cipherString.substring(i, i + 2));
            char ch = (char) temp;
            output = output + ch;
            i = i + 2;
        }
        return output;
    }

    public static boolean isPrime(BigInteger number) {
        //проверка через дефолтную функцию BigInteger.isProbablePrime(certainty)
        if (!number.isProbablePrime(5))
            return false;

        //проверка, если чётное
        BigInteger two = new BigInteger("2");
        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two)))
            return false;

        //проверка всех делителей от 3 до number
        for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) { //стартуем от 3, 5 и так далее по нечетным, и чекаем делители
            if (BigInteger.ZERO.equals(number.mod(i))); //чекаем является ли i делителем number
        }
        return true;
    }

    public static BigInteger power(BigInteger a, BigInteger b, BigInteger p) {
        if (b.compareTo(BigInteger.ONE) == 0)
            return a;
        else {
            int e = b.intValue();
            return (((BigInteger) a.pow(e))).mod(p);
        }
    }

    public static BigInteger largePrime(int bits) {
        Random randomInteger = new Random();
        BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger);
        return largePrime;
    }

    public static boolean bigIntOdd(BigInteger val) {
        if (!val.mod(new BigInteger("2")).equals(BigInteger.ZERO))
            return true;
        return false;
    }

    public static String shift(String targetWord, String alphabet, int key) {
        String result = "";
        for (int i = 0; i < targetWord.length(); ++i) {
            for (int j = 0; j < alphabet.length(); ++j) {
                if (targetWord.charAt(i) == alphabet.charAt(j)) {
                    int temp = j + key;
                    while (temp >= alphabet.length())
                    {
                        temp -= alphabet.length();
                    }
                    result += alphabet.charAt(temp);
                }
            }
        }
        return result;
    }
}
