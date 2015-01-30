package tv.douyu.misc.danmaku.task;

import tv.douyu.misc.danmaku.bean.YuwanBean;

/**
 * Created by wm on 2014/11/11.
 */

public interface SendYuWanCallback {
    public void onYuWanReceived(YuwanBean yuwanBean);
}
