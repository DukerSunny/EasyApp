package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.tools.DevUtil;

import java.io.IOException;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/05
 *
 * dummy
 */
public class SurfaceActivity extends ActivityFramework implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
    private SurfaceHolder mHolder = null;
    private MediaPlayer mPlayer;
    private View mProgress;

    public static Intent create(Context context) {
        return new Intent(context, SurfaceActivity.class);
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
        super.onDestroy();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        DevUtil.e("info what=" + what + " extra=" + extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        DevUtil.e("on prepared");
        mProgress.setVisibility(View.GONE);
        mPlayer.start();
    }

    private void openUrl(String url) {
        DevUtil.e("open " + url);
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setDisplay(mHolder);
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            showToast("网络错误，打开直播流失败！");
        }
    }

    @Override
    public void queryLayout() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        mHolder = surfaceView.getHolder();

        mProgress = findViewById(R.id.progress);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_surface);
    }

    @Override
    public void startAction() {
        DevUtil.e("cpu=" + Build.CPU_ABI + " / " + Build.CPU_ABI2);
    }
}