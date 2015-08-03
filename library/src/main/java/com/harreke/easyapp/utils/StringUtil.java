package com.harreke.easyapp.utils;

import android.text.SpannableStringBuilder;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 正则工具
 */
public class StringUtil {
    public static String escape(String input) {
        if (input != null && input.length() > 0) {
            input = input.replace("&amp;", "&");
            input = input.replace("&ldquo;", "“");
            input = input.replace("&rdquo;", "”");
            input = input.replace("&middot;", "•");
            input = input.replace("&mdash;", "-");
            input = input.replace("&quot;", "\"");
            input = input.replace("&gt;", ">");
            input = input.replace("&lt;", "<");
            input = input.replace("&nbsp;", " ");
            input = input.replace("&deg;","°");
        }

        return input;
    }

    public static Matcher getMatcher(String pattern, CharSequence input) {
        return Pattern.compile(pattern).matcher(input);
    }

    public static String getString(SpannableStringBuilder builder, int start, int end) {
        char[] chars = new char[end - start];
        builder.getChars(start, end, chars, 0);

        return new String(chars);
    }

    public static String getString(StringBuilder builder, int start, int end) {
        char[] chars = new char[end - start];
        builder.getChars(start, end, chars, 0);

        return new String(chars);
    }

    public static String indentNumber(int number) {
        String result;

        if (number < 10000) {
            return String.valueOf(number);
        } else {
            result = String.valueOf(number / 10000f);

            return result.substring(0, result.indexOf(".") + 2) + "万";
        }
    }

    public static boolean isValidMail(String input) {
        return StringUtil.getMatcher("\\w+@(\\w+.)+[a-z]{2,3}", input).matches();
    }

    public static int toInt(String input) {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String toMD5(String target) {
        String result;
        StringBuilder stringBuilder;
        MessageDigest md5;
        byte[] digest;

        if (target != null && target.length() > 0) {
            try {
                stringBuilder = new StringBuilder();
                md5 = MessageDigest.getInstance("MD5");
                md5.update(target.getBytes());
                digest = md5.digest();
                for (byte b : digest) {
                    stringBuilder.append(String.format("%02x", b));
                }

                result = stringBuilder.toString();
            } catch (Exception e) {
                result = "";
            }
        } else {
            result = "";
        }

        return result;
    }

    /**
     * 描述：判断两个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str1, String str2){
        if(null == str1 || null == str2 || str1.trim().length() == 0 || str2.trim().length() == 0){
            return true;
        }
        return false;
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || "null".equals(str);
    }
}