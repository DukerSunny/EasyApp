package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.melnykov.fab.FloatingActionButton;

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
    private int mChannelId;
    private Helper mHelper;

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
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void queryLayout() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        mHelper = new Helper(this, R.id.channel_list);
        mHelper.setRefresh((FloatingActionButton) findViewById(R.id.channel_refresh));
        mHelper.addFooterView(footer_loadmore);
        mHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
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
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}