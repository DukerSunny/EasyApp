package com.harreke.easyappframework.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyappframework.beans.ActionBarItem;
import com.harreke.easyappframework.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.RequestBuilder;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.Room;
import com.harreke.easyappframework.tools.DevUtil;
import com.harreke.easyappframework.tools.GsonUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/05
 *
 * dummy
 */
public class SurfaceActivity extends ActivityFramework implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, View.OnClickListener {
    private IRequestCallback<String> mCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure() {
            mProgress.setVisibility(View.GONE);
            showToast("获取房间信息失败！");
        }

        @Override
        public void onSuccess(String result) {
            Room room = GsonUtil.toBean(result, Room.class);

            if (room != null && room.getData() != null) {
                showToast("成功获得房间信息，正在打开直播流…");
                mDialog.setVisibility(View.GONE);
                openUrl(room.getData().getRtmp_url() + "/" + room.getData().getRtmp_live());
            } else {
                mProgress.setVisibility(View.GONE);
                showToast("获取房间信息失败！");
            }
        }
    };
    private View mDialog;
    private SurfaceHolder mHolder = null;
    private EditText mInput;
    private IjkMediaPlayer mPlayer;
    private View mProgress;
    private View mSend;

    public static Intent create(Context context) {
        return new Intent(context, SurfaceActivity.class);
    }

    @Override
    public void assignEvents() {
        mSend.setOnClickListener(this);
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
    public void onBackPressed() {
        if (mDialog.getVisibility() == View.VISIBLE) {
            exit(false);
        } else {
            mDialog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        String room = mInput.getText().toString().trim();

        if (room.length() > 0) {
            showToast("正在获取房间信息…");
            mProgress.setVisibility(View.VISIBLE);
            DevUtil.e("get http://api.douyutv.com/api/client/room/" + room);
            executeRequest(new RequestBuilder(RequestBuilder.METHOD_GET, "http://api.douyutv.com/api/client/room/" + room), mCallback);
        } else {
            showToast("无效的房间号！");
        }
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
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        DevUtil.e("info what=" + what + " extra=" + extra);
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        DevUtil.e("on prepared");
        mProgress.setVisibility(View.GONE);
        mPlayer.start();
    }

    private void openUrl(String url) {
        DevUtil.e("open " + url);
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = new IjkMediaPlayer();
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

        mDialog = findViewById(R.id.dialog);
        mInput = (EditText) findViewById(R.id.input);
        mSend = findViewById(R.id.send);
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