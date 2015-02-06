package tv.douyu.wrapper.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.misc.util.HotWordsUtil;
import tv.douyu.model.bean.Room;
import tv.douyu.model.parser.RoomListParser;
import tv.douyu.wrapper.holder.HotWordHolder;
import tv.douyu.wrapper.holder.LiveHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlMiddleHelper {
    private int mCateId;
    private ToggleViewValueAnimator mHotWordsAnimator;
    private HotWordsRecyclerHelper mHotWordsRecyclerHelper;
    private ToggleViewValueAnimator mLiveAnimator;
    private LiveRecyclerHelper mLiveRecyclerHelper;
    private PlayerControlMiddleYuWanHelper mYuWanHelper;
    @InjectView(R.id.player_control_middle)
    View player_control_middle;
    @InjectView(R.id.player_control_middle_hotwords)
    View player_control_middle_hotwords;
    @InjectView(R.id.player_control_middle_live)
    View player_control_middle_live;

    public PlayerControlMiddleHelper(IFramework framework, View rootView) {
        ButterKnife.inject(this, rootView);
        mHotWordsAnimator = ToggleViewValueAnimator.animate(player_control_middle_hotwords);
        mLiveAnimator = ToggleViewValueAnimator.animate(player_control_middle_live);

        mHotWordsRecyclerHelper = new HotWordsRecyclerHelper(framework);
        mHotWordsRecyclerHelper.attachAdapter();
        mLiveRecyclerHelper = new LiveRecyclerHelper(framework);
        mLiveRecyclerHelper.setListParser(new RoomListParser());
        mLiveRecyclerHelper.attachAdapter();
        mYuWanHelper = new PlayerControlMiddleYuWanHelper(player_control_middle) {
            @Override
            protected void onSendClick() {
                onYuWanSendClick();
            }
        };

        player_control_middle.post(new Runnable() {
            @Override
            public void run() {
                mHotWordsAnimator.alphaOff(0f).alphaOn(1f).xOff(-player_control_middle_hotwords.getMeasuredWidth()).xOn(0f)
                        .visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                mLiveAnimator.alphaOff(0f).alphaOn(1f).xOff(player_control_middle.getMeasuredWidth())
                        .xOn(player_control_middle.getMeasuredWidth() - player_control_middle_live.getMeasuredWidth())
                        .visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                hide(false);
            }
        });
    }

    public void generateHotWords(int cateId) {
        mHotWordsRecyclerHelper.clear();
        mHotWordsRecyclerHelper.from(HotWordsUtil.getHotWords(cateId));
    }

    public void generateLive(int cateId) {
        mCateId = cateId;
        mLiveRecyclerHelper.onRequestAction();
    }

    public void hide(boolean animate) {
        mHotWordsAnimator.toggleOff(animate);
        mLiveAnimator.toggleOff(animate);
        mYuWanHelper.hide(animate);
    }

    public boolean isHotWordsShowing() {
        return mHotWordsAnimator.isToggledOn();
    }

    public boolean isLiveShowing() {
        return mLiveAnimator.isToggledOn();
    }

    protected abstract void onHotWordClick(String hotWord);

    protected abstract void onLiveRoomClick(int roomId);

    protected abstract void onYuWanSendClick();

    public void show(boolean animate) {
        mYuWanHelper.show(animate);
    }

    public void showYuWanSend() {
        mYuWanHelper.showSend();
    }

    public void showYuWanSending() {
        mYuWanHelper.showSending();
    }

    public void toggleHotWords() {
        mHotWordsAnimator.toggle(true);
    }

    public void toggleLive() {
        mLiveAnimator.toggle(true);
    }

    private class HotWordsRecyclerHelper extends RecyclerFramework<String> {
        public HotWordsRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<String> createHolder(View itemView, int viewType) {
            return new HotWordHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_hotword, parent, false);
        }

        @Override
        protected int getRecyclerViewId() {
            return R.id.recycler_hotwords;
        }

        @Override
        public void onItemClick(int position, String hotWord) {
            hide(true);
            onHotWordClick(hotWord);
        }
    }

    private class LiveRecyclerHelper extends RecyclerFramework<Room> {
        public LiveRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Room> createHolder(View itemView, int viewType) {
            return new LiveHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_live, parent, false);
        }

        @Override
        protected int getRecyclerViewId() {
            return R.id.recycler_live;
        }

        @Override
        public void onItemClick(int position, Room room) {
            hide(true);
            onLiveRoomClick(room.getRoom_id());
        }

        @Override
        protected void onRequestAction() {
            clear();
            from(API.getLive(mCateId, 20, 1));
        }
    }
}