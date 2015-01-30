package tv.douyu.model.enumeration;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public enum ScreenRatio {
    Ratio_16x9,
    Ratio_4x3,
    Ratio_Auto,
    Ratio_FullScreen;

    public static ScreenRatio get(int index) {
        switch (index) {
            case 0:
                return Ratio_16x9;
            case 1:
                return Ratio_4x3;
            case 2:
                return Ratio_Auto;
            default:
                return Ratio_FullScreen;
        }
    }
}