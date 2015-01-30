package tv.douyu.misc.danmaku;


import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tv.douyu.misc.danmaku.bean.GroupBean;
import tv.douyu.misc.danmaku.bean.RoomBean;
import tv.douyu.misc.danmaku.bean.ServerBean;
import tv.douyu.misc.danmaku.task.DanmakuLoginTask;
import tv.douyu.misc.danmaku.task.DanmakuLoopTask;
import tv.douyu.misc.danmaku.task.DanmukuSendLoopTask;
import tv.douyu.misc.danmaku.task.RoomLoginTask;
import tv.douyu.misc.danmaku.task.SendYuWanCallback;
import tv.douyu.misc.danmaku.task.TickLoopTask;
import tv.douyu.misc.util.IOUtils;

/**
 * Created by neavo on 14-3-17.
 */

public class DanmakuClient {

    private final static String TAG = "DanmakuClient";
    private String url = "";
    private String port = "";
    private String roomID = "";
    private Socket roomSocket = null;
    private Socket danmakuSocket = null;
    private Future<Map<String, Object>> tickLoopFuture = null;
    private Future<Map<String, Object>> danmauLoopFuture = null;
    private DanmakuClientCallback danmakuClientCallback = null;
    private DanmakuSendResponseCallback sendResponseCallback = null;
    private SendYuWanCallback yuWanCallback = null;

    private ExecutorService threadPool;
    private DanmukuSendCallback danmukuSendCallback = null;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    threadPool = Executors.newFixedThreadPool(3);
                    roomSocket = new Socket();
                    danmakuSocket = new Socket();

                    // 登录房间服务器
                    Map<String, Object> result = new RoomLoginTask()
                            .setUrl(url)
                            .setPort(port)
                            .setRoomID(roomID)
                            .setRoomSocket(roomSocket).call();

                    //IOUtils.closeQuite(roomSocket);
                    RoomBean roomBean = (RoomBean) result.get("roomBean");
                    GroupBean groupBean = (GroupBean) result.get("groupBean");
                    ServerBean serverBean = (ServerBean) result.get("serverBean");

                    new DanmakuLoginTask()
                            .setRoomID(roomID)
                            .setUrl(serverBean.getUrl())
                            .setPort(serverBean.getPort())
                            .setRoomBean(roomBean)
                            .setGroupBean(groupBean)
                            .setDanmakuSocket(danmakuSocket).call();

                    if (danmakuClientCallback != null) {
                        danmakuClientCallback.onConnect(roomBean.getNpv());
                    }

                    threadPool.submit(new DanmukuSendLoopTask()
                                    .setDanmakuClientCallback(danmukuSendCallback)
                                    .setDanmuSendResponseCallback(sendResponseCallback)
                                    .setYuWanCallback(yuWanCallback)
                                    .setDanmakuSocket(roomSocket)
                    );

                    threadPool.submit(new TickLoopTask()
                                    .setDanmakuSocket(danmakuSocket, roomSocket)
                    );

                    threadPool.submit(new DanmakuLoopTask()
                                    .setDanmakuSocket(danmakuSocket)
                                    .setDanmakuClientCallback(danmakuClientCallback)
                    );

                } catch (IOException e) {
                    Log.e("tag", "Thread onError " + e.getMessage());
                    if (danmakuClientCallback != null) {
                        danmakuClientCallback.onError();
                    }

                    IOUtils.closeQuite(roomSocket);
                    IOUtils.closeQuite(danmakuSocket);
                }
            }
        }).start();
    }

    //发送弹幕
    public void sendDanmaku(String content) throws Exception {
        if (this.roomSocket == null || roomSocket.isOutputShutdown()) {
            throw new Exception("the Socket is invalidated!");
        } else {
            MessageEncode message = new MessageEncode()
                    .addItem("type", "chatmessage")
                    .addItem("receiver", "0")
                    .addItem("content", content)
                    .addItem("scope", "");
//            String mm = message.getMsgBody();
//            Log.e("MessageEncode", "messgeSend:" + mm);
            roomSocket.getOutputStream().write(message.build().array());
        }
    }

    //送鱼丸
    public void sendYuWan(String content) throws Exception {
        if (this.roomSocket == null || roomSocket.isOutputShutdown()) {
            throw new Exception("the Socket is invalidated!");
        } else {
            MessageEncode message = new MessageEncode()
                    .addItem("type", "donatereq")
                    .addItem("ms", content);
            roomSocket.getOutputStream().write(message.build().array());
        }
    }

    //
    public void disconnect() {
        IOUtils.closeQuite(roomSocket);
        IOUtils.closeQuite(danmakuSocket);

        if (threadPool!=null) {
            threadPool.shutdownNow();
        }

        if (danmakuClientCallback != null) {
            danmakuClientCallback.onDisconnect();
        }
    }

    public void setDanmakuSendCallback(DanmukuSendCallback callback) {
        this.danmukuSendCallback = callback;
    }

    public void setDanmakuClientCallback(DanmakuClientCallback danmakuClientCallback) {
        this.danmakuClientCallback = danmakuClientCallback;
    }

    public void setDanmuSendResponseCallback(DanmakuSendResponseCallback callback) {
        this.sendResponseCallback = callback;
    }

    public void setYuWanCallback(SendYuWanCallback callback) {
        this.yuWanCallback = callback;
    }

}
