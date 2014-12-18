package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.lists.recyclerview.RecyclerFramework;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;

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
    private ChannelRecyclerHelper mChannelRecyclerHelper;

    public static ChannelFragment create(int channelId) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("channelId", channelId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void acquireArguments(Bundle bundle) {
        mChannelId = bundle.getInt("channelId");
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mChannelRecyclerHelper = new ChannelRecyclerHelper(this);
        mChannelRecyclerHelper.setHasFixedSize(false);
        mChannelRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_channel);
    }

    @Override
    public void startAction() {
        mChannelRecyclerHelper.from(API.getChannel(mChannelId, 20, mChannelRecyclerHelper.getCurrentPage()));
    }

    private class ChannelRecyclerHelper extends RecyclerFramework<Content> {
        public ChannelRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        public RecyclerHolder<Content> createHolder(View convertView, int viewType) {
            return new ChannelHolder(convertView);
        }

        @Override
        public View createView(ViewGroup parent, int viewType) {
            return LayoutInflater.from(getActivity()).inflate(R.layout.item_channel, parent, false);
        }

        @Override
        protected void onRequestAction() {
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
                return listParser.getItemList();
            } else {
                return null;
            }
        }
    }
}