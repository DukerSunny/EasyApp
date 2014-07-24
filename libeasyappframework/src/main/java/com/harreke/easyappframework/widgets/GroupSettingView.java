package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.LinearLayout;

import com.harreke.easyappframework.listeners.OnChildSettingChangeListener;
import com.harreke.easyappframework.listeners.OnGroupSettingChangeListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 多选设置视图
 *
 * 具有跟RadioGroup类似的运作效果，是单选设置视图（ChildSettingView）的包装
 *
 * @see com.harreke.easyappframework.widgets.ChildSettingView
 */
public class GroupSettingView extends LinearLayout implements OnHierarchyChangeListener, OnChildSettingChangeListener {
    private int mCheckedPosition = -1;
    private OnGroupSettingChangeListener mGroupSettingChangeListener = null;

    public GroupSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnHierarchyChangeListener(this);
    }

    /**
     * 选中指定的体设置视图
     *
     * @param position
     *         单选设置视图位置
     */
    public final void check(int position) {
        ChildSettingView child;
        int i;

        if (position >= 0 && position < getChildCount() && mCheckedPosition != position) {
            for (i = 0; i < getChildCount(); i++) {
                child = (ChildSettingView) getChildAt(i);
                if (i != position) {
                    child.setChecked(false);
                }
            }
            child = (ChildSettingView) getChildAt(position);
            if (child != null) {
                child.setChecked(true);
            }
            mCheckedPosition = position;
        }
    }

    /**
     * 获得指定的单选设置视图
     *
     * @return 单选设置视图
     */
    public final ChildSettingView getCheckedChild() {
        return (ChildSettingView) getChildAt(mCheckedPosition);
    }

    /**
     * 获得被选中的单选设置视图位置
     *
     * @return 被选中的单选设置视图位置
     */
    public final int getCheckedPosition() {
        return mCheckedPosition;
    }

    @Override
    public void onChildSettingChange(ChildSettingView view, boolean isChecked) {
        ChildSettingView child;
        ChildSettingView checkedChild = null;
        int position = 0;
        int i;

        for (i = 0; i < getChildCount(); i++) {
            child = (ChildSettingView) getChildAt(i);
            if (!child.equals(view)) {
                child.setChecked(false);
            } else {
                checkedChild = child;
                position = i;
            }
        }
        if (mCheckedPosition != position) {
            mCheckedPosition = position;
            if (mGroupSettingChangeListener != null) {
                mGroupSettingChangeListener.onGroupSettingChange(this, checkedChild, position);
            }
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        ChildSettingView childSettingView;
        ChildSettingView view;
        int index = indexOfChild(child);
        int i;

        if (child instanceof ChildSettingView) {
            childSettingView = (ChildSettingView) child;
            childSettingView.setOnChildSettingListener(this);
            if (childSettingView.isChecked()) {
                for (i = 0; i < index; i++) {
                    view = (ChildSettingView) getChildAt(i);
                    view.setChecked(false);
                }
                mCheckedPosition = index;
            }
        } else {
            throw new IllegalArgumentException("Can only insert ChildSettingView inside of GroupSettingView");
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    /**
     * 设置多选状态监听器
     *
     * @param groupSettingChangeListener
     *         多选状态监听器
     */
    public final void setOnGroupSettingChangeListener(OnGroupSettingChangeListener groupSettingChangeListener) {
        mGroupSettingChangeListener = groupSettingChangeListener;
    }
}