package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.bases.activities.SearchActivity;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.ChannelHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class ChannelFragment extends FragmentFramework {
    private View channel_scrolltop_button;
    private int mChannelId;
    private Helper mHelper;
    private boolean mNeedShowScrollTop;
    private ViewAlphaAnimation mScrollTopAnimation;

    public static ChannelFragment create(int channelId) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("channelId", channelId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Bundle bundle) {
        mChannelId = bundle.getInt("channelId");

        mNeedShowScrollTop = false;
    }

    private boolean isScrollTopShowing() {
        return channel_scrolltop_button.getVisibility() == View.VISIBLE;
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void onDestroyView() {
        mScrollTopAnimation.destroy();
        super.onDestroyView();
    }

    @Override
    public void queryLayout() {
        View header_channel = View.inflate(getActivity(), R.layout.header_channel, null);

        channel_scrolltop_button = findContentView(R.id.channel_scrolltop_button);

        mScrollTopAnimation = new ViewAlphaAnimation(channel_scrolltop_button);

        mHelper = new Helper(this, R.id.channel_list);
        mHelper.addHeaderView(header_channel);
        mHelper.bindAdapter();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_channel);
    }

    @Override
    public void startAction() {
        mHelper.from(API.getChannel(mChannelId, 20, mHelper.getCurrentPage()));
    }

    private class Helper extends AbsListFramework<Content, ChannelHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public ChannelHolder createHolder(View convertView) {
            return new ChannelHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_channel, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, Content content) {
            if (content != null) {
                start(ContentActivity.create(getActivity(), content.getContentId()));
            } else {
                start(SearchActivity.create(getActivity()));
            }
        }

        @Override
        public ArrayList<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                setTotalPage(listParser.getTotalPage());

                return listParser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);

            if (scrollState != SCROLL_STATE_IDLE) {
                if (isScrollTopShowing() && !mScrollTopAnimation.isAnimating()) {
                    mScrollTopAnimation.fadeOut();
                }
            } else {
                if (!isScrollTopShowing() && !mScrollTopAnimation.isAnimating() && mNeedShowScrollTop) {
                    mScrollTopAnimation.fadeIn();
                }
            }
        }

        @Override
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }

    private class ViewAlphaAnimation implements Animation.AnimationListener {
        private boolean mAnimating;
        private AlphaAnimation mFadeIn;
        private AlphaAnimation mFadeOut;
        private View mView;

        public ViewAlphaAnimation(View view) {
            mView = view;
            mAnimating = false;
            mFadeIn = new AlphaAnimation(0, 1);
            mFadeIn.setDuration(400);
            mFadeIn.setAnimationListener(this);
            mFadeOut = new AlphaAnimation(1, 0);
            mFadeOut.setDuration(400);
            mFadeOut.setAnimationListener(this);
        }

        public void destroy() {
            stop();
            mView = null;
        }

        public void fadeIn() {
            mView.clearAnimation();
            mView.startAnimation(mFadeIn);
        }

        public void fadeOut() {
            mView.clearAnimation();
            mView.startAnimation(mFadeOut);
        }

        public boolean isAnimating() {
            return mAnimating;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimating = false;
            if (mView.getAnimation() == mFadeOut) {
                mView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
            mAnimating = true;
            if (mView.getAnimation() == mFadeIn) {
                mView.setVisibility(View.VISIBLE);
            }
        }

        public void stop() {
            mView.clearAnimation();
        }
    }
}