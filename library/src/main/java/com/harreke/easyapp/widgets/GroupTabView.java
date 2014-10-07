package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/16
 *
 * 标签组视图
 *
 * 标签组视图是一个线性布局，放入该布局的子标签视图{@link com.harreke.easyapp.widgets.ChildTabView}将成为互斥状态，即单选模式
 *
 * 标签组视图的选择模式{@link com.harreke.easyapp.widgets.GroupTabView.TabMode}有两种：固定和滑动。
 *
 * 无论哪种选择模式，在按下时，手指所处的子标签将获得焦点，通过滑动手势可改变焦点，松手时将选中获得焦点的子标签。
 */
public class GroupTabView extends LinearLayout implements ViewGroup.OnHierarchyChangeListener, CompoundButton.OnCheckedChangeListener {
    private boolean mBlock = false;
    private int mCheckedPosition = -1;
    private OnCheckedChangeListener mGroupCheckedChangeListener = null;
    private int mPressedPosition = -1;
    private TabMode mTabMode = TabMode.SCROLLABLE;

    public GroupTabView(Context context) {
        this(context, null);
    }

    public GroupTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnHierarchyChangeListener(this);
    }

    /**
     * 选中指定位置的标签
     *
     * @param position
     *         标签位置
     */
    public final void check(int position) {
        if (position != mCheckedPosition) {
            mBlock = true;
            setChecked(mCheckedPosition, false);
            mBlock = false;
            setChecked(position, true);
            mCheckedPosition = position;
        }
    }

    private int getEventPosition(MotionEvent event) {
        View view;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int i;

        for (i = 0; i < getChildCount(); i++) {
            view = getChildAt(i);
            if (x >= view.getLeft() && x <= view.getRight() && y >= view.getTop() && y <= view.getBottom()) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 获得标签组视图的选择模式
     *
     * @return 选择模式
     *
     * @see com.harreke.easyapp.widgets.GroupTabView.TabMode
     */
    public final TabMode getTabMode() {
        return mTabMode;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position;

        if (!mBlock) {
            position = indexOfChild(buttonView);
            if (position != mCheckedPosition) {
                setChecked(mCheckedPosition, false);
                mCheckedPosition = position;
                if (mGroupCheckedChangeListener != null) {
                    mGroupCheckedChangeListener.onCheckedChanged(this, position, (ChildTabView) buttonView);
                }
            }
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        ChildTabView childTabView;

        if (child instanceof ChildTabView) {
            childTabView = (ChildTabView) child;
            if (childTabView.isChecked()) {
                mBlock = true;
                setChecked(mCheckedPosition, false);
                mBlock = false;
                mCheckedPosition = indexOfChild(childTabView);
            }
            ((ChildTabView) child).setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return pressEventChild(event);
    }

    private void press(int position) {
        if (position != mPressedPosition) {
            setPressed(mPressedPosition, false);
            setPressed(position, true);
            mPressedPosition = position;
        }
    }

    private boolean pressEventChild(MotionEvent event) {
        int position = getEventPosition(event);

        if (position > -1) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                press(position);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                check(position);
                setPressed(position, false);

                return true;
            }
        } else {
            setPressed(mPressedPosition, false);
            mPressedPosition = -1;
        }

        return false;
    }

    private void setChecked(int position, boolean checked) {
        if (position >= 0 && position < getChildCount()) {
            ((ChildTabView) getChildAt(position)).setChecked(checked);
        }
    }

    /**
     * 设置标签选中监听器
     *
     * @param checkedChangeListener
     *         标签选中监听器
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        mGroupCheckedChangeListener = checkedChangeListener;
    }

    private void setPressed(int position, boolean pressed) {
        if (position >= 0 && position < getChildCount()) {
            getChildAt(position).setPressed(pressed);
        }
    }

    /**
     * 设置标签组视图的选择模式
     *
     * 默认选择模式为固定
     *
     * @param tabMode
     *         选择模式
     *
     * @see com.harreke.easyapp.widgets.GroupTabView.TabMode
     */
    public final void setTabMode(TabMode tabMode) {
        mTabMode = tabMode;
    }

    /**
     * 子标签视图选择模式
     */
    public enum TabMode {
        /**
         * 固定模式
         *
         * 标签组视图大小固定，内部的每个子标签视图平分容器大小。
         */
        FIXED,
        /**
         * 滑动模式
         *
         * 标签组视图大小不固定（通常大于屏幕的宽度或高度），内部的每个子标签视图有各自的大小。在标签组视图上滑动也会使整个视图和其内部的子标签滚动。
         */
        SCROLLABLE
    }

    public interface OnCheckedChangeListener {
        public void onCheckedChanged(GroupTabView groupTabView, int position, ChildTabView childTabView);
    }
}