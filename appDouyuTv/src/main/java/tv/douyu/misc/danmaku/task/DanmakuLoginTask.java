package tv.douyu.misc.danmaku.task;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import tv.douyu.misc.danmaku.bean.GroupBean;
import tv.douyu.misc.danmaku.bean.RoomBean;

/**
 * Created by neavo on 14-3-20.
 */

public class DanmakuLoginTask implements Callable<Map<String, Object>> {

    private String url = null;
    private String port = null;
    private String roomID = null;
    private RoomBean roomBean = null;
    private GroupBean groupBean = null;
    private Socket danmakuSocket = null;

    @Override
    public Map<String, Object> call() throws IOException {
        danmakuSocket.connect(new InetSocketAddress(url, Integer.parseInt(port)));
        OutputStream socketOS = danmakuSocket.getOutputStream();
        InputStream socketIS = danmakuSocket.getInputStream();
//        Long timestamp = new Long(System.currentTimeMillis());

        MessageEncode messageEncode = MessageEncode.begin();

        ByteBuffer byteBuffer = messageEncode
                .addItem("type", "loginreq")
                .addItem("username", roomBean.getUseName())
                .addItem("password", roomBean.getRoomPassword())
                .addItem("roomid", roomID)
                .build();

        String gmessge = messageEncode.getMsgBody();
        Log.e("MessageEncode", "messgeSendLoginroom:" + gmessge);

        socketOS.write(byteBuffer.array());
        socketOS.flush();

        SystemClock.sleep(1000);
        MessageDecode.decode(socketIS);

        MessageEncode gEncode = MessageEncode.begin();
        byteBuffer = gEncode
                .addItem("type", "joingroup")
                .addItem("rid", groupBean.getRoomID())
                .addItem("gid", groupBean.getGroupID())
                .build();

        String gg = gEncode.getMsgBody();
        Log.e("MessageEncode", "messgeSendjoingroup:" + gg);

        socketOS.write(byteBuffer.array());
        socketOS.flush();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("DanmakuLogin", "DanmakuLoginOk");
        return result;
    }

    public DanmakuLoginTask setUrl(String url) {
        this.url = url;

        return this;
    }

    public DanmakuLoginTask setPort(String port) {
        this.port = port;

        return this;
    }

    public DanmakuLoginTask setRoomID(String roomID) {
        this.roomID = roomID;

        return this;
    }

    public DanmakuLoginTask setRoomBean(RoomBean roomBean) {
        this.roomBean = roomBean;

        return this;
    }

    public DanmakuLoginTask setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;

        return this;
    }

    public DanmakuLoginTask setDanmakuSocket(Socket danmakuSocket) {
        this.danmakuSocket = danmakuSocket;

        return this;
    }
}
