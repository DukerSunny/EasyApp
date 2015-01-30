package tv.douyu.misc.danmaku.task;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import tv.douyu.misc.danmaku.DanmakuClientCallback;
import tv.douyu.misc.danmaku.bean.DanmakuBean;
import tv.douyu.misc.danmaku.bean.LiveStatusBean;

/**
 * Created by neavo on 14-3-20.
 */

public class DanmakuLoopTask implements Callable<Map<String, Object>> {

    private Socket danmakuSocket = null;
    private DanmakuClientCallback danmakuClientCallback = null;

    @Override
    public Map<String, Object> call() throws Exception {
        while (danmakuSocket != null && !danmakuSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            Object obj = MessageDecode.decode(danmakuSocket.getInputStream());

            if (obj instanceof DanmakuBean && danmakuClientCallback != null) {
                danmakuClientCallback.onDanmakuReceived((DanmakuBean) obj);
            }

            if (obj instanceof LiveStatusBean && danmakuClientCallback != null) {
                danmakuClientCallback.onLiveStatusReceived((LiveStatusBean) obj);
            }

        }

        return new HashMap<>();
    }

    public DanmakuLoopTask setDanmakuSocket(Socket danmakuSocket) {
        this.danmakuSocket = danmakuSocket;

        return this;
    }

    public DanmakuLoopTask setDanmakuClientCallback(DanmakuClientCallback danmakuClientCallback) {
        this.danmakuClientCallback = danmakuClientCallback;

        return this;
    }
}
