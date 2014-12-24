package air.tv.douyu.android.holders;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harreke.easyapp.adapters.viewpager.PageAdapter;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RatioImageView;

import java.util.List;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Recommend;
import air.tv.douyu.android.beans.SlideShow;
import air.tv.douyu.android.beans.SlideShowRecommend;
import me.relex.circleindicator.CircleIndicator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowHolder extends RecyclerHolder<Recommend> {
    private ViewPager.OnPageChangeListener mOnSlideChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            slideshow_title.setText(mSlideShowAdapter.getItem(position).getTitle());
        }
    };
    private View.OnClickListener mOnSlideShowClickListener;
    private SlideShowAdapter mSlideShowAdapter;
    private CircleIndicator slideshow_indicator;
    private ViewPager slideshow_pager;
    private View slideshow_root;
    private TextView slideshow_title;

    public SlideShowHolder(View itemView) {
        super(itemView);

        slideshow_root = itemView.findViewById(R.id.slideshow_root);
        slideshow_pager = (ViewPager) itemView.findViewById(R.id.slideshow_pager);
        slideshow_title = (TextView) itemView.findViewById(R.id.slideshow_title);
        slideshow_indicator = (CircleIndicator) itemView.findViewById(R.id.slideshow_indicator);
    }

    @Override
    public void setItem(Recommend recommend) {
        List<SlideShow> slideShowList;

        if (recommend instanceof SlideShowRecommend) {
            Log.e(null, "slideshow set item");
            slideShowList = ((SlideShowRecommend) recommend).getSlideShowList();
            if (slideShowList != null && slideShowList.size() > 0) {
                slideshow_root.setVisibility(View.GONE);
                slideshow_pager.setVisibility(View.VISIBLE);
                mSlideShowAdapter = new SlideShowAdapter();
                mSlideShowAdapter.add(slideShowList);

                slideshow_pager.setOnPageChangeListener(mOnSlideChangeListener);
                slideshow_pager.setAdapter(mSlideShowAdapter);
                slideshow_indicator.setViewPager(slideshow_pager);
            } else {
                slideshow_root.setVisibility(View.VISIBLE);
                slideshow_pager.setVisibility(View.GONE);
            }
        } else {
            Log.e(null, "not a slideshow item");
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
    }

    public void setOnSlideShowClickListener(View.OnClickListener onSlideShowClickListener) {
        mOnSlideShowClickListener = onSlideShowClickListener;
    }

    private class SlideShowAdapter extends PageAdapter<SlideShow, RatioImageView> {
        @Override
        protected RatioImageView createPage(ViewGroup container, int position, SlideShow slideShow) {
            Log.e(null, "createPage" + position + " slideshow=" + slideShow.getTitle());
            RatioImageView ratioImageView = (RatioImageView) LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_slideshow_page, container, false);

            ratioImageView.setTag(slideShow.getId());
            ratioImageView.setOnClickListener(mOnSlideShowClickListener);
            ImageLoaderHelper.loadImage(ratioImageView, slideShow.getPic_url(), R.drawable.image_loading_16x9,
                    R.drawable.image_retry_16x9);

            return ratioImageView;
        }
    }
}