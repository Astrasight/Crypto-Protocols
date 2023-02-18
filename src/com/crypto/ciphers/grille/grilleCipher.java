package com.crypto.ciphers.grille;

import static com.crypto.ciphers.grille.grilleAlgo.*;
import static com.crypto.utils.mathUtils.*;
import java.util.ArrayList;
import java.util.List;

public class grilleCipher {

    public static String SLOT = "*";
    public static String PAD = "X";
    public static String UNMARKED = "-";

    private String plaintext;
    private int plaintextLength;
    private int[][] keyPositions;

    public grilleCipher(String plaintext) {
        this.plaintext = plaintext.toUpperCase();
        System.out.println("Initial plaintext: " + getPlaintext() + "\nL = " + getPlaintext().length());
        this.plaintextLength = plaintext.length();
        this.fixPlaintext();
        System.out.println("Fixed  plainttext: " + getPlaintext() + "\nL = " + getPlaintext().length());
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext() {
        this.plaintext = plaintext;
    }

    public int getPlaintextLength() {
        return plaintextLength;
    }

    public int[][] getKeyPositions() {
        return keyPositions;
    }

    public void setKeyPositions(int[][] keyPositions) {
        this.keyPositions = keyPositions;
    }

    public void fixPlaintext() {
        if (!isSquare(getPlaintextLength())) {
            StringBuilder fixedPlaintext = new StringBuilder(plaintext);
            System.out.println("next sqr " + nextSquare(plaintextLength));
            for (int i = getPlaintextLength() - 1; i < nextSquare(getPlaintextLength() - 1); ++i) {
                fixedPlaintext.append(PAD);
            }
            this.plaintext = fixedPlaintext.toString();
            this.plaintextLength = fixedPlaintext.toString().length();
        }
    }

    public grilleAlgo generateRandomKey() {
        int n = (int) Math.sqrt(getPlaintextLength());
        grilleAlgo key = new grilleAlgo(n);

        key.initialize();

        grilleAlgo section1 = key.getSubgrille(SECTION1);
        section1.initialize();
        grilleAlgo section2 = section1.rightRotation();
        grilleAlgo section3 = section2.rightRotation();
        grilleAlgo section4 = section3.rightRotation();

        key.setSubgrille(section1, SECTION1);
        key.setSubgrille(section2, SECTION2);
        key.setSubgrille(section3, SECTION3);
        key.setSubgrille(section4, SECTION4);

        int randomI, randomJ;
        List<String> marked = new ArrayList<>();
        int N = key.getN();
        keyPositions = new int[N * N / 4][2];
        int k = 0;
        while (k < N * N / 4) {
            randomI = (int) (Math.random() * N);
            randomJ = (int) (Math.random() * N);
            if (key.getLetter(randomI, randomJ).equals(SLOT)) {
                randomI = (int) (Math.random() * N);
                randomJ = (int) (Math.random() * N);
            } else if (!marked.contains(key.getLetter(randomI, randomJ))) {
                marked.add(key.getLetter(randomI, randomJ));
                key.setLetter(randomI, randomJ, SLOT);
                keyPositions[k][0] = randomI;
                keyPositions[k][1] = randomJ;
                k++;
            }
        }
        return key;
    }

    public String encrypt(grilleAlgo key) {
        //Grille randomKey = generateRandomKey();
        int N = key.getN();
        String[] plaintextGroups = separatePlaintext(getPlaintext());
        StringBuilder ciphertext = new StringBuilder();
        grilleAlgo cipherMatrix = new grilleAlgo(N);
        cipherMatrix.initializeDefault();

        System.out.println("\nKey:");
        key.display();

        int count = 0;
        for (int g = 0; g < 4; ++g) {
            System.out.println("\nEmpty cipher matrix:");
            cipherMatrix.display();
            System.out.println("\nGroup #" + (g+1));
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    if (key.getLetter(i, j).equals(SLOT)) {
                        cipherMatrix.setLetter(i, j, plaintextGroups[g].charAt(count)+"");
                        count++;
                    }
                }
            }
            cipherMatrix.display();
            cipherMatrix.rotateLeft();
            count = 0;
        }
        System.out.println("\nFinal cipher matrix:");
        cipherMatrix.display();

        // Build the ciphertext
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                ciphertext.append(cipherMatrix.getLetter(i, j));
            }
        }
        return ciphertext.toString();
    }

    public String decrypt(grilleAlgo key) {
        return "";
    }

    /**
     * Displays the slot positions of the key in a listed format
     */
    public void displayKeyPositions() {
        System.out.println("-- Grille Key Positions --");
        for (int i = 0; i < keyPositions.length; ++i) {
            System.out.print("(" + keyPositions[i][0] + ", " + keyPositions[i][1] + "), ");
        }
    }

    /**
     * Separates the plaintext in four groups of length N * N / 4
     * @param plaintext the plaintext to separate
     * @return an array containing four strings
     */
    public String[] separatePlaintext(String plaintext) {
        int N = getPlaintextLength();
        int N0 = N / 4, N1 = N / 2, N2 = 3 * N / 4;
        String[] groups = new String[4];
        groups[0] = plaintext.substring(0, N0);
        groups[1] = plaintext.substring(N0, N1);
        groups[2] = plaintext.substring(N1, N2);
        groups[3] = plaintext.substring(N2, N);
        return groups;
    }

}
