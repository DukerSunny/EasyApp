package com.harreke.easyapp.helpers;

import android.app.AlertDialog;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 对话框助手
 */
public class DialogHelper {
    private final static String TAG = "DialogHelper";

//    private static void setShowing(AlertDialog dialog, boolean showing) {
//        Field field;
//
//        try {
//            field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//            field.setAccessible(true);
//            field.set(dialog, showing);
//        } catch (Exception e) {
//            Log.e(TAG, "Hack dialog failed");
//        }
//    }
//
//    public static void showAndLock(AlertDialog dialog) {
//        dialog.show();
//        setShowing(dialog, false);
//    }
//
//    public static void unlockAndHide(AlertDialog dialog) {
//        setShowing(dialog, true);
//        dialog.dismiss();
//    }
}