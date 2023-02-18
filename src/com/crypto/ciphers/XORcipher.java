package com.crypto.ciphers;

import java.io.*;
import java.util.*;

public class XORcipher {

    private String key;
    private String message;

    public XORcipher(String message, String key) {
        this.message = message;
        this.key = key;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getMessage() { return message; }

    public void setMessage() { this.message = message; }

    public int[] encrypt(String message, String key) {
        int[] output = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            int o = (Integer.valueOf(message.charAt(i)) ^ Integer.valueOf(key.charAt(i % (key.length() - 1)))) + '0';
            output[i] = o;
        }
        return output;
    }

    public String decrypt(int[] input, String key) {
        String output = "";
        for (int i = 0; i < input.length; ++i) {
            output += (char) ((input[i] - 48) ^ (int) key.charAt(i % (key.length() - 1)));
        }
        return output;
    }
}

