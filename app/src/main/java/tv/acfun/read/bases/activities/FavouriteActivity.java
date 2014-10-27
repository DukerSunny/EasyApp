package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.ChannelHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class FavouriteActivity extends ActivityFramework {
    private View favourite_back;
    private View.OnClickListener mClickListener;
    private Helper mFavouriteListHelper;
    private LoginHelper mLoginHelper;

    public static Intent create(Context context) {
        return new Intent(context, FavouriteActivity.class);
    }

    @Override
    public void assignEvents() {
        favourite_back.setOnClickListener(mClickListener);
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.favourite_back:
                        onBackPressed();
                }
            }
        };
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {

    }

    @Override
    public void onActionBarMenuCreate() {

    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        mLoginHelper.hide();
        super.onDestroy();
    }

    @Override
    public void queryLayout() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        mFavouriteListHelper = new Helper(this, R.id.favourite_list);
        mFavouriteListHelper.setRefresh((FloatingActionButton) findViewById(R.id.favourite_refresh));
        mFavouriteListHelper.addFooterView(footer_loadmore);
        mFavouriteListHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mFavouriteListHelper.bindAdapter();

        mLoginHelper = new LoginHelper(getActivity());

        favourite_back = findViewById(R.id.favourite_back);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    public void startAction() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        if (acFunRead.isExpired()) {
            acFunRead.clearLogin();
            mLoginHelper.show(LoginHelper.Reason.Expired);
        } else {
            mFavouriteListHelper
                    .from(API.getFavourite(acFunRead.readToken(), "110,73,74,75", 20, mFavouriteListHelper.getCurrentPage()));
        }
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
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}