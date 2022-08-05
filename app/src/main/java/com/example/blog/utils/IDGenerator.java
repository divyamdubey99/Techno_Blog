package com.example.blog.utils;

import java.security.SecureRandom;

public class IDGenerator {

    public static String generateAlphaNumericString() {

        String regexDigit = "\\d+";
        String regexAlphabets = "[a-zA-Z]+";
        String randomAlphaNumString;

        do {
            randomAlphaNumString = randomString(20, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

        } while ((randomAlphaNumString.matches(regexDigit) || randomAlphaNumString.matches(regexAlphabets)));
        return randomAlphaNumString;
    }

    private static String randomString(int len, String mPassword) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(mPassword.charAt(rnd.nextInt(mPassword.length())));
        }
        return sb.toString();
    }
}
