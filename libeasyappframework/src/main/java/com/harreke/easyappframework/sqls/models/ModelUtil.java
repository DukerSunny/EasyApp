package com.harreke.easyappframework.sqls.models;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/25
 */
public class ModelUtil {
    public static String joint(String[] strs) {
        String result = "";
        int i;

        if (strs != null) {
            for (i = 0; i < strs.length - 1; i++) {
                result += "'" + strs[i] + "' , ";
            }
            result += "'" + strs[strs.length - 1] + "'";
        }

        return result;
    }

    public static String joint(String[] strs1, String[] strs2) {
        String result = "";
        int i;

        if (strs1 != null && strs2 != null && strs1.length == strs2.length) {
            for (i = 0; i < strs1.length - 1; i++) {
                result += strs1[i] + " = '" + strs2[i] + "' , ";
            }
            result += strs1[strs1.length - 1] + " = '" + strs2[strs1.length - 1] + "' , ";
        }

        return result;
    }
}