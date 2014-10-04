package com.harreke.easyapp.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public abstract class FragmentPageAdapter extends FragmentPagerAdapter {
    private FragmentManager mManager;
    private int mTotalPage = 0;
    private ViewGroup mViewGroup;

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
        mManager = fm;
    }

    public void clear() {
        int i;

        for (i = 0; i < mTotalPage; i++) {
            remove(i);
        }
    }

    @Override
    public int getCount() {
        return mTotalPage;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public final void refresh() {
        notifyDataSetChanged();
    }

    public void remove(int position) {
        FragmentTransaction transaction;
        Fragment fragment;
        String tag;

        tag = "android:switcher:" + mViewGroup.getId() + ":" + position;
        fragment = mManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction = mManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            mManager.executePendingTransactions();
        }
    }

    public final void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mViewGroup = container;
    }
}