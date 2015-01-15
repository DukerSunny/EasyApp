package com.harreke.easyapp.helpers;

import android.util.Log;
import android.widget.CompoundButton;

import com.harreke.easyapp.listeners.OnButtonsCheckedChangeListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class CompoundButtonHelper {
    private boolean mBlock = false;
    private CompoundButton[] mCompoundButtons;
    private OnButtonsCheckedChangeListener mOnButtonsCheckedChangeListener = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = 0;
            int i;

            Log.e(null, "on checked change listener");
            if (!mBlock) {
                mBlock = true;
                for (i = 0; i < mCompoundButtons.length; i++) {
                    if (!mCompoundButtons[i].equals(buttonView)) {
                        mCompoundButtons[i].setChecked(false);
                    } else {
                        position = i;
                    }
                }
                mBlock = false;
                if (mOnButtonsCheckedChangeListener != null) {
                    mOnButtonsCheckedChangeListener.onButtonCheck(buttonView, position);
                }
            }
        }
    };

    public CompoundButtonHelper(CompoundButton... compoundButtons) {
        int i;

        if (compoundButtons == null || compoundButtons.length == 0) {
            throw new IllegalArgumentException("Compound buttons are null!");
        }
        Log.e(null, "collect buttons " + compoundButtons.length);
        mCompoundButtons = compoundButtons;
        for (i = 0; i < mCompoundButtons.length; i++) {
            mCompoundButtons[i].setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

    public void check(int position) {
        if (position >= 0 && position < mCompoundButtons.length) {
            mCompoundButtons[position].setChecked(true);
        }
    }

    public void checkById(int buttonId) {
        int i;

        for (i = 0; i < mCompoundButtons.length; i++) {
            if (i == mCompoundButtons[i].getId()) {
                break;
            }
        }
        if (i < mCompoundButtons.length) {
            check(i);
        }
    }

    public void setOnButtonsCheckedChangeListener(OnButtonsCheckedChangeListener onButtonsCheckedChangeListener) {
        mOnButtonsCheckedChangeListener = onButtonsCheckedChangeListener;
    }
}