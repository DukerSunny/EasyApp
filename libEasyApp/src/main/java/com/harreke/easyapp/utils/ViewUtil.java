package com.harreke.easyapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/31
 */
public class ViewUtil {
    public static int findChild(ViewGroup parent, int childId) {
        int position = -1;
        int i;

        for (i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getId() == childId) {
                position = i;
                break;
            }
        }

        return position;
    }

    public static byte[] getBitmapBytes(View view) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap;

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        view.setDrawingCacheEnabled(false);

        return outputStream.toByteArray();
    }

    public static float getFloatTag(View view) {
        return (view != null && view.getTag() != null && view.getTag() instanceof Float) ? (Float) view.getTag() : 0f;
    }

    public static float getFloatTag(View view, int key) {
        return (view != null && view.getTag(key) != null && view.getTag(key) instanceof Float) ? (Float) view.getTag(key) : 0f;
    }

    public static int getIntTag(View view) {
        return (view != null && view.getTag() != null && view.getTag() instanceof Integer) ? (Integer) view.getTag() : 0;
    }

    public static int getIntTag(View view, int key) {
        return (view != null && view.getTag(key) != null && view.getTag(key) instanceof Integer) ? (Integer) view.getTag(key) :
                0;
    }

    public static float getLongTag(View view) {
        return (view != null && view.getTag() != null && view.getTag() instanceof Long) ? (Long) view.getTag() : 0l;
    }

    public static float getLongTag(View view, int key) {
        return (view != null && view.getTag(key) != null && view.getTag(key) instanceof Long) ? (Long) view.getTag(key) : 0l;
    }

    public static Object getObjectTag(View view) {
        return (view != null && view.getTag() != null) ? view.getTag() : null;
    }

    public static Object getObjectTag(View view, int key) {
        return (view != null && view.getTag(key) != null) ? view.getTag(key) : null;
    }

    public static RectF getRect(View view) {
        int[] position = new int[2];
        RectF rect = new RectF();

        view.getLocationOnScreen(position);
        rect.left = position[0];
        rect.top = position[1];
        rect.right = rect.left + view.getMeasuredWidth();
        rect.bottom = rect.top + view.getMeasuredHeight();
        Log.e(null, "view x=" + rect.left + " y=" + rect.top + " width=" + rect.width() + " height=" + rect.height());

        return rect;
    }

    public static String getStringTag(View view) {
        return (view != null && view.getTag() != null && view.getTag() instanceof String) ? (String) view.getTag() : null;
    }

    public static String getStringTag(View view, int key) {
        return (view != null && view.getTag(key) != null && view.getTag(key) instanceof String) ? (String) view.getTag(key) :
                null;
    }

    public static void hideInputMethod(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showInputMethod(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }
}
