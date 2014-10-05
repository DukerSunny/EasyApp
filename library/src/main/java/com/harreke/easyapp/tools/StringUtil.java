package com.harreke.easyapp.tools;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 正则工具
 */
public class StringUtil {
    private static String B = "<b>$2</b>";
    private static String EMOT = "<img src='$2/$3'/>";
    private static String FONT = "$3";
    private static String H1 = "<h1>$2</h1>";
    private static String H2 = "<h2>$2</h2>";
    private static String H3 = "<h3>$2</h3>";
    private static String H4 = "<h4>$2</h4>";
    private static String H5 = "<h5>$2</h5>";
    private static String H6 = "<h6>$2</h6>";
    private static String I = "<i>$2</i>";
    private static String IMG = "<img src='$2'>图像</img>";
    private static String LI = "<li>$2</li>";
    private static String SIZE = "<font size='$2'>$3</font>";
    private static String U = "<u>$2</u>";
    private static String URL = "<a href='$2' target=_blank>$2</a>";

    /**
     * 转换UBB
     */
    public static String UBBDecode(String text) {
        text = replace(text, "url", URL);
        text = replace(text, "img", IMG);
        text = replaceSingle(text, "emot=(.+?),(.+?)", EMOT);
        text = replace(text, "font=(.+?)", "font", FONT);
        text = replace(text, "size=(.+?)px", "size", SIZE);
        text = replace(text, "u", U);
        text = replace(text, "i", I);
        text = replace(text, "li", LI);
        text = replace(text, "b", B);
        text = replace(text, "h1", H1);
        text = replace(text, "h2", H2);
        text = replace(text, "h3", H3);
        text = replace(text, "h4", H4);
        text = replace(text, "h5", H5);
        text = replace(text, "h6", H6);

        return text;
    }

    public static String cleanHtmlSymbols(String input) {
        if (input != null && input.length() > 0) {
            input = input.replace("&amp;", "&");
            input = input.replace("&ldquo;", "“");
            input = input.replace("&rdquo;", "”");
            input = input.replace("&middot;", "•");
            input = input.replace("&quot;", "\"");
            input = input.replace("&gt;", ">");
            input = input.replace("&lt;", "<");
            input = input.replace("&nbsp;", " ");
        }

        return input;
    }

    public static String convertUBB(String input) {
        Matcher matcher;

        input = input.replaceAll("\\[emot=([a-z]+?),([0-9]+?)/\\]", "<img src='$1/$2'/>");

        return input;
    }

    public static Matcher getMatcher(String pattern, CharSequence input) {
        return Pattern.compile(pattern).matcher(input);
    }

    public static String replace(String text, String reg, String replaceStr) {
        Matcher m = Pattern.compile("(\\[" + reg + "\\])(.[^\\[]*)(\\[/" + reg + "\\])", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
                .matcher(text);

        text = m.replaceAll(replaceStr);

        return text;
    }

    public static String replace(String text, String reg, String regEnd, String replaceStr) {
        Matcher m = Pattern.compile("(\\[" + reg + "\\])(.[^\\[]*)(\\[/" + regEnd + "\\])", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
                .matcher(text);
        text = m.replaceAll(replaceStr);

        return text;
    }

    public static String replaceSingle(String text, String reg, String replaceStr) {
        Matcher m = Pattern.compile("(\\[" + reg + "/\\])", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(text);

        text = m.replaceAll(replaceStr);

        return text;
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
}