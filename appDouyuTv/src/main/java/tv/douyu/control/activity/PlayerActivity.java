package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.requests.IRequestCallback;

import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.ui.widget.DanmakuSurfaceView;
import tv.douyu.R;
import tv.douyu.callback.OnMediaListener;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.api.API;
import tv.douyu.misc.players.MediaStatus;
import tv.douyu.misc.widget.VideoView;
import tv.douyu.model.bean.FullRoom;
import tv.douyu.model.bean.Setting;
import tv.douyu.model.bean.User;
import tv.douyu.model.enumeration.AuthorizeStatus;
import tv.douyu.model.enumeration.DanmakuDensity;
import tv.douyu.model.enumeration.DanmakuSize;
import tv.douyu.model.enumeration.ScreenRatio;
import tv.douyu.model.enumeration.SwitchDecode;
import tv.douyu.model.parser.FullRoomParser;
import tv.douyu.wrapper.helper.PlayerControlHelper;
import tv.douyu.wrapper.helper.PlayerDanmakuHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public class PlayerActivity extends ActivityFramework {
    private boolean mAuthorized = false;
    private long mBackTime = 0l;
    private String mCdn = "ws";
    private float mDefaultBrightness = 0f;
    private FullRoomParser mFullRoomParser;
    private OnMediaListener mOnMediaListener = null;
    private PlayerControlHelper mPlayerControlHelper;
    private PlayerDanmakuHelper mPlayerDanmakuHelper;
    private IRequestCallback<String> mRoomCallback;
    private int mRoomId;
    private Setting mSetting;
    private boolean mUseHD = false;
    private DanmakuSurfaceView player_danmaku;
    private VideoView player_video;

    public static Intent create(Context context, int roomId) {
        Intent intent = new Intent(context, PlayerActivity.class);

        intent.putExtra("roomId", roomId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mRoomId = intent.getIntExtra("roomId", 0);
        mSetting = DouyuTv.getInstance().getSetting();

        mFullRoomParser = new FullRoomParser();

        mDefaultBrightness = getBrightness();
        setBrightness(1f);
    }

    @Override
    public void attachCallbacks() {
        player_video.setOnMediaListener(mOnMediaListener);
    }

    @Override
    protected void createMenu() {
    }

    private void destroy() {
        destroyPlayer();
        destroyDanmaku();
    }

    private void destroyDanmaku() {
        if (mPlayerDanmakuHelper != null) {
            mPlayerDanmakuHelper.destroy();
        }
    }

    private void destroyPlayer() {
        player_video.destroy();
    }

    @Override
    public void enquiryViews() {
        player_video = (VideoView) findViewById(R.id.player_video);
        player_danmaku = (DanmakuSurfaceView) findViewById(R.id.player_danmaku);

        player_video.setZOrderOnTop(false);
        player_video.setZOrderMediaOverlay(false);
        player_danmaku.setZOrderOnTop(false);
        player_danmaku.setZOrderMediaOverlay(true);

        mPlayerControlHelper = new PlayerControlHelperImpl(this, mSetting);

        player_video.setUseHW(mSetting.getSwitchDecode() == SwitchDecode.HW);
        setDanmakuDensity(mSetting.getDanmakuDensity().getDensity());
        setDanmakuSize(mSetting.getDanmakuSize().getSize());
        setDanmakuOpacity(mSetting.getDanmakuOpacity());
    }

    @Override
    public void establishCallbacks() {
        mRoomCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showStatus(MediaStatus.Error_Server);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<FullRoom> objectResult = mFullRoomParser.parse(result);
                FullRoom fullRoom;

                if (objectResult != null) {
                    fullRoom = objectResult.getObject();
                    mAuthorized = DouyuTv.getInstance().validateAuthorize() == AuthorizeStatus.Authorized;
                    mPlayerDanmakuHelper = new PlayerDanmakuHelperImpl(player_danmaku, fullRoom);
                    mPlayerControlHelper.generateHotWords(fullRoom.getCate_id());
                    mPlayerControlHelper.generateLive(fullRoom.getCate_id());
                    mPlayerControlHelper.setTitle(fullRoom.getRoom_name());
                    player_video.openVideo(selectDefinition(fullRoom));
                } else {
                    showStatus(MediaStatus.Error_Server);
                }
            }
        };
        mOnMediaListener = new OnMediaListener() {
            @Override
            public void onHideProgress() {
                hideProgress();
            }

            @Override
            public void onMediaStatus(MediaStatus mediaStatus) {
                showStatus(mediaStatus);
            }

            @Override
            public void onPrepared() {
                mPlayerControlHelper.setTouchEnabled(true);
            }

            @Override
            public void onShowProgress() {
                showProgress();
            }
        };
    }

    private float getBrightness() {
        return getWindow().getAttributes().screenBrightness;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    private void hideProgress() {
        mPlayerControlHelper.hideProgress();
    }

    @Override
    public void onBackPressed() {
        long backTime = System.currentTimeMillis();

        if (mPlayerControlHelper.isPlayerDefinitionShowing()) {
            mPlayerControlHelper.hidDefinition(true);
        } else if (mPlayerControlHelper.isPlayerSettingShowing()) {
            mPlayerControlHelper.hideSetting(true);
        } else {
            if (backTime - mBackTime <= 3000l) {
                destroy();
                super.onBackPressed();
            } else {
                mBackTime = backTime;
                showToast(R.string.app_exit);
            }
        }
    }

    @Override
    protected void onDestroy() {
        destroy();
        setBrightness(mDefaultBrightness);
        super.onDestroy();
    }

    private String selectDefinition(FullRoom fullRoom) {
        boolean hasSD = fullRoom.getRtmp_multi_bitrate() != null;

        mPlayerControlHelper.setHasSD(hasSD);
        mPlayerControlHelper.setDefinition(mCdn, mUseHD);
        if (mUseHD || !hasSD) {
            return fullRoom.getRtmp_url() + "/" + fullRoom.getRtmp_live();
        } else {
            return fullRoom.getRtmp_url() + "/" + fullRoom.getRtmp_multi_bitrate().getMiddle();
        }
    }

    public void sendYuWan() {
        User user = DouyuTv.getInstance().getUser();

        if (user == null) {
            Log.e(null, "请先登录！");
            showToast("请先登录！");
        } else {
            if (user.getGold1() >= 100) {
                mPlayerControlHelper.showYuWanSending();
                mPlayerDanmakuHelper.sendYuWan();
            } else {
                Log.e(null, "鱼丸不足！");
                showToast("鱼丸不足！");
            }
        }
    }

    private void setBrightness(float brightness) {
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.screenBrightness = brightness;
        getWindow().setAttributes(params);
    }

    private void setDanmakuDensity(int value) {
        DanmakuGlobalConfig.DEFAULT.maximumNumsInScreen = value;
    }

    private void setDanmakuOpacity(int value) {
        DanmakuGlobalConfig.DEFAULT.setDanmakuTransparency(value / 255f);
    }

    private void setDanmakuSize(int value) {
        DanmakuGlobalConfig.DEFAULT.setScaleTextSize((float) value / (float) DanmakuSize.Normal.getSize());
    }

    private void showProgress() {
        mPlayerControlHelper.showProgress();
    }

    private void showStatus(MediaStatus mediaStatus) {
        switch (mediaStatus) {
            case Error_Server:
                showToast(R.string.media_error_server);
                break;
            case Error_Network:
            case Error_Unsupported:
                showToast(R.string.media_error_network);
                break;
            case Completion:
                showToast(R.string.media_completion);
        }
    }

    @Override
    public void startAction() {
        showProgress();
        mPlayerControlHelper.setTouchEnabled(false);
        executeRequest(API.getRoom(mRoomId, mCdn), mRoomCallback);
    }

    private class PlayerControlHelperImpl extends PlayerControlHelper {
        public PlayerControlHelperImpl(IFramework framework, Setting setting) {
            super(framework, setting);
        }

        @Override
        public void onPlayerBackClick() {
            onBackPressed();
        }

        @Override
        protected void onPlayerBrightnessChange(int value) {
            setBrightness((value + 38) / 255f);
        }

        @Override
        protected void onPlayerDanmakuDensityChange(int index) {
            DanmakuDensity danmakuDensity = DanmakuDensity.get(index);

            setDanmakuDensity(danmakuDensity.getDensity());
            mSetting.setDanmakuDensity(danmakuDensity);
            DouyuTv.getInstance().setSetting(mSetting);
        }

        @Override
        protected void onPlayerDanmakuOffClick() {
            if (mPlayerDanmakuHelper != null) {
                mPlayerDanmakuHelper.hideDanmaku();
            }
        }

        @Override
        protected void onPlayerDanmakuOnClick() {
            if (mPlayerDanmakuHelper != null) {
                mPlayerDanmakuHelper.showDanmaku();
            }
        }

        @Override
        protected void onPlayerDanmakuOpacityChange(int value) {
            setDanmakuOpacity(value);
            mSetting.setDanmakuOpacity(value + 38);
            DouyuTv.getInstance().setSetting(mSetting);
        }

        @Override
        protected void onPlayerDanmakuSendClick() {
            String text;

            if (mAuthorized) {
                text = getInputText();
                if (text.length() == 0) {
                    showToast("弹幕不能为空！");
                } else if (text.length() < 5) {
                    showToast("弹幕过短！");
                } else {
                    mPlayerDanmakuHelper.sendDanmaku(text);
                }
            } else {
                showToast(R.string.login_required);
            }
        }

        @Override
        protected void onPlayerDanmakuSizeChange(int index) {
            DanmakuSize danmakuSize = DanmakuSize.get(index);

            setDanmakuSize(danmakuSize.getSize());
            mSetting.setDanmakuSize(danmakuSize);
            DouyuTv.getInstance().setSetting(mSetting);
        }

        @Override
        protected void onPlayerDefinitionChange(String cdn, boolean useHD) {
            mCdn = cdn;
            mUseHD = useHD;
            startAction();
        }

        @Override
        protected void onPlayerHotWordClick(String hotWord) {
            mPlayerDanmakuHelper.sendDanmaku(hotWord);
        }

        @Override
        protected void onPlayerLiveRoomClick(int roomId) {
            mRoomId = roomId;
            startAction();
        }

        @Override
        protected void onPlayerPauseClick() {
            player_video.pause();
            if (mPlayerDanmakuHelper != null) {
                mPlayerDanmakuHelper.destroy();
            }
        }

        @Override
        protected void onPlayerPlayClick() {
            startAction();
        }

        @Override
        protected void onPlayerScreenRatioChange(int index) {
            player_video.changeRatio(ScreenRatio.get(index));
        }

        @Override
        protected void onPlayerYuWanSendClick() {
            sendYuWan();
        }
    }

    private class PlayerDanmakuHelperImpl extends PlayerDanmakuHelper {
        public PlayerDanmakuHelperImpl(DanmakuSurfaceView danmakuSurfaceView, FullRoom fullRoom) {
            super(danmakuSurfaceView, fullRoom);
        }

        @Override
        protected void onDanmakuSendSuccess() {
            showToast("弹幕发送成功！");
            mPlayerControlHelper.clearDanmakuInput();
        }

        @Override
        protected void onMessage(String message) {
            Log.e(null, "on message " + message);
            showToast(message);
        }

        @Override
        protected void onYuWanSendComplete() {
            mPlayerControlHelper.showYuWanSend();
        }
    }
}