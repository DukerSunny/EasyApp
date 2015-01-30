package tv.douyu.control.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.Recommend;
import tv.douyu.model.bean.SlideShow;
import tv.douyu.model.bean.SlideShowRecommend;
import tv.douyu.wrapper.holder.RecommendHolder;
import tv.douyu.wrapper.holder.SlideShowHolder;
import tv.douyu.model.parser.RecommendListParser;
import tv.douyu.model.parser.SlideShowListParser;
import tv.douyu.control.activity.LiveActivity;
import tv.douyu.control.activity.RoomActivity;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class RecommendFragment extends FragmentFramework {
    private View.OnClickListener mOnRoomClickListener;
    private View.OnClickListener mOnSlideShowClickListener;
    private View.OnClickListener mOnTitleClickListener;
    private IRequestCallback<String> mRecommendCallback;
    private List<Recommend> mRecommendList = new ArrayList<Recommend>();
    private RecommendListParser mRecommendListParser;
    private RecommendRecyclerHelper mRecommendRecyclerHelper;
    private IRequestCallback<String> mSlideCallback;
    private SlideShowHolder mSlideShowHolder = null;
    private SlideShowListParser mSlideShowListParser;

    public static RecommendFragment create() {
        return new RecommendFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
        mSlideShowListParser = new SlideShowListParser();
        mRecommendListParser = new RecommendListParser();
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mRecommendRecyclerHelper = new RecommendRecyclerHelper(this);
        mRecommendRecyclerHelper.setHasFixedSize(false);
        mRecommendRecyclerHelper.setCanLoad(false);
        mRecommendRecyclerHelper.setListParser(new RecommendListParser());
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
            public void onSuccess(String requestUrl, String result) {
                ListResult<SlideShow> listResult = mSlideShowListParser.parse(result);
                SlideShowRecommend slideShowRecommend;

                if (listResult != null && listResult.getList() != null) {
                    slideShowRecommend = new SlideShowRecommend();
                    slideShowRecommend.setSlideShowList(listResult.getList());
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
            public void onSuccess(String requestUrl, String resule) {
                ListResult<Recommend> listResult = mRecommendListParser.parse(resule);

                if (listResult != null && listResult.getList() != null) {
                    mRecommendList.addAll(listResult.getList());
                    mRecommendRecyclerHelper.from(mRecommendList);
                } else {
                    mRecommendRecyclerHelper.showEmptyFailureIdle();
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
    }
}