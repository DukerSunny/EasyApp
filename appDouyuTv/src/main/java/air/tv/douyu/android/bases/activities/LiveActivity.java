package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.apis.API;
import air.tv.douyu.android.beans.Room;
import air.tv.douyu.android.holders.RoomHolder;
import air.tv.douyu.android.parsers.RoomListParser;

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
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(mGameName);
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        mLiveRecyclerHelper = new LiveRecyclerHelper(this);
        mLiveRecyclerHelper.setHasFixedSize(true);
        mLiveRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
