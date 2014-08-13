package com.harreke.easyapp.tools;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 字符串工具
 */
public class StringUtil {
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