package com.harreke.utils.tools;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {
    public static String cleanHtmlTags(String input) {
        Matcher imgMatcher;
        Matcher srcMatcher;
        String img;
        String src;

        input = cleanHtmlSymbols(input);
        input = input.replaceAll(" style=\"[\\S\\s]+?\"", "");
        input = input.replaceAll("<a [\\S\\s]+?>", "").replace("</a>", "");
        input = input.replaceAll("<span [\\S\\s]+?>", "").replace("</span>", "");
        input = input.replace("<hr/>", "");
        input = input.replace("<strong>", "").replace("</strong>", "");
        input = input.replace("<p>", "[break]").replace("</p>", "[break]");
        input = input.replace("<br/>", "[break]");
        imgMatcher = Regular.getMatcher("<img [\\S\\s]+?/>", input);
        while (imgMatcher.find()) {
            img = imgMatcher.group();
            srcMatcher = Regular.getMatcher("src=\"([\\S\\s]+?)\"", img);
            if (srcMatcher.find()) {
                src = "[img]" + srcMatcher.group(1);
                input = input.replace(img, src);
            }
        }

        return input;
    }

    public static String cleanHtmlSymbols(String input) {
        if (input != null && input.length() > 0) {
            input = input.replace("&amp;", "&");
            input = input.replace("&quot;", "\"");
            input = input.replace("&gt;", ">");
            input = input.replace("&lt;", "<");
            input = input.replace("&nbsp;", " ");
        }

        return input;
    }

    public static Matcher getMatcher(String pattern, CharSequence input) {
        return Pattern.compile(pattern).matcher(input);
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