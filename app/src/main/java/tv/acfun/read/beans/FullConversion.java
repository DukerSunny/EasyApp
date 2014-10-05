package tv.acfun.read.beans;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversion {
    private Conversion mContent;
    private int mFloorCount;
    private List<Conversion> mQuoteList;
    private int mRepeatQuoteId;

    public Conversion getContent() {
        return mContent;
    }

    public int getFloorCount() {
        return mFloorCount;
    }

    public List<Conversion> getQuoteList() {
        return mQuoteList;
    }

    public int getRepeatQuoteId() {
        return mRepeatQuoteId;
    }

    public void setContent(Conversion content) {
        mContent = content;
    }

    public void setFloorCount(int floorCount) {
        mFloorCount = floorCount;
    }

    public void setQuoteList(List<Conversion> quoteList) {
        mQuoteList = quoteList;
    }

    public void setRepeatQuoteId(int repeatQuoteId) {
        mRepeatQuoteId = repeatQuoteId;
    }
}
