package tv.douyu.misc.players;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
//public class IjkPlayer implements IMediaPlayer {
//    private boolean mDestroyed = false;
//    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(IMediaPlayer mp) {
//            stop();
//        }
//    };
//    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
//        @Override
//        public boolean onError(IMediaPlayer mp, int what, int extra) {
//            IjkPlayer.this.onError(MediaError.Error_Unsupported);
//            return false;
//        }
//    };
//    private IjkMediaPlayer mMediaPlayer = null;
//    private boolean mPrepared = false;
//    private ScreenRatio mScreenRatio = ScreenRatio.Ratio_Auto;
//    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
//        @Override
//        public void onPrepared(IMediaPlayer mp) {
//            resize(mScreenRatio);
//            play();
//        }
//    };
//    private int mSurfaceHeight = 0;
//    private SurfaceHolder mSurfaceHolder = null;
//    private int mSurfaceWidth = 0;
//    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            mSurfaceWidth = width;
//            mSurfaceHeight = height;
//        }
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            mSurfaceHolder = holder;
//            if (!mDestroyed) {
//                mMediaPlayer.setDisplay(mSurfaceHolder);
//            }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            if (!mDestroyed) {
//                mMediaPlayer.setDisplay(null);
//            }
//        }
//    };
//    private boolean mUseHW = false;
//
//    public IjkPlayer(boolean useHW) {
//        mUseHW = useHW;
//    }
//
//    @Override
//    public void attachSurface(SurfaceView surfaceView) {
//        surfaceView.getHolder().addCallback(mSurfaceCallback);
//    }
//
//    @Override
//    public void destroy() {
//        mDestroyed = true;
//        mPrepared = false;
//        stop();
//        mMediaPlayer.release();
//    }
//
//    @Override
//    public int getVideoHeight() {
//        if (isPrepared()) {
//            return mMediaPlayer.getVideoWidth();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public int getVideoWidth() {
//        if (isPrepared()) {
//            return mMediaPlayer.getVideoHeight();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public boolean hasSurface() {
//        return mSurfaceHolder != null;
//    }
//
//    @Override
//    public void init(Context context) {
//        if (mMediaPlayer != null && !mDestroyed) {
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//        mMediaPlayer = new IjkMediaPlayer();
//        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
//        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
//        mMediaPlayer.setOnErrorListener(mOnErrorListener);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mDestroyed = false;
//        mPrepared = false;
//        mSurfaceHolder = null;
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return isPrepared() && mMediaPlayer.isPlaying();
//    }
//
//    private boolean isPrepared() {
//        return !mDestroyed && mPrepared;
//    }
//
//    @Override
//    public void onError(MediaError mediaError) {
//    }
//
//    @Override
//    public void open(String url) {
//        if (!mDestroyed) {
//            mPrepared = false;
//            try {
//                Log.e(null, "open " + url);
//                mMediaPlayer.setDataSource(url, true);
//                //                mMediaPlayer.setDataSource(url);
//                mMediaPlayer.prepareAsync();
//            } catch (IOException e) {
//                onError(MediaError.Error_Network);
//            }
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (isPrepared() && isPlaying()) {
//            mMediaPlayer.pause();
//        }
//    }
//
//    @Override
//    public void play() {
//        if (isPrepared() && !isPlaying()) {
//            mMediaPlayer.start();
//        }
//    }
//
//    @Override
//    public void resize(ScreenRatio screenRatio) {
//        int videoWidth;
//        int videoHeight;
//
//        if (isPrepared()) {
//            videoWidth = getVideoWidth();
//            videoHeight = getVideoHeight();
//            mScreenRatio = screenRatio;
//            switch (mScreenRatio) {
//                case Ratio_16x9:
//                    break;
//                case Ratio_4x3:
//                    break;
//                case Ratio_Auto:
//                    break;
//                case Ratio_FullScreen:
//                    break;
//            }
//            mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
//            mMediaPlayer.changeVideoSize(mSurfaceWidth, mSurfaceHeight);
//        }
//    }
//
//    @Override
//    public void restoreState() {
//    }
//
//    @Override
//    public void seek(long milliseconds) {
//        if (isPrepared()) {
//            mMediaPlayer.seekTo((int) milliseconds);
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (isPrepared()) {
//            mMediaPlayer.stop();
//        }
//    }
//
//    @Override
//    public void storeStateState() {
//    }
//}
