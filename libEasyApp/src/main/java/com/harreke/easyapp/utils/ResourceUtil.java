package com.harreke.easyapp.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/30
 */
public class ResourceUtil {
    private static int[] colorAttrs = new int[]{R.attr.colorPrimary, R.attr.colorPrimaryDark, R.attr.colorAccent};

    public static String getString(Context context, int stringId) {
        return context.getString(stringId);
    }

    public static String getString(Context context, int stringId, Object... params) {
        return context.getString(stringId, params);
    }

    public static int getColor(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static float getDimension(Context context, int dimenId) {
        return context.getResources().getDimension(dimenId);
    }

    /**
     * 获得主题色
     *
     * @param context
     *         Context
     *
     * @return 主题色数组
     *
     * 一共3个，按顺序为：
     * colorPrimary
     * colorPrimaryDark
     * colorAccent
     */
    public static int[] obtainThemeColor(Context context) {
        TypedArray style = context.obtainStyledAttributes(colorAttrs);
        int[] colors = new int[3];

        colors[0] = style.getColor(0, 0);
        colors[1] = style.getColor(1, 0);
        colors[2] = style.getColor(2, 0);

        style.recycle();

        return colors;
    }
}
