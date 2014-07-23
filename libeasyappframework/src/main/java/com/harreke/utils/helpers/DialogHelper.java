package com.harreke.utils.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;

import com.harreke.utils.listeners.OnDialogButtonClickListener;

public class DialogHelper implements DialogInterface.OnClickListener {
    private AlertDialog mDialog = null;
    private OnDialogButtonClickListener mDialogListener = null;

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, null, null);
    }

    private DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, int contentId, String[] items, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (titleId > 0) {
            builder.setTitle(titleId);
        }
        if (positiveId > 0) {
            builder.setPositiveButton(positiveId, this);
        }
        if (negativeId > 0) {
            builder.setNegativeButton(negativeId, this);
        }
        if (neutralId > 0) {
            builder.setNeutralButton(neutralId, this);
        }
        if (contentId > 0) {
            builder.setMessage(contentId);
        }
        if (items != null) {
            builder.setItems(items, this);
        }
        if (view != null) {
            builder.setView(view);
        }

        mDialog = builder.create();
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, int contentId) {
        this(context, titleId, positiveId, negativeId, neutralId, contentId, null, null);
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, String[] items) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, items, null);
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, View view) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, null, view);
    }

    public final void hide() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public final boolean isShowing() {
        return mDialog.isShowing();
    }

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (mDialogListener != null) {
            mDialogListener.onDialogButtonClick(which);
        }
    }

    public final void setOnDialogListener(OnDialogButtonClickListener dialogListener) {
        this.mDialogListener = dialogListener;
    }

    public final void setOnDismissListener(OnDismissListener dismissListener) {
        if (mDialog != null) {
            mDialog.setOnDismissListener(dismissListener);
        }
    }

    public final void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }
}