package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.SurfaceView;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.beans.Setting;
import air.tv.douyu.android.enums.DanmakuDensity;
import air.tv.douyu.android.enums.DanmakuSize;
import air.tv.douyu.android.enums.ScreenRatio;
import air.tv.douyu.android.helpers.PlayerControlHelper;
import air.tv.douyu.android.players.CanvasDanmakuPlayer;
import air.tv.douyu.android.players.HWMediaPlayer;
import air.tv.douyu.android.players.IDanmakuPlayer;
import air.tv.douyu.android.players.IMediaPlayer;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public class PlayerActivity extends ActivityFramework {
    private long mBackTime = 0l;
    private IDanmakuPlayer mDanmakuPlayer;
    private IMediaPlayer mMediaPlayer;
    private PlayerControlHelper mPlayerControlHelper;
    private int mRoomId;
    private Setting mSetting;
    private SurfaceView player_danmaku;
    private SurfaceView player_video;

    public static Intent create(Context context, int roomId) {
        Intent intent = new Intent(context, PlayerActivity.class);

        intent.putExtra("roomId", roomId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mRoomId = intent.getIntExtra("roomId", 0);
        mSetting = DouyuTv.getInstance().getSetting();
    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    public void enquiryViews() {
        mPlayerControlHelper = new PlayerControlHelper(this, mSetting) {
            @Override
            protected void onBrightnessChange(int progress) {
            }

            @Override
            protected void onDanmakuDensityChange(int index) {
                DanmakuDensity danmakuDensity = DanmakuDensity.get(index);

                mDanmakuPlayer.setDanmakuDensity(danmakuDensity.getDensity());
                mSetting.setDanmakuDensity(danmakuDensity);
                DouyuTv.getInstance().setSetting(mSetting);
            }

            @Override
            protected void onDanmakuOpacityChange(int value) {
                mDanmakuPlayer.setDanmakuOpacity(value);
                mSetting.setDanmakuOpacity(value - 38);
                DouyuTv.getInstance().setSetting(mSetting);
            }

            @Override
            protected void onDanmakuSizeChange(int index) {
                DanmakuSize danmakuSize = DanmakuSize.get(index);

                mDanmakuPlayer.setDanmakuSize(danmakuSize.getSize());
                mSetting.setDanmakuSize(danmakuSize);
                DouyuTv.getInstance().setSetting(mSetting);
            }

            @Override
            public void onPlayerBackClick() {
                onBackPressed();
            }

            @Override
            protected void onScreenRatioChange(int index) {
                mMediaPlayer.resize(ScreenRatio.get(index));
            }
        };
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void onBackPressed() {
        long backTime = System.currentTimeMillis();

        if (mPlayerControlHelper.isPlayerSettingShowing()) {
            mPlayerControlHelper.startSettingOut();
        } else {
            if (backTime - mBackTime <= 3000l) {
                super.onBackPressed();
            } else {
                mBackTime = backTime;
                showToast(R.string.app_exit);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.destroy();
        mDanmakuPlayer.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMediaPlayer.storeStateState();
        mMediaPlayer.detachSurface();
        mDanmakuPlayer.storeState();
        mDanmakuPlayer.detachSurface();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer.attachSurface(player_video);
        mMediaPlayer.restoreState();
        mDanmakuPlayer.attachSurface(player_danmaku);
        mDanmakuPlayer.restoreState();
    }

    @Override
    public void startAction() {
        mMediaPlayer = new HWMediaPlayer();
        mDanmakuPlayer = new CanvasDanmakuPlayer();
    }
}