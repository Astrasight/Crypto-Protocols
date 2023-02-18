package com.crypto;

import com.crypto.ciphers.XORcipher;
import com.crypto.ciphers.vigenereCipher;
import com.crypto.ciphers.grille.grilleAlgo;
import com.crypto.ciphers.grille.grilleCipher;

import static com.crypto.signatures.RSASignatureClass.*;
import static com.crypto.signatures.elgamalSignatureClass.*;
import static com.crypto.utils.languageUtils.*;
import static com.crypto.utils.cipherUtils.*;
import static com.crypto.utils.RSAutils.*;
import static com.crypto.signatures.blindSignatureClass.*;
import static com.crypto.utils.mathUtils.*;
import static com.crypto.utils.pollardMethod.*;
import static java.lang.System.in;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class cryptoMain {

    private final static BigInteger one = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz ";

    public static void main(String[] args) {
        System.out.println("1 - Diffie-Hellman \n2 - El-Gamal\n3 - El-Gamal Signature\n4 - RSA\n5 - RSA Signature\n6 - Blind Signature RSA\n7 - Caesar Cipher with key k\n8 - Atbash cipher\n9 - Route Cipher\n" +
                "10 - Vigenere Cipher\n11 - Grille Cipher\n12 - XOR cipher\n13 - Euclid Algorithm\n 14 - Binary Euclid\n 15 - Extended Euclid\n 16 - Extended Binary Euclid\n" +
                "17 - Jacobi Symbol\n18 - Solovay-Strassen Test\n19 - Miller-Rabin Test\n20 - Pollard Rho Algorithm");
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
            case 7:
                caesarCipher();
                break;
            case 8:
                atbashCipher();
                break;
            case 9:
                routeCipher();
                break;
            case 10:
                vigenereCipher();
                break;
            case 11:
                grilleCipher();
                break;
            case 12:
                XORcipher();
                break;
            case 13:
                euclidCipher();
                break;
            case 14:
                binaryEuclidCipher();
                break;
            case 15:
                extendedEuclid();
                break;
            case 16:
                extendedBinaryEuclid();
                break;
            case 17:
                jacobiSymbol();
                break;
            case 18:
                solovayStrassenTest();
                break;
            case 19:
                millerRabinTest();
                break;
            case 20:
                rhoAlgorithm();
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

    public static void caesarCipher() {

        String[] alphabet1 = new String[]{"abcdefghijklmnopqrstuvwxyz"};

        String message = "some text with particular length";
        int k = 5;
        System.out.println("key k: " + k);
        int m = alphabet.length();
        ArrayList<String> result = new ArrayList<String>();

        for (int l = 0; l < m; ++l) {
            result.add(shift(message, alphabet, l));
        }

        for (String word : result) {
            System.out.println("The encrypted (key is " + k + ") word is: " + word);
            k += 1;
        }
    }

    public static void atbashCipher() {
        String text = "flip the text and replace each odd character with its number";
        int[] isOdd = {1};
        StringBuilder sb = new StringBuilder();
        for (char tmp : text.toCharArray()) {
            if (isOdd[0] % 2 != 0)
                sb.append(isOdd[0]);
            else sb.append(tmp);
            isOdd[0]++;
        }
        System.out.println(text);
        System.out.println(sb.toString());
        System.out.println(sb.reverse().toString());
    }

    public static void routeCipher() {
        System.out.print("type your key: ");
        Scanner scan = new Scanner(System.in);
        String key = scan.nextLine();
        System.out.print("type your message: ");
        String message = scan.nextLine();
        int msgchar = message.length();
        int l = key.length();

        if (!((msgchar % l) == 0)) {

            do {
                message = message + "x";
                msgchar = message.length();
            } while (!((msgchar % l) == 0));

        }
        String ans = "";

        char temp[][] = new char[key.length()][message.length()];
        char msg[] = message.toCharArray();
        int x = 0;
        int y = 0;
        for (int i = 0; i < msg.length; i++) {
            if (msg[i] == ' ')
                temp[x][y] = '_';
            else
                temp[x][y] = msg[i];
            if (y == (key.length() - 1)) {
                y = 0;
                x = x + 1;
            } else {
                y++;
            }
        }

        char t[] = key.toCharArray();
        Arrays.sort(t);

        for (int j = 0; j < x; j++) {
            for (int i = 0; i < key.length(); i++) {
                System.out.print(temp[j][i]);
            }
            System.out.println();
        }
        for (int i = 0; i < t.length; i++) {
            int pos = findLetterPosition(key, t[i]);
            for (int j = 0; j < x; j++) {
                ans += temp[j][pos];
            }
        }

        System.out.println(ans);
        System.exit(0);
    }

    public static void vigenereCipher() {
        String key = "en";
        vigenereCipher v = new vigenereCipher(97, 26);
        String enc = v.encrypt("english", key);
        System.out.println(enc);
        String dec = v.decrypt(enc, key);
        System.out.println(dec);
    }

    public static void grilleCipher() {
        String plainText = "jimattacksatdawnnowinthemorning";
        grilleCipher cipher = new grilleCipher(plainText);
        grilleAlgo key = cipher.generateRandomKey();
        String cipherText = cipher.encrypt(key);
        System.out.println("ciphertext: " + cipherText);
    }

    public static void XORcipher() {
        String message = "some text to be encrypted";
        String key = "thisisakey";

        XORcipher cipher = new XORcipher(message, key);

        int[] encrypted = cipher.encrypt(message, key);
        for(int i = 0; i < encrypted.length; i++)
            System.out.printf("%d,", encrypted[i]);
        System.out.println("");
        System.out.println(cipher.decrypt(encrypted, key));
    }

    public static void euclidCipher() {
        int a = 103;
        int b = 485;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        System.out.println(a);
    }

    public static void binaryEuclidCipher() {
        long a = 189206;
        long b = 246;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        System.out.println(gcd(a, b));
    }

    public static void extendedEuclid() {
        int a = 30;
        int b = 50;
        System.out.println("a = " + a + " and b = " + b);

        AtomicInteger x = new AtomicInteger(), y = new AtomicInteger();

        System.out.println("The GCD is " + extended_gcd(a, b, x, y));
        System.out.printf("x = %d, y = %d\n", x.get(), y.get());
    }

    public static void extendedBinaryEuclid() {
        int a = 36;
        int b = 54;
        int[] res = extendedBinary_gcd(a, b);
        System.out.printf("(%d)*%d + (%d)*%d = %d\n", res[0], a, res[1], b, res[2]);
    }

    public static void fermatPrimality() {
        int k = 3;
        if (isPrime(11,k)) System.out.println(" true");
        else System.out.println(" false");
        if (isPrime(15, k)) System.out.println(" true");
        else System.out.println(" false");
    }

    public static void jacobiSymbol() {
        int a = 3;
        int n = 7;
        int result = calculateJacobi(a, n);
        System.out.printf("Jacobi symbol of (%d/%d) = %d\n", a, n, result);
    }

    public static void solovayStrassenTest() {
        int iter = 50;
        int n1 = 15;
        int n2 = 13;

        if (solovayStrassen(n1, iter))
            System.out.println(n1 + " is prime");
        else
            System.out.println(n1 + " is composite");

        if (solovayStrassen(n2,iter))
            System.out.println(n2 + " is prime");
        else
            System.out.println(n2 + " is composite");
    }

    public static void millerRabinTest() {
        int k = 8;
        int number = 1725;
        System.out.println("All primes smaller "
                + "than 100: ");

        for (int n = 1; n < 100; n++)
            if (isPrimeMiller(n, k))
                System.out.print(n + " ");
    }

    public static void rhoAlgorithm() {
        int number = 1824;
        System.out.println("Pollard Rho Algorithm\n");
        System.out.println("the number is " + number);

        System.out.println("\nFactors are : ");
        factor(number);
    }
}