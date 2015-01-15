package air.tv.douyu.android.players;

import android.content.Context;
import android.view.SurfaceView;

import air.tv.douyu.android.enums.ScreenRatio;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public interface IMediaPlayer {
    public void attachSurface(SurfaceView surfaceView);

    public void destroy();

    public void detachSurface();

    public void init(Context context);

    public void onError(MediaError mediaError);

    public void open(String... urls);

    public void open(MediaDescriptor... mediaDescriptors);

    public void pause();

    public void play();

    public void resize(ScreenRatio screenRatio);

    public void restoreState();

    public void seek(long milliseconds);

    public void storeStateState();
}