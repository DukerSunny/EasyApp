package air.tv.douyu.android.enums;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum DanmakuLocation {
    Top(R.string.setting_live_danmaku_location_top),
    Bottom(R.string.setting_live_danmaku_location_bottom),
    FullScreen(R.string.setting_live_danmaku_location_fullscreen);

    private int mTextId;

    private DanmakuLocation(int textId) {
        mTextId = textId;
    }

    public static DanmakuLocation get(int index) {
        switch (index) {
            case 0:
                return Top;
            case 1:
                return Bottom;
            default:
                return FullScreen;
        }
    }

    public static int indexOf(DanmakuLocation danmakuLocation) {
        switch (danmakuLocation) {
            case Top:
                return 0;
            case Bottom:
                return 1;
            default:
                return 2;
        }
    }

    public int getTextId() {
        return mTextId;
    }
}