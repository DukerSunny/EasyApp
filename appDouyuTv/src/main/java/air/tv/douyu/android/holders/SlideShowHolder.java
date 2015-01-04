package air.tv.douyu.android.holders;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.adapters.viewpager.PageAdapter;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Recommend;
import air.tv.douyu.android.beans.SlideShow;
import air.tv.douyu.android.beans.SlideShowRecommend;
import me.relex.circleindicator.CircleIndicator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowHolder extends RecyclerHolder<Recommend> implements ViewPager.OnPageChangeListener {
    private final static int SLIDE_INTERVAL = 5000;
    private Runnable mSlideRunnable = new Runnable() {
        @Override
        public void run() {
            int position = slideshow_pager.getCurrentItem();

            position++;
            if (position >= mSlideShowAdapter.getCount()) {
                position = 0;
            }
            slideshow_pager.setCurrentItem(position);
            mHandler.postDelayed(this, SLIDE_INTERVAL);
        }
    };
    private Handler mHandler = new Handler();
    private View.OnClickListener mOnSlideShowClickListener;
    private SlideShowAdapter mSlideShowAdapter;
    private CircleIndicator slideshow_indicator;
    private ViewPager slideshow_pager;
    private TextView slideshow_title;

    public SlideShowHolder(View itemView) {
        super(itemView);
        ViewGroup.LayoutParams params;

        slideshow_pager = (ViewPager) itemView.findViewById(R.id.slideshow_pager);
        params = slideshow_pager.getLayoutParams();
        params.width = itemView.getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 9f / 16f);
        slideshow_pager.setLayoutParams(params);
        slideshow_title = (TextView) itemView.findViewById(R.id.slideshow_title);
        slideshow_indicator = (CircleIndicator) itemView.findViewById(R.id.slideshow_indicator);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        slideshow_title.setText(mSlideShowAdapter.getItem(position).getTitle());
        stopSlide();
        startSlide();
    }

    @Override
    public void setItem(Recommend recommend) {
        List<SlideShow> slideShowList;

        if (recommend instanceof SlideShowRecommend) {
            slideShowList = ((SlideShowRecommend) recommend).getSlideShowList();
            if (slideShowList != null && slideShowList.size() > 0) {
                mSlideShowAdapter = new SlideShowAdapter();
                mSlideShowAdapter.add(slideShowList);

                slideshow_pager.setOnPageChangeListener(this);
                slideshow_pager.setOffscreenPageLimit(3);
                slideshow_pager.setAdapter(mSlideShowAdapter);
                slideshow_indicator.setViewPager(slideshow_pager);
                slideshow_indicator.setOnPageChangeListener(this);
                startSlide();
            }
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
    }

    public void setOnSlideShowClickListener(View.OnClickListener onSlideShowClickListener) {
        mOnSlideShowClickListener = onSlideShowClickListener;
    }

    public void startSlide() {
        if (mSlideShowAdapter != null && mSlideShowAdapter.getCount() > 1) {
            stopSlide();
            mHandler.postDelayed(mSlideRunnable, SLIDE_INTERVAL);
        }
    }

    public void stopSlide() {
        mHandler.removeCallbacks(mSlideRunnable);
    }

    private class SlideShowAdapter extends PageAdapter<SlideShow, ImageView> {
        @Override
        protected ImageView createView(ViewGroup container, int position, SlideShow slideShow) {
            ImageView imageView = new ImageView(container.getContext());

            imageView.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setTag(slideShow.getId());
            imageView.setOnClickListener(mOnSlideShowClickListener);
            ImageLoaderHelper.loadImage(imageView, slideShow.getPic_url(), R.drawable.loading_16x9, R.drawable.retry_16x9);

            return imageView;
        }
    }
}