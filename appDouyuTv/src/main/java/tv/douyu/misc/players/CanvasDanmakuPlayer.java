package tv.douyu.misc.players;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import tv.douyu.model.bean.Danmaku;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public class CanvasDanmakuPlayer implements IDanmakuPlayer {
    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
    private SurfaceHolder mSurfaceHolder = null;
    @Override
    public void add(Danmaku danmaku) {
    }

    @Override
    public void attachSurface(SurfaceView surfaceView) {
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void open(String danmakuList) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void play() {
    }

    @Override
    public void remove(Danmaku danmaku) {
    }

    @Override
    public void restoreState() {
    }

    @Override
    public void seek(long milliseconds) {

    }

    @Override
    public void setDanmakuDensity(int density) {

    }

    @Override
    public void setDanmakuOpacity(int opacity) {

    }

    @Override
    public void setDanmakuSize(int size) {

    }

    @Override
    public void storeState() {

    }
}
