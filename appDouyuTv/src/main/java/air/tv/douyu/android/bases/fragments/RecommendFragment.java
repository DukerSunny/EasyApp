package air.tv.douyu.android.bases.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import air.tv.douyu.android.R;
import air.tv.douyu.android.api.API;
import air.tv.douyu.android.bases.activities.LiveActivity;
import air.tv.douyu.android.bases.activities.RoomActivity;
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
    private View.OnClickListener mOnTitleClickListener;
    private IRequestCallback<String> mRecommendCallback;
    private List<Recommend> mRecommendList = new ArrayList<Recommend>();
    private RecommendRecyclerHelper mRecommendRecyclerHelper;
    private IRequestCallback<String> mSlideCallback;
    private SlideShowHolder mSlideShowHolder = null;

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
                //                start(RoomActivity.create(getContext(), (Integer) v.getTag(), (ImageView) v));
                start(RoomActivity.create(getContext(), ViewUtil.getIntTag(v)));
            }
        };
        mOnTitleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(LiveActivity.create(getContext(), ViewUtil.getStringTag(v, R.id.value), ViewUtil.getIntTag(v, R.id.key)));
            }
        };
        mOnRoomClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                start(RoomActivity.create(getContext(), (Integer) v.getTag(), (ImageView) v.findViewById(R.id.room_src)),
                //                        ActivityFramework.Anim.None);
                start(RoomActivity.create(getContext(), ViewUtil.getIntTag(v)));
            }
        };
        mSlideCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mRecommendRecyclerHelper.showEmptyFailureIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                SlideShowListParser parser = SlideShowListParser.parse(s);
                SlideShowRecommend slideShowRecommend;

                if (parser != null) {
                    slideShowRecommend = new SlideShowRecommend();
                    slideShowRecommend.setSlideShowList(parser.getData());
                    mRecommendList.clear();
                    mRecommendList.add(slideShowRecommend);
                    executeRequest(API.getRecommend(), mRecommendCallback);
                }
            }
        };
        mRecommendCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mRecommendRecyclerHelper.showEmptyIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String json) {
                RecommendListParser parser = RecommendListParser.parse(json);

                if (parser != null) {
                    mRecommendList.addAll(parser.getData());
                    mRecommendRecyclerHelper.from(mRecommendList);
                } else {
                    mRecommendRecyclerHelper.showEmptyIdle();
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void onPause() {
        if (mSlideShowHolder != null) {
            mSlideShowHolder.stopSlide();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSlideShowHolder != null) {
            mSlideShowHolder.startSlide();
        }
    }

    @Override
    public void startAction() {
        mRecommendRecyclerHelper.clear();
        mRecommendRecyclerHelper.showEmptyLoading();
        executeRequest(API.getSlide(4), mSlideCallback);
    }

    private class RecommendRecyclerHelper extends RecyclerFramework<Recommend> {
        private final static int CONTENT = 1;
        private final static int HEADER = 0;

        public RecommendRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Recommend> createHolder(View itemView, int viewType) {
            if (viewType == HEADER) {
                mSlideShowHolder = new SlideShowHolder(itemView);
                mSlideShowHolder.setOnSlideShowClickListener(mOnSlideShowClickListener);

                return mSlideShowHolder;
            } else {
                return new RecommendHolder(itemView, mOnTitleClickListener, mOnRoomClickListener);
            }
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
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
        }

        @Override
        protected List<Recommend> onParse(String json) {
            return null;
        }

        @Override
        protected void setItemDecoration() {
        }
    }
}