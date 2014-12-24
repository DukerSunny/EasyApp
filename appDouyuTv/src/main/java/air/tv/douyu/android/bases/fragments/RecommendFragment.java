package air.tv.douyu.android.bases.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.GsonUtil;

import java.util.List;

import air.tv.douyu.android.R;
import air.tv.douyu.android.api.API;
import air.tv.douyu.android.beans.Recommend;
import air.tv.douyu.android.beans.SlideShowRecommend;
import air.tv.douyu.android.holders.RecommendHolder;
import air.tv.douyu.android.holders.SlideShowHolder;
import air.tv.douyu.android.parsers.RecommendListParser;
import air.tv.douyu.android.parsers.SlideShowListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class RecommendFragment extends FragmentFramework {
    private View.OnClickListener mOnRoomClickListener;
    private View.OnClickListener mOnSlideShowClickListener;
    private RecommendRecyclerHelper mRecommendRecyclerHelper;
    private IRequestCallback<String> mSlideCallback;

    public static RecommendFragment create() {
        return new RecommendFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mRecommendRecyclerHelper = new RecommendRecyclerHelper(this);
        mRecommendRecyclerHelper.setHasFixedSize(false);
        mRecommendRecyclerHelper.setCanLoad(false);
        mRecommendRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnSlideShowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
        mOnRoomClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("clicked room " + v.getTag());
            }
        };
        mSlideCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                Log.e(null, "slide json failure");
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                SlideShowListParser parser = SlideShowListParser.parse(s);
                SlideShowRecommend slideShowRecommend;
                Log.e(null, "slide json" + s);

                if (parser != null) {
                    slideShowRecommend = new SlideShowRecommend();
                    Log.e(null, "slide=" + GsonUtil.toString(parser.getData()));
                    slideShowRecommend.setSlideShowList(parser.getData());
                    mRecommendRecyclerHelper.addItem(0, slideShowRecommend);
                    mRecommendRecyclerHelper.scrollToTop();
                }
            }
        };
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_recommend);
    }

    @Override
    public void startAction() {
        mRecommendRecyclerHelper.from(API.getRecommend());
    }

    private class RecommendRecyclerHelper extends RecyclerFramework<Recommend> {
        private final static int CONTENT = 1;
        private final static int HEADER = 0;

        public RecommendRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Recommend> createHolder(View itemView, int viewType) {
            SlideShowHolder slideShowHolder;

            if (viewType == HEADER) {
                slideShowHolder = new SlideShowHolder(itemView);
                slideShowHolder.setOnSlideShowClickListener(mOnSlideShowClickListener);

                return slideShowHolder;
            } else {
                return new RecommendHolder(itemView, mOnRoomClickListener);
            }
        }

        @Override
        protected View createView(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            if (viewType == HEADER) {
                return inflater.inflate(R.layout.item_slideshow, parent, false);
            } else {
                return inflater.inflate(R.layout.item_recommend, parent, false);
            }
        }

        @Override
        protected int getViewType(int position) {
            return getItem(position) instanceof SlideShowRecommend ? HEADER : CONTENT;
        }

        @Override
        public void onItemClick(int position, Recommend recommend) {
            showToast(
                    "clicked recommend " + position + " " + recommend.getTitle() + " " + recommend.getClass().getSimpleName());
        }

        @Override
        protected List<Recommend> onParse(String json) {
            RecommendListParser parser = RecommendListParser.parse(json);

            if (parser != null) {
                return parser.getData();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostAction(boolean success) {
            super.onPostAction(success);
            Log.e(null, "success=" + success);
            if (success) {
                executeRequest(API.getSlide(4), mSlideCallback);
            }
        }

        @Override
        protected void onRequestAction() {
            startAction();
        }

        @Override
        protected void setItemDecoration() {
        }
    }
}