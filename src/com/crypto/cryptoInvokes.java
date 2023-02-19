package com.crypto;

import com.crypto.ciphers.XORcipher;
import com.crypto.ciphers.vigenereCipher;
import com.crypto.ciphers.grille.grilleAlgo;
import com.crypto.ciphers.grille.grilleCipher;

import static com.crypto.utils.languageUtils.*;
import static com.crypto.utils.cipherUtils.*;
import static com.crypto.utils.mathUtils.*;
import static com.crypto.utils.pollardMethod.*;
import static com.crypto.utils.arithmeticOperations.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class cryptoInvokes {
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz ";

    public static void caesarCipher() {
        System.out.println("Caesar Cipher\n");
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
        denominator();
    }

    public static void atbashCipher() {
        System.out.println("Atbash Cipher\n");
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
        denominator();
    }

    public static void routeCipher() {
        System.out.println("Route Cipher\n");
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
        denominator();
        System.exit(0);
    }

    public static void vigenereCipher() {
        System.out.println("Vigenere Cipher\n");
        String key = "en";
        vigenereCipher v = new vigenereCipher(97, 26);
        String enc = v.encrypt("english", key);
        System.out.println(enc);
        String dec = v.decrypt(enc, key);
        System.out.println(dec);
        denominator();
    }

    public static void grilleCipher() {
        System.out.println("Grille Cipher\n");
        String plainText = "jimattacksatdawnnowinthemorning";
        grilleCipher cipher = new grilleCipher(plainText);
        grilleAlgo key = cipher.generateRandomKey();
        String cipherText = cipher.encrypt(key);
        System.out.println("ciphertext: " + cipherText);
        denominator();
    }

    public static void XORcipher() {
        System.out.println("XOR Cipher\n");
        String message = "some text to be encrypted";
        String key = "thisisakey";

        XORcipher cipher = new XORcipher(message, key);

        int[] encrypted = cipher.encrypt(message, key);
        for(int i = 0; i < encrypted.length; i++)
            System.out.printf("%d,", encrypted[i]);
        System.out.println("");
        System.out.println(cipher.decrypt(encrypted, key));
        denominator();
    }

    public static void euclidCipher() {
        System.out.println("Default GCD\n");
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
        denominator();
    }

    public static void binaryEuclidCipher() {
        System.out.println("Binary GCD\n");
        int a = 189206;
        int b = 246;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        System.out.println(gcd(a, b));
        denominator();
    }

    public static void extendedEuclid() {
        System.out.println("Extended GCD\n");
        int a = 30;
        int b = 50;
        System.out.println("a = " + a + " and b = " + b);

        AtomicInteger x = new AtomicInteger(), y = new AtomicInteger();

        System.out.println("The GCD is " + extended_gcd(a, b, x, y));
        System.out.printf("x = %d, y = %d\n", x.get(), y.get());
        denominator();
    }

    public static void extendedBinaryEuclid() {
        System.out.println("Extended Binary GCD\n");
        int a = 36;
        int b = 54;
        int[] res = extendedBinary_gcd(a, b);
        System.out.printf("(%d)*%d + (%d)*%d = %d\n", res[0], a, res[1], b, res[2]);
        denominator();
    }

    public static void fermatPrimality() {
        System.out.println("Fermat Primality Test\n");
        int k = 3;
        if (isPrime(11,k)) System.out.println(" true");
        else System.out.println(" false");
        if (isPrime(15, k)) System.out.println(" true");
        else System.out.println(" false");
        denominator();
    }

    public static void jacobiSymbol() {
        System.out.println("Jacobi Symbol\n");
        int a = 3;
        int n = 7;
        int result = calculateJacobi(a, n);
        System.out.printf("Jacobi symbol of (%d/%d) = %d\n", a, n, result);
        denominator();
    }

    public static void solovayStrassenTest() {
        System.out.println("Solovay Strassen Test\n");
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
        denominator();
    }

    public static void millerRabinTest() {
        System.out.println("Miller Rabin Test\n");
        int k = 8;
        int number = 1725;
        System.out.println("All primes smaller "
                + "than 100: ");

        for (int n = 1; n < 100; n++)
            if (isPrimeMiller(n, k))
                System.out.print(n + " ");
        denominator();
    }

    public static void rhoAlgorithmFact() {
        System.out.println("Rho Algorithm Factorial\n");
        int number = 1824;
        System.out.println("the number is " + number);

        System.out.println("\nFactors are : ");
        factorRho(number);
        denominator();
    }

    public static void rhoAlgorithmLog() {
        System.out.println("Rho Algorithm Logarithmic\n");
        int n = 1258125;
        System.out.println("the number is " + n);
        System.out.print("Divisor of  " + n + " is " +
                rhoLog(n));
        denominator();
    }

    public static void arithmetic() {
        System.out.println("Byte Arithmetic Operations\n");
        int result;
        Scanner scan = new Scanner(System.in);
        System.out.println("enter any number1 > 0\n");
        int n1 = scan.nextInt();
        System.out.println("enter any number2 > 0\n");
        int n2 = scan.nextInt();

        System.out.println("add operation\n");
        result = add(n1,n2);
        System.out.println(result + "\n");
        System.out.println("substract operation\n");
        result = substract(n1,n2);
        System.out.println(result + "\n");
        System.out.println("multiply operation\n");
        result = multiply(n1,n2);
        System.out.println(result + "\n");
        System.out.println("divide\n");
        result = divide(n1,n2);
        System.out.println(result + "\n");
        System.out.println("karatsuba algorithm\n");
        result = multiplyKaratsuba(n1,n2);
        System.out.println(result + "\n");
        denominator();
    }
}
