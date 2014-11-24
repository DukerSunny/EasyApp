package com.harreke.easyapp.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 对话框助手
 */
public class DialogHelper {
    private final static String TAG = "DialogHelper";
    private DialogInterface.OnClickListener mClickListener = null;
    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mClickListener != null) {
                forceShow();
                mClickListener.onClick(dialog, which);
            }
        }
    };
    private AlertDialog mDialog = null;

    public DialogHelper(Context context) {
        mDialog = new AlertDialog.Builder(context).create();
    }

    private void forceClose() {
        Field field;

        try {
            field = mDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(mDialog, true);
        } catch (Exception e) {
            Log.e(TAG, "Hack dialog failed");
        }
    }

    private void forceShow() {
        Field field;

        try {
            field = mDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(mDialog, false);
        } catch (Exception e) {
            Log.e(TAG, "Hack dialog failed");
        }
    }

    public void hide() {
        forceClose();
        mDialog.dismiss();
    }

    public final boolean isShowing() {
        return mDialog.isShowing();
    }

    public void setNegativeButton(int textId) {
        setNegativeButton(mDialog.getContext().getString(textId));
    }

    public void setNegativeButton(String text) {
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, text, mDialogListener);
    }

    public void setNeutralButton(int textId) {
        setNeutralButton(mDialog.getContext().getString(textId));
    }

    public void setNeutralButton(String text) {
        mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, text, mDialogListener);
    }

    public void setOnClickListener(DialogInterface.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setPositiveButton(int textId) {
        setPositiveButton(mDialog.getContext().getString(textId));
    }

    public void setPositiveButton(String text) {
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, text, mDialogListener);
    }

    public void setTitle(String text) {
        mDialog.setTitle(text);
    }

    public void setTitle(int textId) {
        mDialog.setTitle(textId);
    }

    public void setView(View view) {
        mDialog.setView(view);
    }

    public void setView(int layoutId) {
        mDialog.setView(View.inflate(mDialog.getContext(), layoutId, null));
    }

    public void show() {
        mDialog.show();
    }
}