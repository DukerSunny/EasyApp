package com.harreke.easyapp.widgets.expandabletabview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.harreke.easyapp.widgets.ChildSettingView;
import com.harreke.easyapp.widgets.GroupSettingView;
import com.harreke.easyapp.widgets.expandabletabview.inners.FixedSubTabView;
import com.harreke.easyapp.widgets.expandabletabview.inners.DividedSubTabView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/15
 *
 * 扩展标签视图
 *
 * 扩展标签是一种可以用于快速导航页面的视图，分为两个部分：主标签条和子标签条
 *
 * 1.主标签条用来显示主标签；
 * 2.子标签条用来显示一个主标签下包含的子标签
 *
 * 子标签可设置两种模式：分割或固定
 *
 * 要选择一个主标签下的子标签，按下一个主标签，主标签条下方会显示出子标签条，可以选择：
 *
 * 1.不松手，手指直接从主标签滑动到一个子标签，然后松手，选择该子标签；
 * 2.松手，子标签条会短暂停留一段时间，此时再点击一个子标签，选择该子标签
 *
 * 一旦选择了一个子标签，或者松手后一段时间没再触碰扩展标签视图，子标签条都会消失
 */
public class ExpandableTabView extends GroupSettingView {
    private boolean mSubTabAdded = false;
    private GroupSettingView mSubTabContainer;
    private boolean mSubTabEnabled = false;
    private TabMode mTabMode = TabMode.DIVIDED;
    private WindowManager mWindowManager;

    public ExpandableTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public final void addMainTab(ChildSettingView childSettingView) {
        addView(childSettingView);
    }

    public final void addMainTab() {

    }

    public final void enableSubTab() {
        mSubTabEnabled = true;
        generateSubTabContainer();
    }

    private void generateSubTabContainer() {
        switch (mTabMode) {
            case DIVIDED:
                mSubTabContainer = new DividedSubTabView(getContext());
                break;
            case FIXED:
                mSubTabContainer = new FixedSubTabView(getContext());
        }
    }

    public final void hideSubTab() {
        mWindowManager.removeView(mSubTabContainer);
        mSubTabAdded = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mSubTabAdded) {
            hideSubTab();
        }
        super.onDetachedFromWindow();
    }

    /**
     * 设置子标签模式
     *
     * 必须在初始化子标签条之前设置才能生效
     */
    public final void setTabMode(TabMode tabMode) {
        mTabMode = tabMode;
    }

    public final void showSubTab() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        params.x = 0;
        params.y = getBottom();
        params.token = getWindowToken();
        params.setTitle("SubTabContainer:" + Integer.toHexString(mSubTabContainer.hashCode()));
        mWindowManager.addView(mSubTabContainer, params);
        mSubTabAdded = true;
    }

    public static enum TabMode {
        /**
         * 标签模式——分割
         *
         * 表示每个标签会平分屏幕宽度，并且固定位置，不可滑动
         */
        DIVIDED,
        /**
         * 标签模式——固定
         *
         * 表示每个标签有固定的宽度，并且位置不固定，可以水平滑动
         */
        FIXED
    }
}