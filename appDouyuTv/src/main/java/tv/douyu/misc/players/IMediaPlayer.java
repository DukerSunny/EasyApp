package tv.douyu.misc.players;

import android.content.Context;
import android.view.SurfaceView;

import tv.douyu.model.enumeration.ScreenRatio;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public interface IMediaPlayer {
    public void attachSurface(SurfaceView surfaceView);

    public void destroy();

    public int getVideoHeight();

    public int getVideoWidth();

    public boolean hasSurface();

    public void init(Context context);

    public boolean isPlaying();

    public void onError(MediaStatus mediaStatus);

    public void open(String url);

    public void pause();

    public void play();

    public void resize(ScreenRatio screenRatio);

    public void restoreState();

    public void seek(long milliseconds);

    public void stop();

    public void storeStateState();
}