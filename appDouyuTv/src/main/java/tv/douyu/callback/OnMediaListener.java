package tv.douyu.callback;

import tv.douyu.misc.players.MediaStatus;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/20
 */
public interface OnMediaListener {
    public void onHideProgress();

    public void onMediaStatus(MediaStatus mediaStatus);

    public void onPrepared();

    public void onShowProgress();
}
