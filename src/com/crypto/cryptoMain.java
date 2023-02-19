package com.crypto;

import static com.crypto.signatures.RSASignatureClass.*;
import static com.crypto.signatures.elgamalSignatureClass.*;
import static com.crypto.utils.cipherUtils.*;
import static com.crypto.utils.RSAutils.*;
import static com.crypto.signatures.blindSignatureClass.*;
import static java.lang.System.in;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class cryptoMain {
    private final static BigInteger one = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz ";

    public static void main(String[] args) throws Exception {

        cryptoInvokes myObj = new cryptoInvokes();
        Method[] methods = cryptoInvokes.class.getDeclaredMethods();

        for(Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                method.invoke(myObj);
            }
        }

        System.out.println("1 - Diffie-Hellman \n2 - El-Gamal\n3 - El-Gamal Signature\n4 - RSA\n5 - RSA Signature\n6 - Blind Signature RSA\n");
        Scanner scan = new Scanner(in);
        while (!scan.hasNextInt()) {
            scan.next();
        }
        int s = scan.nextInt();
        switch (s) {
            case 1:
                diffieHellman();
                break;
            case 2:
                elgamalFunc();
                break;
            case 3:
                elgamalSignatureFunc();
                break;
            case 4:
                RSA();
                break;
            case 5:
                RSAsignatureFunc();
                break;
            case 6:
                blindSignatureFunc();
                break;
        }
    }

    public static void diffieHellman() {

        System.out.println("Diffie Hellman algorithm");

        BigInteger P, G, x, a, y, b, ka, kb;

        P = largePrime(8);
        System.out.println("the value of P: " + P);

        G = largePrime(8);
        System.out.println("the value of  G: " + G);

        a = largePrime(8);
        System.out.println("closed key for Alice: " + a);

        x = powerBigInt(G, a, P);

        b = largePrime(8);
        ;
        System.out.println("closed key for Bob: " + b);

        y = powerBigInt(G, b, P);

        ka = powerBigInt(y, a, P);
        kb = powerBigInt(x, b, P);

        System.out.println("Secret key for the Alice is: " + ka);
        System.out.println("Secret key for the Bob is: " + kb);
    }

    public static void elgamalFunc() {
        BigInteger p, b, c, secretKey;
        secretKey = largePrime(16);
        //
        // вычисление открытого ключа
        //
        System.out.println("secretKey = " + secretKey);
        p = BigInteger.probablePrime(256, random);
        b = largePrime(16);
        c = b.modPow(secretKey, p);
        System.out.println("p = " + p);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        //
        // шифрование
        //
        String s = "sample text with the particular size";
        System.out.println("plain text: " + s);
        BigInteger X = stringCipher(s);
        BigInteger r = new BigInteger(64, random);
        BigInteger EC = X.multiply(c.modPow(r, p)).mod(p);
        BigInteger brmodp = b.modPow(r, p);
        System.out.println("message = " + X);
        System.out.println("integer r such that 1 < r < (p − 1) ---> r = " + r);
        //System.out.println("EC = " + EC);
        System.out.println("first parth of the encrypted message b^r mod p = " + brmodp);
        //
        // дешифрование
        //
        BigInteger crmodp = brmodp.modPow(secretKey, p);
        BigInteger d = crmodp.modInverse(p);
        BigInteger ad = d.multiply(EC).mod(p);
        System.out.println("second part of the encrypted message c^r mod p = " + crmodp);
        //System.out.println("d = " + d);
        String decryptedMessage = cipherToString(ad);
        System.out.println("deciphered text: " + decryptedMessage);
    }

    public static void elgamalSignatureFunc() {

        // Алиса выбирает q и вычисляет p = 2q + 1
        BigInteger q = BigInteger.probablePrime(32, random);
        BigInteger p = q.multiply(BigInteger.valueOf(2)).add(one);
        while (!isPrimeBigInt(q)) {
            q = BigInteger.probablePrime(32, random);
        }
        System.out.println("q = " + q);
        while (!isPrimeBigInt(p)) {
            q = BigInteger.probablePrime(32, random);
            p = q.multiply(BigInteger.valueOf(2)).add(one);
        }
        // BigInteger p = BigInteger.valueOf(467);
        System.out.println("p = " + p);

        // Алиса вычисляет g, примитивный корень по модулю p
        BigInteger g;

        while (true) {
            g = new BigInteger(32, random);
            if (g.compareTo(one) > 0 &&
                    g.compareTo(p) < 0 &&
                    !(g.multiply(g).mod(p).equals(one)) &&
                    !(g.modPow(q, p).equals(one)))
                break;
        }
        //  g = BigInteger.valueOf(2);

        // Алиса вычисляет её закрытый ключ
        BigInteger s;

        while (true) {
            s = new BigInteger(32, random);
            if (s.compareTo(one) > 0 &&
                    s.compareTo(p.subtract(one)) < 0)
                break;
        }
        //  s = BigInteger.valueOf(127);

        // Алиса вычисляет её открытый ключ
        BigInteger v = g.modPow(s, p);
        System.out.println("Alice's public key: " + v);

        // Алиса выбирает её сообщение, m
        String message = "sample text with the particular size";
        System.out.println(message);
        BigInteger m = stringCipher(message);

        // Алиса подписывает её сообщение
        BigInteger[] key = elgamalSignature(p, g, s, m);
        System.out.println("signature: ");
        for (int i = 0; i < key.length; ++i) {
            System.out.println(key[i]);
        }

        // Боб проверяет подпись
        boolean result = elgamalVerification(p, g, v, m, key);

        String str = (result == true) ? "the signature has been verified" : "the signature has not been verified";
        System.out.println(str);
    }

    public static void RSA() {

        // большие числа p и q, случайные
        BigInteger p = largePrime(128);
        BigInteger q = largePrime(128);

        // далее при помощи p и q вычислим n
        // n - модуль для закрытого и открытого ключей, n-ное колво бит это длина ключа
        BigInteger n = n(p, q);

        // вычисление функции эйлера
        // она, как известно, равна phi(n) = (p-1)(q-1)
        BigInteger phi = getPhi(p, q);

        // далее найти такой е, чтобы 1 < e < phi(n)    и чтобы gcd(e,phi) = 1, т.е совместно простые e и phi
        BigInteger e = genE(phi);

        // далее вычислить d, для которого d ≡ e^(-1) (mod Phi(n))
        BigInteger d = extEuclid(e, phi)[1];

        // выписываем значения в консоль
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + n);
        System.out.println("Phi: " + phi);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

        String message = "sample text with particular size";
        BigInteger cipherMessage = stringCipher(message);
        BigInteger encrypted = encrypt(cipherMessage, e, n);
        BigInteger decrypted = decrypt(encrypted, d, n);
        String restoredMessage = cipherToString(decrypted);

        System.out.println("original message: " + message);
        System.out.println("cipher text: " + cipherMessage);
        System.out.println("ecrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
        System.out.println("restored message: " + restoredMessage);
    }

    public static void RSAsignatureFunc() {

        // большие числа p и q, случайные
        BigInteger p = BigInteger.probablePrime(128, random);
        BigInteger q = BigInteger.probablePrime(128, random);

        // далее при помощи p и q вычислим n
        // n - модуль для закрытого и открытого ключей, n-ное колво бит это длина ключа
        BigInteger n = n(p, q);

        // вычисление функции эйлера
        // она, как известно, равна phi(n) = (p-1)(q-1)
        BigInteger phi = getPhi(p, q);

        // далее найти такой е, чтобы 1 < e < phi(n)    и чтобы gcd(e,phi) = 1, т.е совместно простые e и phi
        BigInteger e = genE(phi);

        // далее вычислить d, для которого d ≡ e^(-1) (mod Phi(n))
        BigInteger d = extEuclid(e, phi)[1];

        // выписываем значения в консоль
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + n);
        System.out.println("Phi: " + phi);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

        String message = "sample text with particular size";
        BigInteger cipherMessage = stringCipher(message);

        BigInteger[] signature = new BigInteger[2];
        signature = RSAsignature(cipherMessage, d, n);

        System.out.println("signature: ");
        for (int i = 0; i < signature.length; ++i) {
            System.out.println(signature[i]);
        }

        BigInteger[] openKey = new BigInteger[2];
        openKey[0] = cipherMessage;
        openKey[1] = signature[1];

        BigInteger prototype = cipherMessage;
        prototype = signature[1].modPow(e, n);

        System.out.println(prototype);
        System.out.println(cipherMessage);

        boolean result = RSAsignatureVerification(cipherMessage, prototype);

        String str = (result == true) ? "the RSA signature has been verified" : "the RSA signature has not been verified";
        System.out.println(str);
        Scanner scan = new Scanner(in);
        while (!scan.hasNextInt()) {
            scan.next();
        }
        int s = scan.nextInt();
        if (s == 1) RSAsignatureFunc();

    }

    public static void blindSignatureFunc() {
        BigInteger e = largePrime(64); //
        BigInteger d = largePrime(64); //
        BigInteger n = largePrime(64); //
        BigInteger factor = largePrime(64); //
        String message = "some text with particular length"; // M - изначальное сообщение
        BigInteger m = stringCipher(message); // M - изначальное сообщение в формате BigInteger

        BigInteger blindMsg = blindHideMsg(m, factor, e, n);
        BigInteger blindSig = blindSignature(blindMsg, d, n);
        BigInteger sig = blindRetrieveSig(blindSig, factor, n);
        BigInteger realSig = m.modPow(d, n);
        System.out.println("initial message" + message);
        System.out.println("ciphered message: " + m);
        System.out.println("blind hide message: " + blindMsg);
        System.out.println("blind sign: " + blindSig);
        System.out.println("blind retrieve sign: " + sig);
        System.out.println("sign: " + realSig);
    }
}
