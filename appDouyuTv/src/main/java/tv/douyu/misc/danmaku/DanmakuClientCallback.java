package tv.douyu.misc.danmaku;

import tv.douyu.misc.danmaku.bean.DanmakuBean;
import tv.douyu.misc.danmaku.bean.LiveStatusBean;

/**
 * Created by neavo on 14-3-20.
 */

public interface DanmakuClientCallback {

    public void onError();

    public void onConnect(String verification);

    public void onDisconnect();

    public void onDanmakuReceived(DanmakuBean danmakuBean);

    public void onLiveStatusReceived(LiveStatusBean liveStatusBean);


}
