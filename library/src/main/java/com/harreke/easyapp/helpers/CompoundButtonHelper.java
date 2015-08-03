package com.harreke.easyapp.helpers;

import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import com.harreke.easyapp.listeners.OnButtonsCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class CompoundButtonHelper {
    private boolean mBlock = false;
    private List<CompoundButton> mCompoundButtonList;
    private OnButtonsCheckedChangeListener mOnButtonsCheckedChangeListener = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CompoundButton compoundButton;
            int position = 0;
            int i;

            if (!mBlock) {
                mBlock = true;
                for (i = 0; i < mCompoundButtonList.size(); i++) {
                    compoundButton = mCompoundButtonList.get(i);
                    if (!compoundButton.equals(buttonView)) {
                        compoundButton.setChecked(false);
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

    public CompoundButtonHelper(@NonNull CompoundButton... compoundButtons) {
        mCompoundButtonList = new ArrayList<>();
        add(compoundButtons);
    }

    public void add(@NonNull CompoundButton... compoundButtons) {
        CompoundButton compoundButton;
        int i;

        for (i = 0; i < compoundButtons.length; i++) {
            compoundButton = compoundButtons[i];
            compoundButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
            mCompoundButtonList.add(compoundButton);
        }
    }

    public void check(int position) {
        int i;

        if (position >= 0 && position < mCompoundButtonList.size()) {
            mBlock = true;
            for (i = 0; i < mCompoundButtonList.size(); i++) {
                if (position != i) {
                    mCompoundButtonList.get(i).setChecked(false);
                }
            }
            mCompoundButtonList.get(position).setChecked(true);
            mBlock = false;
        }
    }

    public void checkById(int buttonId) {
        int i;

        for (i = 0; i < mCompoundButtonList.size(); i++) {
            if (i == mCompoundButtonList.get(i).getId()) {
                break;
            }
        }
        if (i < mCompoundButtonList.size()) {
            check(i);
        }
    }

    public void clear() {
        int i;

        for (i = 0; i < mCompoundButtonList.size(); i++) {
            mCompoundButtonList.get(i).setOnCheckedChangeListener(null);
        }
        mCompoundButtonList.clear();
    }

    public void setOnButtonCheckedChangeListener(OnButtonsCheckedChangeListener onButtonsCheckedChangeListener) {
        mOnButtonsCheckedChangeListener = onButtonsCheckedChangeListener;
    }
}