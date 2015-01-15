package air.tv.douyu.android.enums;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum DanmakuDensity {
    Low(24),
    Middle(36),
    High(48),
    Unlimited(100);

    private int mDensity;

    private DanmakuDensity(int density) {
        mDensity = density;
    }

    public static DanmakuDensity get(int index) {
        switch (index) {
            case 0:
                return Low;
            case 1:
                return Middle;
            case 2:
                return High;
            default:
                return Unlimited;
        }
    }

    public static int indexOf(DanmakuDensity danmakuSize) {
        switch (danmakuSize) {
            case Low:
                return 0;
            case Middle:
                return 1;
            case High:
                return 2;
            default:
                return 3;
        }
    }

    public int getDensity() {
        return mDensity;
    }
}