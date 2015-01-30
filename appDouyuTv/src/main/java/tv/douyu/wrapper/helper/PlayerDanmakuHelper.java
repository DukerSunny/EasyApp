package tv.douyu.wrapper.helper;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.harreke.easyapp.frameworks.application.ApplicationFramework;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.FullRoom;
import tv.douyu.model.bean.Server;
import tv.douyu.model.bean.User;
import tv.douyu.misc.danmaku.DanmakuClient;
import tv.douyu.misc.danmaku.DanmakuClientCallback;
import tv.douyu.misc.danmaku.DanmakuSendResponseCallback;
import tv.douyu.misc.danmaku.DanmukuSendCallback;
import tv.douyu.misc.danmaku.bean.DanmakuBean;
import tv.douyu.misc.danmaku.bean.DanmakuSendResponseBean;
import tv.douyu.misc.danmaku.bean.LiveStatusBean;
import tv.douyu.misc.danmaku.bean.YuwanBean;
import tv.douyu.misc.danmaku.task.SendYuWanCallback;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.DanmakuFactory;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.AcFunDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuSurfaceView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/20
 */
public abstract class PlayerDanmakuHelper {
    private int CDTime = 0;
    private Handler mMessageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    onMessage("无法连接弹幕服务器！");
                    break;
                case 7:
                    switch (msg.arg1) {
                        case 0:
                            onMessage("您的发言被管理员屏蔽");
                            break;
                        case 2:
                            onMessage("您被禁止发言");
                            break;
                        case 208:
                            onMessage("用户未找到");
                            break;
                        case 288:
                            onMessage("被管理员限制发言");
                            break;
                        case 289:
                            onMessage("聊天内容重复");
                            break;
                        case 290:
                            onMessage(String.format("您的发言CD还有%d秒", CDTime));
                    }
                    break;
                case 11:
                    onMessage("发送消息出错，您被禁止发言");
            }
            return false;
        }
    });
    private DanmakuSendResponseCallback mDanmakuSendResponseCallback = new DanmakuSendResponseCallback() {
        @Override
        public void onSendResponseReceived(DanmakuSendResponseBean danmakuBean) {
            onDanmakuSendSuccess();
            CDTime = Integer.parseInt(danmakuBean.getCdtime());
            if (CDTime > 0) {
                startCDTimer();
            }
        }
    };
    private Timer CDTimer = null;
    private DanmakuClient mDanmakuClient = null;
    private long mDanmakuCurrMillisecond = -1l;
    private DrawHandler.Callback mDrawCallback = new DrawHandler.Callback() {
        @Override
        public void prepared() {
            mDanmakuSurfaceView.start();
        }

        @Override
        public void updateTimer(DanmakuTimer timer) {
            mDanmakuCurrMillisecond = timer.currMillisecond;
        }
    };
    private DanmukuSendCallback mDanmakuSendCallback = new DanmukuSendCallback() {
        @Override
        public void onDanmakuReceived(DanmakuBean danmakuBean) {
            Message message = mMessageHandler.obtainMessage();

            message.what = 7;
            message.arg1 = Integer.parseInt(danmakuBean.getResCode());
            message.sendToTarget();
        }
    };
    private DanmakuSurfaceView mDanmakuSurfaceView = null;
    private boolean mPhoneVerification = false;
    private DanmakuClientCallback mDanmakuClientCallback = new DanmakuClientCallback() {
        @Override
        public void onConnect(String verification) {
            mPhoneVerification = "1".equals(verification);
        }

        @Override
        public void onDanmakuReceived(DanmakuBean danmakuBean) {
            danmakuBean.setContent(danmakuBean.getContent().replaceAll("\\[emot:\\w+\\]", ""));
            if (danmakuBean.getContent().length() > 0) {
                addDanmaku(danmakuBean.getContent());
            }
        }

        @Override
        public void onDisconnect() {
        }

        @Override
        public void onError() {
            Message message = mMessageHandler.obtainMessage();

            message.arg1 = 3;
            message.sendToTarget();

            destroy();
        }

        @Override
        public void onLiveStatusReceived(LiveStatusBean liveStatusBean) {
            if (mRoomId.equals(liveStatusBean.getRoomID()) && "0".equals(liveStatusBean.getLiveStatus())) {
                destroy();
            }
        }
    };
    private String mRoomId;
    private SendYuWanCallback mSendYuWanCallback = new SendYuWanCallback() {
        @Override
        public void onYuWanReceived(final YuwanBean yuwanBean) {
            mMessageHandler.post(new Runnable() {
                @Override
                public void run() {
                    DouyuTv douyuTv;
                    User user;

                    switch (yuwanBean.getYuwan_r()) {
                        case "0":
                            douyuTv = DouyuTv.getInstance();
                            user = douyuTv.getUser();
                            user.setGold1(Integer.valueOf(yuwanBean.getYuwan_sb()));
                            douyuTv.setUser(user);
                            onMessage("鱼丸赠送成功!");
                            break;
                        case "283":
                            onMessage("鱼丸不足!");
                            break;
                        case "284":
                            onMessage("服务器内部异常!");
                            break;
                        case "285":
                            onMessage("操作超时，服务器繁忙!");
                            break;
                    }
                    onYuWanSendComplete();
                }
            });
        }
    };

    public PlayerDanmakuHelper(DanmakuSurfaceView danmakuSurfaceView, FullRoom fullRoom) {
        List<Server> serverList = fullRoom.getServers();
        Server server;
        int roomId = fullRoom.getRoom_id();

        if (serverList != null && serverList.size() > 0) {
            server = serverList.get((int) (Math.random() * (serverList.size() - 1)));
            mDanmakuSurfaceView = danmakuSurfaceView;
            mDanmakuSurfaceView.setCallback(mDrawCallback);
            mDanmakuSurfaceView.prepare(makeDanmakuParser());
            mDanmakuSurfaceView.enableDanmakuDrawingCache(true);
            mRoomId = String.valueOf(roomId);
            mDanmakuClient = new DanmakuClient();
            mDanmakuClient.setRoomID(mRoomId);
            mDanmakuClient.setUrl(server.getIp());
            mDanmakuClient.setPort(String.valueOf(server.getPort()));
            mDanmakuClient.setDanmakuClientCallback(mDanmakuClientCallback);
            mDanmakuClient.setDanmakuSendCallback(mDanmakuSendCallback);
            mDanmakuClient.setDanmuSendResponseCallback(mDanmakuSendResponseCallback);
            mDanmakuClient.setYuWanCallback(mSendYuWanCallback);
            mDanmakuClient.connect();
        }
    }

    private void addDanmaku(String msg) {
        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = msg.replaceAll("\\[([^\\]]+)\\]", "");
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.BLACK;
        danmaku.time = mDanmakuCurrMillisecond + 50;
        danmaku.textSize = ApplicationFramework.Density * 22;

        mDanmakuSurfaceView.addDanmaku(danmaku);
    }

    public void destroy() {
        if (mDanmakuSurfaceView != null) {
            mDanmakuSurfaceView.release();
            mDanmakuSurfaceView = null;
        }
        if (mDanmakuClient != null) {
            mDanmakuClient.disconnect();
            mDanmakuClient = null;
        }
    }

    public void hideDanmaku() {
        mDanmakuSurfaceView.hideAndPauseDrawTask();
    }

    private BaseDanmakuParser makeDanmakuParser() {
        BaseDanmakuParser parser = new AcFunDanmakuParser();
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN);
        IDataSource dataSource = loader.getDataSource();

        try {
            loader.load("[{\"c\":\"0,16777215,1,25,297k3721070627,1387516268\",\"m\":\"\"}]");
        } catch (IllegalDataException ignored) {
        }
        parser.load(dataSource);

        return parser;
    }

    protected abstract void onDanmakuSendSuccess();

    protected abstract void onMessage(String message);

    protected abstract void onYuWanSendComplete();

    public void sendDanmaku(String text) {
        Log.e(null, "send danmaku " + text);
        if (mPhoneVerification) {
            onMessage("您还没有验证手机号，验证后才可以发弹幕！");
            return;
        }
        if (CDTime > 0) {
            onMessage(String.format("您的发言CD还有%d秒", CDTime));
            return;
        }
        try {
            Log.e(null, "2 send danmaku " + text);
            mDanmakuClient.sendDanmaku(text);
        } catch (Exception e) {
            onMessage("发送弹幕失败！");
        }
    }

    public void sendYuWan() {
        try {
            mDanmakuClient.sendYuWan("100");
        } catch (Exception e) {
            onYuWanSendComplete();
            onMessage("赠送鱼丸失败！");
        }
    }

    public void showDanmaku() {
        mDanmakuSurfaceView.showAndResumeDrawTask(System.currentTimeMillis());
    }

    private void startCDTimer() {
        if (CDTimer == null) {
            CDTimer = new Timer();
            CDTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    CDTime--;
                    if (CDTime <= 0) {
                        CDTime = 0;
                        CDTimer.cancel();
                        CDTimer = null;
                    }
                }
            }, 1000, 1000);
        }
    }
}
