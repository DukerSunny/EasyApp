package com.harreke.easyapp.helpers;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.pages.imagepage.ImagePageFramework;
import com.harreke.easyapp.listeners.OnSlideClickListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.FrameworkInflater;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 幻灯片助手
 */
public class ImageSlideHelper extends ImagePageFramework<String> {
    private static final int SLIDE_INTERVAL = 5000;

    private IFramework mFramework;
    private int mImageLayoutId;
    private View.OnClickListener mIndicatorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            check((Integer) view.getTag());
        }
    };
    private int mIndicatorLayoutId;
    private int mPosition = 0;
    private Runnable mSlideRunnable = new Runnable() {
        @Override
        public void run() {
            mPosition++;
            if (mPosition >= getCount()) {
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
                mSlideClickListener.onSlideClick(mPosition, getItem(mPosition));
            }
        }
    };
    private Handler mSlideHandler = new Handler();
    private ViewGroup slide_indicator;
    private ViewPager slide_page;

    public ImageSlideHelper(IFramework framework, int pageId, int imageLayoutId, int indicatorId) {
        super(framework, pageId);
        mFramework = framework;
        mImageLayoutId = imageLayoutId;

        slide_page = (ViewPager) framework.findContentView(pageId);
        slide_indicator = (ViewGroup) framework.findContentView(indicatorId);
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
    public final void clear() {
        super.clear();
        pause();
        slide_indicator.removeAllViews();
    }

    @Override
    public ImageView createPage(int position, String url) {
        ImageView image = (ImageView) FrameworkInflater.inflate(mFramework, mImageLayoutId, null);

        if (image != null) {
            image.setOnClickListener(mPageClickListener);
            ImageLoaderHelper.loadImage(image, url, new SlideCallback(position));
        }

        return image;
    }

    public final void destroy() {
        clear();
        mFramework = null;
    }

    public final void from(ArrayList<String> imageList) {
        View indicator;
        int i;

        if (imageList != null) {
            for (i = 0; i < imageList.size(); i++) {
                add(imageList.get(i));
                indicator = FrameworkInflater.inflate(mFramework, mIndicatorLayoutId, slide_indicator);
                if (indicator != null) {
                    indicator.setOnClickListener(mIndicatorClickListener);
                    slide_indicator.addView(indicator);
                }
            }
            refresh();
        }
    }

    public final int getPosition() {
        return mPosition;
    }

    @Override
    public void onPageChange(int position) {
        if (mPosition != position) {
            mPosition = position;
            check(position);
        }
    }

    public final void pause() {
        mSlideHandler.removeCallbacks(mSlideRunnable);
    }

    public final void setOnSlideClickListener(OnSlideClickListener slideClickListener) {
        mSlideClickListener = slideClickListener;
    }

    public final void setPosition(int position) {
        if (position >= 0 && position < getCount()) {
            slide_page.setCurrentItem(position);
        }
    }

    public final void start() {
        mSlideHandler.postDelayed(mSlideRunnable, SLIDE_INTERVAL);
    }

    private class SlideCallback implements IRequestCallback<ImageView> {
        private int mTtargetPosition;

        public SlideCallback(int targetPosition) {
            mTtargetPosition = targetPosition;
        }

        @Override
        public void onFailure(String requestUrl) {
            setLoaded(mTtargetPosition, false);
        }

        @Override
        public void onSuccess(String requestUrl, ImageView imageView) {
            setLoaded(mTtargetPosition, true);
        }
    }
}