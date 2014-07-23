package com.harreke.utils.tools;

public class StringUtil {
    public static String getCk(String ck) {
        String tmp = ck + "Walle doesn't have penis";
        return String.valueOf(getHash(tmp));
    }

    public static int getHash(String str) {
        int len = str.length();
        int hash = 0;
        for (int i = 0; i < len; i++) {
            hash = (hash << 5) + hash + str.charAt(i);
        }
        return hash;
    }

    public static String getNumberAbbreviation(int number) {
        if (number <= 9999) {
            return String.valueOf(number);
        } else {
            return Math.floor(number / 1000f) / 10 + "万";
        }
    }
}