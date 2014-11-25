package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.ContributionHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class ContributionActivity extends ActivityFramework {
    private Helper mContributionListHelper;
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
        setToolbarNavigation(R.drawable.image_back_inverse);
    }

    @Override
    public void enquiryViews() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        mContributionListHelper = new Helper(this, R.id.contribution_list);
        mContributionListHelper.setRefresh(findViewById(R.id.contribution_refresh));
        mContributionListHelper.addFooterView(footer_loadmore);
        mContributionListHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mContributionListHelper.bindAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        mContributionListHelper.from(API.getContribution(mUserId, 20, mContributionListHelper.getCurrentPage()));
    }

    private class Helper extends AbsListFramework<Content, ContributionHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public ContributionHolder createHolder(View convertView) {
            return new ContributionHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_contribution, null);
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
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}