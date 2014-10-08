package com.harreke.easyapp.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 对话框助手
 */
public class DialogHelper {
    private AlertDialog mDialog = null;

    /**
     * 创建对话框助手
     *
     * @param context
     *         创建的Context
     * @param titleId
     *         对话框标题Id，为0则不显示标题
     * @param positiveId
     *         确认按钮标题Id，为0则不显示确认按钮
     * @param negativeId
     *         取消按钮标题Id，为0则不显示取消按钮
     * @param neutralId
     *         额外按钮Id， 为0则不显示额外按钮
     * @param clickListener
     *         对话框点击监听器
     */
    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId,
            DialogInterface.OnClickListener clickListener) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, null, null, clickListener);
    }

    private DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, int contentId,
            String[] items, View view, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (titleId > 0) {
            builder.setTitle(titleId);
        }
        if (positiveId > 0) {
            builder.setPositiveButton(positiveId, clickListener);
        }
        if (negativeId > 0) {
            builder.setNegativeButton(negativeId, clickListener);
        }
        if (neutralId > 0) {
            builder.setNeutralButton(neutralId, clickListener);
        }
        if (contentId > 0) {
            builder.setMessage(contentId);
        }
        if (items != null) {
            builder.setItems(items, clickListener);
        }
        if (view != null) {
            builder.setView(view);
        }

        mDialog = builder.create();
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, int contentId,
            DialogInterface.OnClickListener clickListener) {
        this(context, titleId, positiveId, negativeId, neutralId, contentId, null, null, clickListener);
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, String[] items,
            DialogInterface.OnClickListener clickListener) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, items, null, clickListener);
    }

    public DialogHelper(Context context, int titleId, int positiveId, int negativeId, int neutralId, View view,
            DialogInterface.OnClickListener clickListener) {
        this(context, titleId, positiveId, negativeId, neutralId, 0, null, view, clickListener);
    }

    public final void hide() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public final boolean isShowing() {
        return mDialog.isShowing();
    }

    public final void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }
}