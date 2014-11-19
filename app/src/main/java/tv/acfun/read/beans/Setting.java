package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/19
 */
public class Setting {
    private boolean mBlockMobilePicture = true;
    private int mMaxQuoteCount = 3;

    public int getMaxQuoteCount() {
        return mMaxQuoteCount;
    }

    public boolean isBlockMobilePicture() {
        return mBlockMobilePicture;
    }

    public void setBlockMobilePicture(boolean blockMobilePicture) {
        mBlockMobilePicture = blockMobilePicture;
    }

    public void setMaxQuoteCount(int maxQuoteCount) {
        mMaxQuoteCount = maxQuoteCount;
    }
}
