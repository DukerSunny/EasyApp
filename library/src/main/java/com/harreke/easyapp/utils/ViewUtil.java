package com.harreke.easyapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.harreke.easyapp.widgets.transitions.ViewInfo;

import java.io.ByteArrayOutputStream;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/31
 */
public class ViewUtil {
    public static byte[] bitmap2Bytes(@NonNull Bitmap bitmap) {
        return bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static byte[] bitmap2Bytes(@NonNull Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        bitmap.compress(compressFormat, quality, outputStream);

        return outputStream.toByteArray();
    }

    public static Bitmap bytes2Bitmap(@NonNull byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void check(@NonNull RadioGroup group, int index) {
        if (index >= 0 && index < group.getChildCount()) {
            group.check(group.getChildAt(index).getId());
        }
    }

    public static int findChild(@NonNull ViewGroup parent, int childId) {
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

    public static int getBackgroundColor(@NonNull View view) {
        Drawable drawable;

        if (Build.VERSION.SDK_INT >= 11) {
            drawable = view.getBackground();
            if (drawable != null && drawable instanceof ColorDrawable) {
                return ((ColorDrawable) drawable).getColor();
            }
        }

        return Color.TRANSPARENT;
    }

    public static Bitmap getBitmap(@NonNull ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        return drawable != null ? drawable.getBitmap() : null;
    }

    public static byte[] getBitmapBytes(@NonNull ImageView imageView) {
        return bitmap2Bytes(getBitmap(imageView));
    }

    public static Bitmap getDrawingCache(@NonNull View view) {
        Bitmap bitmap;

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        bitmap = view.getDrawingCache();
        //        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    public static byte[] getDrawingCacheBytes(@NonNull View view) {
        byte[] bytes = bitmap2Bytes(getDrawingCache(view), Bitmap.CompressFormat.JPEG, 25);

        view.setDrawingCacheEnabled(false);

        return bytes;
    }

    public static float getFloatTag(@NonNull View view) {
        Object tag = view.getTag();

        return tag instanceof Float ? (Float) tag : 0;
    }

    public static float getFloatTag(@NonNull View view, int key) {
        Object tag = view.getTag(key);

        return tag instanceof Float ? (Float) tag : 0;
    }

    public static int getIntTag(@NonNull View view) {
        Object tag = view.getTag();

        return tag instanceof Integer ? (Integer) tag : 0;
    }

    public static int getIntTag(@NonNull View view, int key) {
        Object tag = view.getTag(key);

        return tag instanceof Integer ? (Integer) tag : 0;
    }

    public static long getLongTag(@NonNull View view) {
        Object tag = view.getTag();

        return tag instanceof Long ? (Long) tag : 0;
    }

    public static long getLongTag(@NonNull View view, int key) {
        Object tag = view.getTag(key);

        return tag instanceof Long ? (Long) tag : 0;
    }

    public static Object getObjectTag(@NonNull View view) {
        return view.getTag();
    }

    public static Object getObjectTag(@NonNull View view, int key) {
        return view.getTag(key);
    }

    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getStatusBarHeight(@NonNull View view) {
        Rect frame = new Rect();
        DisplayMetrics metrics;
        int width;
        int height;

        view.getWindowVisibleDisplayFrame(frame);
        if (frame.top > 0) {
            return frame.top;
        } else {
            metrics = view.getContext().getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            if (frame.bottom > height) {
                return width - frame.bottom;
            } else {
                return height - frame.bottom;
            }
        }
    }

    public static String getStringTag(@NonNull View view) {
        Object tag = view.getTag();

        return tag instanceof String ? (String) tag : null;
    }

    public static String getStringTag(@NonNull View view, int key) {
        Object tag = view.getTag(key);

        return tag instanceof String ? (String) tag : null;
    }

    public static String getText(@NonNull TextView textView) {
        CharSequence charSequence = textView.getText();

        return charSequence != null ? charSequence.toString() : null;
    }

    public static ViewInfo getViewInfo(@NonNull View view) {
        return getViewInfo(view, false);
    }

    public static ViewInfo getViewInfo(@NonNull View view, boolean withStatusBarHeight) {
        ViewInfo viewInfo = new ViewInfo();
        int[] position = new int[2];

        view.getLocationInWindow(position);
        viewInfo.x = position[0];
        viewInfo.y = position[1];
        if (Build.VERSION.SDK_INT <= 19 || !withStatusBarHeight) {
            viewInfo.y -= getStatusBarHeight(view);
        }
        viewInfo.width = view.getMeasuredWidth();
        viewInfo.height = view.getMeasuredHeight();

        return viewInfo;
    }

    public static void hideInputMethod(@NonNull View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isInputMethodShowing(@NonNull View view) {
        return ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).isActive(view);
    }

    public static boolean isStatusBarTop(Activity activity) {
        Rect frame = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        LogUtil.e(null, "window frame=" + JsonUtil.toString(frame));

        return frame.top > 0;
    }

    public static void patchTopPadding(View view) {
        int left;
        int top;
        int right;
        int bottom;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            left = view.getPaddingLeft();
            top = view.getPaddingTop();
            right = view.getPaddingRight();
            bottom = view.getPaddingBottom();
            view.setPadding(left, top + getStatusBarHeight(view), right, bottom);
        }
    }

    public static void showInputMethod(@NonNull View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }
}