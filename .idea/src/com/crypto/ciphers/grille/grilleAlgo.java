package com.crypto.ciphers.grille;

import static com.crypto.utils.mathUtils.*;
import static com.crypto.ciphers.grille.grilleCipher.*;

public class grilleAlgo {

    public static String SLOT = "*";
    public static String PAD = "X";
    public static String UNMARKED = "-";

    public static final int SECTION1 = 1;
    public static final int SECTION2 = 2;
    public static final int SECTION3 = 3;
    public static final int SECTION4 = 4;

    private int n;
    private String[][] letters;

    public grilleAlgo(int n) {
        this.n = n;
        if (isEven(n)) letters = new String[n][n];
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String[][] getLetters() {
        return letters;
    }

    public void setLetters(String[][] letters) {
        this.letters = letters;
    }

    public String getLetter(int i, int j) {
        return letters[i][j];
    }

    public void setLetter(int i, int j, String letter) {
        letters[i][j] = letter;
    }

    public grilleAlgo rightRotation() {
        grilleAlgo rightGrille = new grilleAlgo(n);
        String[][] rightRotatedMatrix = new String[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                rightRotatedMatrix[i][j] = letters[n - j - 1][i];
            }
        }
        rightGrille.setLetters(rightRotatedMatrix);
        return rightGrille;
    }

    public grilleAlgo leftRotation() {
        grilleAlgo leftGrille = new grilleAlgo(n);
        String[][] leftRotatedMatrix = new String[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                leftRotatedMatrix[i][j] = letters[j][n - i - 1];
            }
        }
        leftGrille.setLetters(leftRotatedMatrix);
        return leftGrille;
    }


    public void rotateLeft() {
        String[][] leftRotatedMatrix = new String[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; j++) {
                leftRotatedMatrix[i][j] = letters[j][n - i - 1];
            }
        }
        letters = leftRotatedMatrix;
    }

    public void rotateRight() {
        String[][] rightRotatedMatrix = new String[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; j++) {
                rightRotatedMatrix[i][j] = letters[n - j - 1][i];
            }
        }
        letters = rightRotatedMatrix;
    }

    public void display() {
        System.out.println();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; j++) {
                System.out.print(letters[i][j] + " ");
                if (j == n - 1)
                    System.out.print("\n");
            }
        }
    }

    public void randomFill() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; j++) {
                letters[i][j] = "" + (char) ((int) (Math.random() * 26) + 65);
            }
        }
    }

    public grilleAlgo getSubgrille(int section) {
        grilleAlgo subgrille = new grilleAlgo(n / 2);
        String[][] subelements = new String[n / 2][n / 2];
        switch (section) {
            case SECTION1:
                for (int i = 0; i < n / 2; ++i)
                    for (int j = 0; j < n / 2; ++j)
                        subelements[i][j] = letters[i][j];
                break;
            case SECTION2:
                for (int i = 0; i < n / 2; ++i)
                    for (int j = n / 2, y = 0; j < n; ++j, ++y)
                        subelements[i][y] = letters[i][j];
                break;
            case SECTION3:
                for (int i = n / 2, x = 0; i < n; ++i, ++x)
                    for (int j = 0; j < n / 2; ++j)
                        subelements[x][j] = letters[i][j];
                break;

            case SECTION4:
                for (int i = n / 2, x = 0; i < n; ++i, ++x)
                    for (int j = n / 2, y = 0; j < n; ++j, ++y)
                        subelements[x][y] = letters[i][j];
                break;
            default:
                System.out.println("[Error] Invalid section number");
                break;
        }
        subgrille.setLetters(subelements);
        return subgrille;
    }

    public void setSubgrille(grilleAlgo subgrille, int section) {
        switch (section) {
            case SECTION1:
                for (int i = 0; i < n / 2; ++i)
                    for (int j = 0; j < n / 2; ++j)
                        letters[i][j] = subgrille.getLetters()[i][j];
                break;
            case SECTION2:
                for (int i = 0; i < n / 2; ++i)
                    for (int j = n / 2, y = 0; j < n; ++j, ++y)
                        letters[i][j] = subgrille.getLetters()[i][y];
                break;
            case SECTION3:
                for (int i = n / 2, x = 0; i < n; ++i, ++x)
                    for (int j = n / 2, y = 0; j < n; ++j, ++y)
                        letters[i][j] = subgrille.getLetters()[x][y];
                break;
            case SECTION4:
                for (int i = n / 2, x = 0; i < n; ++i, ++x)
                    for (int j = 0; j < n / 2; ++j)
                        letters[i][j] = subgrille.getLetters()[x][j];
                break;
            default:
                System.out.println("[Error] Invalid section number");
                break;
        }
    }

    public void initialize() {
        int count = 1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                letters[i][j] = count + "";
                count++;
            }
        }
    }

    public void initializeWithKey(grilleAlgo key) {
        int N = key.getN();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (key.getLetter(i, j).equals(SLOT)) {
                    this.setLetter(i, j, SLOT);
                } else {
                    this.setLetter(i, j, UNMARKED);
                }
            }
        }
    }

    public void initializeDefault() {
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getN(); j++) {
                this.setLetter(i, j, UNMARKED);
            }
        }
    }

    public static grilleAlgo merge(grilleAlgo first, grilleAlgo second) {
        if (first.getN() == second.getN()) {
            int N = first.getN();
            grilleAlgo merged = new grilleAlgo(N);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (first.getLetter(i, j).equals(UNMARKED) && !second.getLetter(i, j).equals(UNMARKED))
                        merged.setLetter(i, j, second.getLetter(i, j));
                    else if (!first.getLetter(i, j).equals(UNMARKED) && second.getLetter(i, j).equals(UNMARKED))
                        merged.setLetter(i, j, first.getLetter(i, j));
                    else if (first.getLetter(i, j).equals(UNMARKED) && second.getLetter(i, j).equals(UNMARKED))
                        merged.setLetter(i, j, UNMARKED);
                    else if (!first.getLetter(i, j).equals(UNMARKED) && !second.getLetter(i, j).equals(UNMARKED))
                        merged.setLetter(i, j, Math.random() > 0.5 ? first.getLetter(i, j) : second.getLetter(i, j));//merged.set(i, j, first.get(i, j));
                }
            }
            return merged;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getN(); j++) {
                str.append(letters[i][j]);
            }
        }
        return str.toString();
    }

}
