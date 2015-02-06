package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.recyclerview.GridItemDecoration;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.Room;
import tv.douyu.model.parser.RoomListParser;
import tv.douyu.wrapper.holder.RoomHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class LiveActivity extends ActivityFramework {
    private int mCategoryId;
    private String mGameName;
    private LiveRecyclerHelper mLiveRecyclerHelper;

    public static Intent create(Context context, String gameName, int categoryId) {
        Intent intent = new Intent(context, LiveActivity.class);

        intent.putExtra("gameName", gameName);
        intent.putExtra("categoryId", categoryId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mGameName = intent.getStringExtra("gameName");
        mCategoryId = intent.getIntExtra("categoryId", 0);
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(mGameName);
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        mLiveRecyclerHelper = new LiveRecyclerHelper(this);
        mLiveRecyclerHelper.setHasFixedSize(true);
        mLiveRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mLiveRecyclerHelper.setItemDecoration(new GridItemDecoration(2));
        mLiveRecyclerHelper.setAutoLoadMore(DouyuTv.getInstance().getSetting().isAutoLoadMore());
        mLiveRecyclerHelper.setListParser(new RoomListParser());
        mLiveRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public void startAction() {
        mLiveRecyclerHelper.from(API.getLive(mCategoryId, 20, mLiveRecyclerHelper.getCurrentPage()));
    }

    private class LiveRecyclerHelper extends RecyclerFramework<Room> {
        public LiveRecyclerHelper(IFramework framework) {
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
    }
}
