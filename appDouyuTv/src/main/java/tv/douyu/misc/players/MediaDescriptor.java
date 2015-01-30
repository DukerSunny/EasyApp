package tv.douyu.misc.players;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public class MediaDescriptor {
    private long mDuration;
    private String mUrl;

    public MediaDescriptor(long duration, String url) {

        mDuration = duration;
        mUrl = url;
    }

    public long getDuration() {
        return mDuration;
    }

    public String getUrl() {
        return mUrl;
    }
}
