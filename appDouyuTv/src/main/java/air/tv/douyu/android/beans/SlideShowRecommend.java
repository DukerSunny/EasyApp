package air.tv.douyu.android.beans;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowRecommend extends Recommend {
    private final static int ID = "SlideShow".hashCode();
    private List<SlideShow> mSlideShowList;

    public List<SlideShow> getSlideShowList() {
        return mSlideShowList;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    public void setSlideShowList(List<SlideShow> slideShowList) {
        mSlideShowList = slideShowList;
    }
}
