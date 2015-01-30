package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.bases.IFramework;
import com.harreke.easyapp.frameworks.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.ChannelSpecificHolder;
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
    public int getLayoutId() {
        return R.layout.fragment_channel;
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
            return new ChannelSpecificHolder(convertView);
        }

        @Override
        public View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_channel_specific, parent, false);
        }

        @Override
        public void onItemClick(int position, Content content) {
            start(ContentActivity.create(getActivity(), content.getContentId()));
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