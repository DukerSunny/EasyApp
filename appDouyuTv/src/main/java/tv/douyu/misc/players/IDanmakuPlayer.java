package tv.douyu.misc.players;

import android.view.SurfaceView;

import tv.douyu.model.bean.Danmaku;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public interface IDanmakuPlayer {
    public void add(Danmaku danmaku);

    public void attachSurface(SurfaceView surfaceView);

    public void destroy();

    public void open(String danmakuList);

    public void pause();

    public void play();

    public void remove(Danmaku danmaku);

    public void restoreState();

    public void seek(long milliseconds);

    /**
     * 设置弹幕密度
     *
     * @param density
     *         弹幕密度
     */
    public void setDanmakuDensity(int density);

    /**
     * 设置弹幕透明度
     *
     * @param opacity
     *         弹幕透明度，取值范围38—255
     */
    public void setDanmakuOpacity(int opacity);

    /**
     * 设置弹幕大小
     *
     * @param size
     *         弹幕大小
     */
    public void setDanmakuSize(int size);

    public void storeState();
}