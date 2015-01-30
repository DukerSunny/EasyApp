package tv.douyu.control.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.GridItemDecoration;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.control.activity.RoomActivity;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.Room;
import tv.douyu.wrapper.holder.RoomHolder;
import tv.douyu.callback.OnSearchResultListener;
import tv.douyu.model.parser.RoomListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class SearchFragment extends FragmentFramework {
    private int mLiveStatus;
    private OnSearchResultListener mOnSearchResultListener = null;
    private String mQuery = null;
    private SearchRecyclerHelper mSearchRecyclerHelper;

    public static SearchFragment create(String query, int liveStatus) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();

        bundle.putString("query", query);
        bundle.putInt("liveStatus", liveStatus);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
        mQuery = bundle.getString("query");
        mLiveStatus = bundle.getInt("liveStatus");
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mSearchRecyclerHelper = new SearchRecyclerHelper(this);
        mSearchRecyclerHelper.setHasFixedSize(true);
        mSearchRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mSearchRecyclerHelper.setItemDecoration(new GridItemDecoration(2));
        mSearchRecyclerHelper.setAutoLoadMore(DouyuTv.getInstance().getSetting().isAutoLoadMore());
        mSearchRecyclerHelper.setListParser(new RoomListParser());
        mSearchRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mOnSearchResultListener = (OnSearchResultListener) activity;
    }

    @Override
    public void startAction() {
        try {
            mSearchRecyclerHelper.from(API
                    .getSearch(URLEncoder.encode(mQuery, "UTF-8"), mLiveStatus, 20, mSearchRecyclerHelper.getCurrentPage()));
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    private class SearchRecyclerHelper extends RecyclerFramework<Room> {
        public SearchRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Room> createHolder(View itemView, int viewType) {
            return new RoomHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_room, parent, false);
        }

        @Override
        public void onItemClick(int position, Room room) {
            start(RoomActivity.create(getContext(), room.getRoom_id()));
        }

        @Override
        protected void onPostAction(boolean success) {
            super.onPostAction(success);
            if (mOnSearchResultListener != null) {
                mOnSearchResultListener.onSearchResult(mLiveStatus, mSearchRecyclerHelper.getItemCount());
            }
        }
    }
}