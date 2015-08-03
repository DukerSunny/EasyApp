package com.harreke.easyapp.frameworks.viewpager;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.utils.ResourceUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public abstract class FragmentPagerFramework implements ViewPager.OnPageChangeListener {
    private FragmentPageAdapter mAdapter = null;
    private boolean mAttached = false;
    private FragmentManager mManager = null;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mViewPagerStrip;

    public FragmentPagerFramework(IFramework framework) {
        mManager = framework.getActivityFramework().getSupportFragmentManager();
        mViewPager = (ViewPager) framework.findViewById(getViewPagerId());
        mViewPagerStrip = (PagerSlidingTabStrip) framework.findViewById(getPagerStripId());
        if (mViewPagerStrip != null) {
            mViewPagerStrip.setOnPageChangeListener(this);
        } else {
            mViewPager.addOnPageChangeListener(this);
        }
        setPagerStripTextColor(Color.WHITE);
        setPagerStripTextSize((int) ResourceUtil.getDimension(framework.getContext(), R.dimen.Subhead));
    }

    public void attachAdapter() {
        mAdapter = new Adapter(mManager);
        mViewPager.setAdapter(mAdapter);
        if (mViewPagerStrip != null) {
            mViewPagerStrip.setViewPager(mViewPager);
            mAttached = true;
        }
    }

    protected abstract Fragment createFragment(int position);

    protected abstract CharSequence createFragmentTitle(int position);

    public final int getCurrentPage() {
        return mViewPager.getCurrentItem();
    }

    protected abstract int getFragmentCount();

    protected int getPagerStripId() {
        return R.id.viewpager_strip;
    }

    protected int getViewPagerId() {
        return R.id.viewpager;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    public void refreshCurrentPage() {
        refreshPage(getCurrentPage());
    }

    public void refreshPage(int position) {
        if (mAdapter != null) {
            mAdapter.refresh(position);
        }
    }

    public void refreshPager() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void refreshStrip() {
        if (mViewPagerStrip != null && mAttached) {
            mViewPagerStrip.notifyDataSetChanged();
        }
    }

    public final void setCurrentPage(int position, boolean smoothScroll) {
        mViewPager.setCurrentItem(position, smoothScroll);
    }

    public void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    public void setPagerStripTextColor(int textColor) {
        if (mViewPagerStrip != null) {
            mViewPagerStrip.setTextColor(textColor);
        }
    }

    public void setPagerStripTextSize(int textSize) {
        if (mViewPagerStrip != null) {
            mViewPagerStrip.setTextSize(textSize);
        }
    }

    public void setPagerStripTextColorId(int resId) {
        if (mViewPagerStrip != null) {
            mViewPagerStrip.setTextColorStateListResource(resId);
        }
    }

    private class Adapter extends FragmentPageAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return getFragmentCount();
        }

        @Override
        public Fragment getItem(int position) {
            return createFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return createFragmentTitle(position);
        }
    }
}