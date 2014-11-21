package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/19
 */
public class Setting {
    private boolean mAutoLoadImage = false;
    private String mDefaultTextFont = null;
    private int mDefaultTextSize = 14;
    private int mMaxQuoteCount = 3;

    public String getDefaultTextFont() {
        return mDefaultTextFont;
    }

    public int getDefaultTextSize() {
        return mDefaultTextSize;
    }

    public int getMaxQuoteCount() {
        return mMaxQuoteCount;
    }

    public boolean isAutoLoadImage() {
        return mAutoLoadImage;
    }

    public void setAutoLoadImage(boolean autoLoadImage) {
        mAutoLoadImage = autoLoadImage;
    }

    public void setDefaultTextFont(String defaultTextFont) {
        mDefaultTextFont = defaultTextFont;
    }

    public void setDefaultTextSize(int defaultTextSize) {
        mDefaultTextSize = defaultTextSize;
    }

    public void setMaxQuoteCount(int maxQuoteCount) {
        mMaxQuoteCount = maxQuoteCount;
    }
}
