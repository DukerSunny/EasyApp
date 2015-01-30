package tv.douyu.model.bean;

import tv.douyu.model.enumeration.DanmakuDensity;
import tv.douyu.model.enumeration.DanmakuLocation;
import tv.douyu.model.enumeration.DanmakuSize;
import tv.douyu.model.enumeration.SwitchDecode;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public class Setting {
    private boolean mAutoLoadMore = true;
    private DanmakuDensity mDanmakuDensity = DanmakuDensity.Middle;
    private DanmakuLocation mDanmakuLocation = DanmakuLocation.Top;
    private int mDanmakuOpacity = 255;
    private DanmakuSize mDanmakuSize = DanmakuSize.Normal;
    private boolean mPlayVideoUnderMobileNetwork = false;
    private boolean mPlayerGesture = true;
    private boolean mReceivePush = true;
    private boolean mShowEmptyCategory = false;
    private SwitchDecode mSwitchDecode = SwitchDecode.HW;

    public DanmakuDensity getDanmakuDensity() {
        return mDanmakuDensity;
    }

    public DanmakuLocation getDanmakuLocation() {
        return mDanmakuLocation;
    }

    public int getDanmakuOpacity() {
        return mDanmakuOpacity;
    }

    public DanmakuSize getDanmakuSize() {
        return mDanmakuSize;
    }

    public SwitchDecode getSwitchDecode() {
        return mSwitchDecode;
    }

    public boolean isAutoLoadMore() {
        return mAutoLoadMore;
    }

    public boolean isPlayVideoUnderMobileNetwork() {
        return mPlayVideoUnderMobileNetwork;
    }

    public boolean isPlayerGesture() {
        return mPlayerGesture;
    }

    public boolean isReceivePush() {
        return mReceivePush;
    }

    public boolean isShowEmptyCategory() {
        return mShowEmptyCategory;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        mAutoLoadMore = autoLoadMore;
    }

    public void setDanmakuDensity(DanmakuDensity danmakuDensity) {
        mDanmakuDensity = danmakuDensity;
    }

    public void setDanmakuLocation(DanmakuLocation danmakuLocation) {
        mDanmakuLocation = danmakuLocation;
    }

    public void setDanmakuOpacity(int danmakuOpacity) {
        mDanmakuOpacity = danmakuOpacity;
    }

    public void setDanmakuSize(DanmakuSize danmakuSize) {
        mDanmakuSize = danmakuSize;
    }

    public void setPlayVideoUnderMobileNetwork(boolean playVideoUnderMobileNetwork) {
        mPlayVideoUnderMobileNetwork = playVideoUnderMobileNetwork;
    }

    public void setPlayerGesture(boolean playerGesture) {
        mPlayerGesture = playerGesture;
    }

    public void setReceivePush(boolean receivePush) {
        mReceivePush = receivePush;
    }

    public void setShowEmptyCategory(boolean showEmptyCategory) {
        mShowEmptyCategory = showEmptyCategory;
    }

    public void setSwitchDecode(SwitchDecode switchDecode) {
        mSwitchDecode = switchDecode;
    }
}