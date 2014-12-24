package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.ChannelUnspecificHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class ContributionActivity extends ActivityFramework {
    private ContributionRecyclerHelper mContributionRecyclerHelper;
    private int mUserId;
    private String mUsername;

    public static Intent create(Context context, int userId, String username) {
        Intent intent = new Intent(context, ContributionActivity.class);

        intent.putExtra("userId", userId);
        intent.putExtra("username", username);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        mUserId = intent.getIntExtra("userId", 0);
        mUsername = intent.getStringExtra("username");
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void createMenu() {
        setToolbarTitle(getString(R.string.user_contributes, mUsername));
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        mContributionRecyclerHelper = new ContributionRecyclerHelper(this);
        mContributionRecyclerHelper.setHasFixedSize(true);
        mContributionRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void onBackPressed() {
        exit(R.anim.zoom_in_enter, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_contribution);
    }

    @Override
    public void startAction() {
        mContributionRecyclerHelper.from(API.getContribution(mUserId, 20, mContributionRecyclerHelper.getCurrentPage()));
    }

    private class ContributionRecyclerHelper extends RecyclerFramework<Content> {
        public ContributionRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Content> createHolder(View itemView, int viewType) {
            return new ChannelUnspecificHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_channel_unspecific, parent, false);
        }

        @Override
        public void onItemClick(int position, Content content) {
            start(ContentActivity.create(getContext(), content.getContentId()));
        }

        @Override
        public List<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                return listParser.getItemList();
            } else {
                return null;
            }
        }
    }
}