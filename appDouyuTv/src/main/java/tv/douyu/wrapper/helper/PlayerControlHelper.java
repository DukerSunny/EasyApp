package tv.douyu.wrapper.helper;

import android.os.Handler;
import android.view.View;

import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.widgets.circluarprogresses.CircularProgressView;

import tv.douyu.R;
import tv.douyu.model.bean.Setting;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public abstract class PlayerControlHelper {
    private boolean mEnabled = false;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEnabled) {
                switch (v.getId()) {
                    case R.id.player_control:
                        onPlayerControlClick();
                        break;
                }
            }
        }
    };
    private Handler mHandler = new Handler();
    private Runnable mHidePlayerControlRunnable = new Runnable() {
        @Override
        public void run() {
            hide(true);
        }
    };
    private PlayerControlBottomHelper mPlayerControlBottomHelper;
    private PlayerControlDefinitionHelper mPlayerControlDefinitionHelper;
    private PlayerControlMiddleHelper mPlayerControlMiddleHelper;
    private PlayerControlSettingHelper mPlayerControlSettingHelper;
    private PlayerControlTopHelper mPlayerControlTopHelper;
    private boolean mShowing = false;
    private View player_control;
    private CircularProgressView player_progress;

    public PlayerControlHelper(IFramework framework, Setting setting) {
        player_control = framework.findViewById(R.id.player_control);
        player_progress = (CircularProgressView) framework.findViewById(R.id.player_progress);

        mPlayerControlTopHelper = new PlayerControlTopHelperImpl(player_control);
        mPlayerControlMiddleHelper = new PlayerControlMiddleHelperImpl(framework, player_control);
        mPlayerControlBottomHelper = new PlayerControlBottomHelperImpl(player_control);
        mPlayerControlSettingHelper = new PlayerControlSettingHelperImpl(framework, setting);
        mPlayerControlDefinitionHelper = new PlayerControlDefinitionHelperImpl(player_control);

        player_control.setOnClickListener(mOnClickListener);
    }

    public void clearDanmakuInput() {
        mPlayerControlBottomHelper.clearDanmakuInput();
    }

    public void generateHotWords(int cateId) {
        mPlayerControlMiddleHelper.generateHotWords(cateId);
    }

    public void generateLive(int cateId) {
        mPlayerControlMiddleHelper.generateLive(cateId);
    }

    public String getInputText() {
        return mPlayerControlBottomHelper.getInputText();
    }

    public void hidDefinition(boolean animate) {
        mPlayerControlDefinitionHelper.hide(animate);
    }

    public void hide(boolean animate) {
        mShowing = false;
        stopHideCount();
        mPlayerControlTopHelper.hide(animate);
        mPlayerControlMiddleHelper.hide(animate);
        mPlayerControlBottomHelper.hide(animate);
        mPlayerControlDefinitionHelper.hide(animate);
        mPlayerControlSettingHelper.hide(animate);
    }

    public void hideProgress() {
        player_progress.setProgress(0f);
        player_progress.setVisibility(View.GONE);
    }

    public void hideSetting(boolean animate) {
        mPlayerControlSettingHelper.hide(animate);
    }

    public boolean isPlayerDefinitionShowing() {
        return mPlayerControlDefinitionHelper.isShowing();
    }

    public boolean isPlayerSettingShowing() {
        return mPlayerControlSettingHelper.isShowing();
    }

    public boolean isShowing() {
        return mShowing;
    }

    protected abstract void onPlayerBackClick();

    protected abstract void onPlayerBrightnessChange(int value);

    private void onPlayerControlClick() {
        restartHideCount();
        if (!mPlayerControlBottomHelper.isLocked()) {
            toggle();
        } else {
            mPlayerControlBottomHelper.toggleLockShow();
        }
    }

    protected abstract void onPlayerDanmakuDensityChange(int index);

    protected abstract void onPlayerDanmakuOffClick();

    protected abstract void onPlayerDanmakuOnClick();

    protected abstract void onPlayerDanmakuOpacityChange(int value);

    protected abstract void onPlayerDanmakuSendClick();

    protected abstract void onPlayerDanmakuSizeChange(int index);

    protected abstract void onPlayerDefinitionChange(String cdn, boolean useHD);

    protected abstract void onPlayerHotWordClick(String hotWord);

    protected abstract void onPlayerLiveRoomClick(int roomId);

    protected abstract void onPlayerPauseClick();

    protected abstract void onPlayerPlayClick();

    protected abstract void onPlayerScreenRatioChange(int index);

    protected abstract void onPlayerYuWanSendClick();

    public void pause() {
        mPlayerControlBottomHelper.showPause(true);
    }

    public void play() {
        mPlayerControlBottomHelper.showPlay(true);
    }

    private void restartHideCount() {
        stopHideCount();
        startHideCount();
    }

    public void setDefinition(String cdn, boolean useHD) {
        mPlayerControlTopHelper.setUseHD(useHD);
        mPlayerControlDefinitionHelper.setDefinition(cdn, useHD);
    }

    public void setHasSD(boolean hasSD) {
        mPlayerControlDefinitionHelper.setHasSD(hasSD);
    }

    public void setTitle(String title) {
        mPlayerControlTopHelper.setTitle(title);
    }

    public void setTouchEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void show(boolean animate) {
        mShowing = true;
        restartHideCount();
        mPlayerControlTopHelper.show(animate);
        mPlayerControlMiddleHelper.show(animate);
        mPlayerControlBottomHelper.show(animate);
    }

    public void showProgress() {
        player_progress.setVisibility(View.VISIBLE);
        player_progress.setProgress(-1f);
    }

    public void showSetting(boolean animate) {
        mPlayerControlSettingHelper.show(animate);
    }

    public void showYuWanSend() {
        mPlayerControlMiddleHelper.showYuWanSend();
    }

    public void showYuWanSending() {
        mPlayerControlMiddleHelper.showYuWanSending();
    }

    private void startHideCount() {
        mHandler.postDelayed(mHidePlayerControlRunnable, 6000l);
    }

    private void stopHideCount() {
        mHandler.removeCallbacks(mHidePlayerControlRunnable);
    }

    public void toggle() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }

    private class PlayerControlBottomHelperImpl extends PlayerControlBottomHelper {
        public PlayerControlBottomHelperImpl(View rootView) {
            super(rootView);
        }

        @Override
        protected void onDanmakuClearFocus() {
            restartHideCount();
        }

        @Override
        protected void onDanmakuHotWordsClick() {
            stopHideCount();
            mPlayerControlTopHelper.hide(true);
            mPlayerControlBottomHelper.hide(true);
            mPlayerControlMiddleHelper.toggleHotWords();
            if (!mPlayerControlMiddleHelper.isHotWordsShowing()) {
                startHideCount();
            }
        }

        @Override
        protected void onDanmakuOffClick() {
            onPlayerDanmakuOffClick();
            restartHideCount();
        }

        @Override
        protected void onDanmakuOnClick() {
            onPlayerDanmakuOnClick();
            restartHideCount();
        }

        @Override
        protected void onDanmakuRequestFocus() {
            stopHideCount();
        }

        @Override
        protected void onDanmakuSendClick() {
            onPlayerDanmakuSendClick();
            restartHideCount();
        }

        @Override
        protected void onLockOffClick() {
            restartHideCount();
            PlayerControlHelper.this.show(true);
        }

        @Override
        protected void onLockOnClick() {
            PlayerControlHelper.this.hide(true);
        }

        @Override
        protected void onPauseClick() {
            restartHideCount();
            onPlayerPauseClick();
        }

        @Override
        protected void onPlayClick() {
            PlayerControlHelper.this.hide(true);
            onPlayerPlayClick();
        }
    }

    private class PlayerControlDefinitionHelperImpl extends PlayerControlDefinitionHelper {
        public PlayerControlDefinitionHelperImpl(View rootView) {
            super(rootView);
        }

        @Override
        protected void onDefinitionChange(String cdn, boolean useHD) {
            mPlayerControlTopHelper.setUseHD(useHD);
            PlayerControlHelper.this.hide(false);
            onPlayerDefinitionChange(cdn, useHD);
        }

        @Override
        protected void onDefinitionMaskClick() {
            startHideCount();
        }
    }

    private class PlayerControlMiddleHelperImpl extends PlayerControlMiddleHelper {
        public PlayerControlMiddleHelperImpl(IFramework framework, View rootView) {
            super(framework, rootView);
        }

        @Override
        protected void onHotWordClick(String hotWord) {
            onPlayerHotWordClick(hotWord);
        }

        @Override
        protected void onLiveRoomClick(int roomId) {
            onPlayerLiveRoomClick(roomId);
        }

        @Override
        protected void onYuWanSendClick() {
            onPlayerYuWanSendClick();
        }

        @Override
        protected void setInputText(String hotWord) {
            mPlayerControlBottomHelper.setInputText(hotWord);
        }
    }

    private class PlayerControlSettingHelperImpl extends PlayerControlSettingHelper {
        public PlayerControlSettingHelperImpl(IFramework framework, Setting setting) {
            super(framework, setting);
        }

        @Override
        protected void onBrightnessChange(int value) {
            onPlayerBrightnessChange(value);
        }

        @Override
        protected void onDanmakuDensityChange(int index) {
            onPlayerDanmakuDensityChange(index);
        }

        @Override
        protected void onDanmakuOpacityChange(int value) {
            onPlayerDanmakuOpacityChange(value);
        }

        @Override
        protected void onDanmakuSizeChange(int index) {
            onPlayerDanmakuSizeChange(index);
        }

        @Override
        protected void onMaskClick() {
            startHideCount();
        }

        @Override
        protected void onScreenRatioChange(int index) {
            onPlayerScreenRatioChange(index);
        }
    }

    private class PlayerControlTopHelperImpl extends PlayerControlTopHelper {
        public PlayerControlTopHelperImpl(View rootView) {
            super(rootView);
        }

        @Override
        protected void onBackClick() {
            restartHideCount();
            onPlayerBackClick();
        }

        @Override
        protected void onDefinitionClick() {
            stopHideCount();
            mPlayerControlDefinitionHelper.show(true);
        }

        @Override
        protected void onLiveClick() {
            stopHideCount();
            mPlayerControlTopHelper.hide(true);
            mPlayerControlBottomHelper.hide(true);
            mPlayerControlMiddleHelper.toggleLive();
            if (!mPlayerControlMiddleHelper.isLiveShowing()) {
                startHideCount();
            }
        }

        @Override
        protected void onSettingClick() {
            stopHideCount();
            showSetting(true);
        }
    }
}