package tv.douyu.misc.danmaku.task;

import android.net.TrafficStats;
import android.os.SystemClock;

import com.harreke.easyapp.utils.StringUtil;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.util.CMessage;

/**
 * Created by neavo on 14-3-20.
 */

public class TickLoopTask implements Callable<Map<String, Object>> {

    private static int count = 0;
    private Socket danmakuSocket = null;
    private boolean isSendByteCount = false;
    private long mCurBytes = 0;
    private long mPreBytes = 0;
    private Socket roomSocket = null;

    @Override
    public Map<String, Object> call() throws Exception {
        mPreBytes = TrafficStats.getUidRxBytes(android.os.Process.myUid());
        while (danmakuSocket != null && !danmakuSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            SystemClock.sleep(10000);//心跳10s发一次
            count++;
            //            Log.e("1111", "count:"+count);
            if (count == 5) {
                /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
                long bytes = TrafficStats.getUidRxBytes(android.os.Process.myUid());
                //                    Log.e(TAG, "-----------recv data bytes ：" + (bytes - mPreBytes));
                /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
                //                    TrafficStats.getUidTxBytes(android.os.Process.myUid());
                mCurBytes = new Long(bytes - mPreBytes);
                mPreBytes = bytes;
                count = 0;
                //                Log.e("1111", "mCurBytes:"+mCurBytes);
            }

            String tick = String.valueOf(System.currentTimeMillis() / 1000);
            ByteBuffer byteBuffer = MessageEncode.begin().addItem("type", "keeplive").addItem("tick", tick)
                    .addItem("vbw", String.valueOf(mCurBytes))
                    .addItem("k", StringUtil.toMD5(tick + CMessage.getInstance().getEncryption(DouyuTv.getInstance()) +
                            String.valueOf(mCurBytes))).build();

            OutputStream socketOS = danmakuSocket.getOutputStream();
            socketOS.write(byteBuffer.array());
            socketOS.flush();

            roomSocket.getOutputStream().write(byteBuffer.array());
            mCurBytes = 0;

        }

        return new HashMap<>();
    }

    public TickLoopTask setDanmakuSocket(Socket danmakuSocket, Socket roomSocket) {
        this.danmakuSocket = danmakuSocket;
        this.roomSocket = roomSocket;
        return this;
    }
}
