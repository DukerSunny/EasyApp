package tv.acfun.read.beans;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class FullConversion {
    private Conversion mConversion;
    private int mFloorCount;
    private List<Conversion> mQuoteList;

    public Conversion getConversion() {
        return mConversion;
    }

    public int getFloorCount() {
        return mFloorCount;
    }

    public List<Conversion> getQuoteList() {
        return mQuoteList;
    }

    @Override
    public int hashCode() {
        return mConversion.hashCode();
    }

    public void setConversion(Conversion conversion) {
        mConversion = conversion;
    }

    public void setFloorCount(int floorCount) {
        mFloorCount = floorCount;
    }

    public void setQuoteList(List<Conversion> quoteList) {
        mQuoteList = quoteList;
    }
}