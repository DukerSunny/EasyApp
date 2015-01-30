package tv.douyu.misc.danmaku;

import tv.douyu.misc.danmaku.bean.DanmakuSendResponseBean;

/**
 * Created by wm on 2014/11/12.
 */
public interface DanmakuSendResponseCallback {
    public void onSendResponseReceived(DanmakuSendResponseBean danmakuBean);
}
