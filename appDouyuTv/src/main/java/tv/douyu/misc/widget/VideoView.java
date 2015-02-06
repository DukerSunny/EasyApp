package tv.douyu.misc.widget;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.option.AvFourCC;
import tv.douyu.callback.OnMediaListener;
import tv.douyu.misc.players.MediaStatus;
import tv.douyu.model.enumeration.ScreenRatio;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/20
 */
public class VideoView extends SurfaceView
        implements IMediaPlayer.OnErrorListener, IMediaPlayer.OnPreparedListener, SurfaceHolder.Callback,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener {
    public IjkMediaPlayer mMediaPlayer = null;
    private OnMediaListener mOnMediaListener = null;
    private boolean mPrepared = false;
    private int mSurfaceHeight = 0;
    private int mSurfaceWidth = 0;
    private boolean mUseHW = false;
    private int mVideoHeight = 0;
    private int mVideoWidth = 0;

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);
        setZOrderMediaOverlay(false);
    }

    public void changeRatio(ScreenRatio screenRatio) {
        ViewGroup.LayoutParams params;
        int width;
        int height;
        int videoWidth;
        int videoHeight;
        float scale;
        float scaleWitdh;
        float scaleHeight;
        float surfaceScale;
        float videoScale;

        //        if (isPrepared()) {
        //            videoWidth = getVideoWidth();
        //            videoHeight = getVideoHeight();
        //            scaleWitdh = mSurfaceWidth / videoWidth;
        //            scaleHeight = mSurfaceHeight / videoHeight;
        //            videoScale = videoWidth / videoHeight;
        //            surfaceScale = mSurfaceWidth / mSurfaceHeight;
        //            switch (screenRatio) {
        //                case Ratio_16x9:
        //                    if (videoScale >= 1.78f) {
        //
        //                    }
        //                    break;
        //                default:
        //                    width = mSurfaceWidth;
        //                    height = mSurfaceHeight;
        //            }
        //            params = getLayoutParams();
        //            params.width = width;
        //            params.height = height;
        //            setLayoutParams(params);
        //        }
    }

    public void destroy() {
        mSurfaceWidth = 0;
        mSurfaceHeight = 0;
        mPrepared = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public int getVideoHeight() {
        if (isPrepared()) {
            return mVideoHeight;
        } else {
            return 0;
        }
    }

    public int getVideoWidth() {
        if (isPrepared()) {
            return mVideoWidth;
        } else {
            return 0;
        }
    }

    private void hideProgress() {
        if (mOnMediaListener != null) {
            mOnMediaListener.onHideProgress();
        }
    }

    public boolean isPrepared() {
        return mMediaPlayer != null && mPrepared;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        destroy();
        onMediaStatus(MediaStatus.Completion);
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        Log.e(null, "error what=" + what + " extra=" + extra);
        //        onMediaStatus(MediaStatus.Error_Unsupported);

        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                showProgress();
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                hideProgress();

        }

        return false;
    }

    private void onMediaStatus(MediaStatus mediaStatus) {
        if (mOnMediaListener != null) {
            mOnMediaListener.onMediaStatus(mediaStatus);
        }
    }

    private void onPrepared() {
        if (mOnMediaListener != null) {
            mOnMediaListener.onPrepared();
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mPrepared = true;
        onPrepared();
        play();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        mVideoWidth = width;
        mVideoHeight = height;
        getHolder().setFixedSize(mVideoWidth, mVideoHeight);
    }

    public void openVideo(String url) {
        destroy();
        mMediaPlayer = new IjkMediaPlayer();
        mMediaPlayer.setOverlayFormat(AvFourCC.SDL_FCC_RV32);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setAvCodecOption("skip_loop_filter", "48");
        mMediaPlayer.setFrameDrop(12);
        mMediaPlayer.setMediaCodecEnabled(mUseHW);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setDisplay(getHolder());
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            onMediaStatus(MediaStatus.Error_Network);
        }
    }

    public void pause() {
        if (isPrepared()) {
            mMediaPlayer.pause();
        }
    }

    public void play() {
        if (isPrepared()) {
            mMediaPlayer.start();
        }
    }

    public void setOnMediaListener(OnMediaListener onMediaListener) {
        mOnMediaListener = onMediaListener;
    }

    public void setUseHW(boolean useHW) {
        mUseHW = useHW;
    }

    private void showProgress() {
        if (mOnMediaListener != null) {
            mOnMediaListener.onShowProgress();
        }
    }

    public void stop() {
        if (isPrepared()) {
            mMediaPlayer.stop();
            destroy();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceWidth == 0) {
            mSurfaceWidth = width;
        }
        if (mSurfaceHeight == 0) {
            mSurfaceHeight = height;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
        }
    }
}