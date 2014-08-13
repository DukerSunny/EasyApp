package com.harreke.easyapp.helpers;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.harreke.easyapp.adapters.viewpager.ImagePageAdapter;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.pages.IPager;
import com.harreke.easyapp.R;
import com.harreke.easyapp.listeners.OnSlideClickListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.FrameworkInflater;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 幻灯片助手
 */
public abstract class SlideHelper implements IPager<String, ImageView>, ViewPager.OnPageChangeListener {
    private static final int SLIDE_INTERVAL = 5000;

    private Adapter mAdapter;
    private IFramework mFramework;
    private View.OnClickListener mIndicatorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            check((Integer) view.getTag());
        }
    };
    private int mIndicatorLayoutId;
    private int mPageLayoutId;
    private int mPosition = 0;
    private Runnable mSlideRunnable = new Runnable() {
        @Override
        public void run() {
            mPosition++;
            if (mPosition >= mAdapter.getCount()) {
                mPosition = 0;
            }
            check(mPosition);
        }
    };
    private OnSlideClickListener mSlideClickListener = null;
    private View.OnClickListener mPageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mSlideClickListener != null) {
                mSlideClickListener.onSlideClick(mPosition, mAdapter.getItem(mPosition));
            }
        }
    };
    private Handler mSlideHandler = new Handler();
    private LinearLayout slide_indicator;
    private ViewPager slide_page;

    public SlideHelper(IFramework framework, int imageLayoutId, int indicatorLayoutId) {
        mFramework = framework;
        mPageLayoutId = imageLayoutId;
        mIndicatorLayoutId = indicatorLayoutId;

        slide_page = (ViewPager) framework.queryContent(R.id.slide_page);
        slide_indicator = (LinearLayout) framework.queryContent(R.id.slide_indicator);

        mAdapter = new Adapter();
        slide_page.setAdapter(mAdapter);
    }

    public final void destroy() {
        clear();
        mFramework = null;
    }

    public final void clear() {
        pause();
        mAdapter.clear();
        mAdapter.refresh();
        slide_indicator.removeAllViews();
    }

    public final void pause() {
        mSlideHandler.removeCallbacks(mSlideRunnable);
    }

    public final void from(ArrayList<String> imageList) {
        View indicator;
        int i;

        if (imageList != null) {
            for (i = 0; i < imageList.size(); i++) {
                mAdapter.add(imageList.get(i));
                indicator = FrameworkInflater.inflate(mFramework, mIndicatorLayoutId, slide_indicator);
                if (indicator != null) {
                    indicator.setOnClickListener(mIndicatorClickListener);
                    slide_indicator.addView(indicator);
                }
            }
            refresh();
        }
    }

    public final void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    public final int getPosition() {
        return mPosition;
    }

    public final void setPosition(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            slide_page.setCurrentItem(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (mPosition != position) {
            mPosition = position;
            check(position);
        }
    }

    private void check(int position) {
        ImageView page;
        int i;

        for (i = 0; i < slide_indicator.getChildCount(); i++) {
            page = (ImageView) slide_indicator.getChildAt(i);
            if (i == position) {
                page.setImageResource(R.drawable.shape_dot_checked);
            } else {
                page.setImageResource(R.drawable.shape_dot);
            }
        }
        slide_page.setCurrentItem(position, true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public final void setOnSlideClickListener(OnSlideClickListener slideClickListener) {
        mSlideClickListener = slideClickListener;
    }

    public final void start() {
        mSlideHandler.postDelayed(mSlideRunnable, SLIDE_INTERVAL);
    }

    private class Adapter extends ImagePageAdapter<ImageView> {
        @Override
        public ImageView createPage(int position, String url) {
            ImageView image = (ImageView) FrameworkInflater.inflate(mFramework, mPageLayoutId, null);

            if (image != null) {
                image.setOnClickListener(mPageClickListener);
                ImageLoaderHelper.loadImage(image, url, new SlideCallback(position));
            }

            return image;
        }
    }

    private class SlideCallback implements IRequestCallback<ImageView> {
        private int mTtargetPosition;

        public SlideCallback(int targetPosition) {
            mTtargetPosition = targetPosition;
        }

        @Override
        public void onFailure() {
            mAdapter.setLoaded(mTtargetPosition, false);
        }

        @Override
        public void onSuccess(ImageView imageView) {
            mAdapter.setLoaded(mTtargetPosition, true);
        }
    }
}