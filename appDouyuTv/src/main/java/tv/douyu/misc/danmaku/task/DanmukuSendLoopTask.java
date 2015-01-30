package tv.douyu.misc.danmaku.task;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import tv.douyu.misc.danmaku.DanmakuSendResponseCallback;
import tv.douyu.misc.danmaku.DanmukuSendCallback;
import tv.douyu.misc.danmaku.bean.DanmakuBean;
import tv.douyu.misc.danmaku.bean.DanmakuSendResponseBean;
import tv.douyu.misc.danmaku.bean.YuwanBean;

/**
 * Created by Administrator on 2014/10/29.
 */
public class DanmukuSendLoopTask implements Callable<Map<String, Object>>{

    private Socket roomSocket = null;
    private DanmukuSendCallback danmakuSendCallback = null;
    private DanmakuSendResponseCallback sendResponseCallback = null;
    private SendYuWanCallback yuwanCallback = null;

    @Override
    public Map<String, Object> call() throws Exception {
        while (roomSocket != null && !roomSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            Object obj = MessageDecode.decode(roomSocket.getInputStream());

            if (obj instanceof DanmakuBean && danmakuSendCallback != null) {
                danmakuSendCallback.onDanmakuReceived((DanmakuBean) obj);
            }

            if (obj instanceof DanmakuSendResponseBean && sendResponseCallback != null) {
                sendResponseCallback.onSendResponseReceived((DanmakuSendResponseBean) obj);
            }

            if (obj instanceof YuwanBean && yuwanCallback != null) {
                yuwanCallback.onYuWanReceived((YuwanBean) obj);
            }


        }

        return new HashMap<>();
    }

    public DanmukuSendLoopTask setDanmakuSocket(Socket roomSocket) {
        this.roomSocket = roomSocket;

        return this;
    }

    public DanmukuSendLoopTask setDanmakuClientCallback(DanmukuSendCallback Callback) {
        this.danmakuSendCallback = Callback;

        return this;
    }

    public DanmukuSendLoopTask setYuWanCallback(SendYuWanCallback Callback) {
        this.yuwanCallback = Callback;

        return this;
    }


    public DanmukuSendLoopTask setDanmuSendResponseCallback(DanmakuSendResponseCallback Callback) {
        this.sendResponseCallback = Callback;

        return this;
    }

}
