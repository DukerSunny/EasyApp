package tv.douyu.model.enumeration;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum DanmakuSize {
    Small(12),
    Normal(22),
    Large(32),
    Huge(40);

    private int mSize;

    private DanmakuSize(int size) {
        mSize = size;
    }

    public static DanmakuSize get(int index) {
        switch (index) {
            case 0:
                return Small;
            case 1:
                return Normal;
            case 2:
                return Large;
            default:
                return Huge;
        }
    }

    public static int indexOf(DanmakuSize danmakuSize) {
        switch (danmakuSize) {
            case Small:
                return 0;
            case Normal:
                return 1;
            case Large:
                return 2;
            default:
                return 3;
        }
    }

    public int getSize() {
        return mSize;
    }
}