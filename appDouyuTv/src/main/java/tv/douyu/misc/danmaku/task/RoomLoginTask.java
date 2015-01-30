package tv.douyu.misc.danmaku.task;

import android.util.Log;

import com.harreke.easyapp.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.User;
import tv.douyu.misc.danmaku.bean.ErrorBean;
import tv.douyu.misc.danmaku.bean.GroupBean;
import tv.douyu.misc.danmaku.bean.RoomBean;
import tv.douyu.misc.danmaku.bean.ServerBean;
import tv.douyu.misc.danmaku.bean.ServerGroupBean;
import tv.douyu.misc.util.CMessage;

/**
 * Created by neavo on 14-3-20.
 */

public class RoomLoginTask implements Callable<Map<String, Object>> {

    private String url = "";
    private String port = "";
    private String roomID = "";
    private Socket roomSocket = null;

    private RoomBean roomBean = null;
    private GroupBean groupBean = null;
    private ErrorBean errorBean = null;
    private ServerBean serverBean = null;
    private ServerGroupBean msgServerBean = null;

    private HashMap<String, Object> result = new HashMap<>();

    @Override
    public Map<String, Object> call() throws IOException {
        roomSocket.connect(new InetSocketAddress(url, Integer.parseInt(port)));
        OutputStream socketOS = roomSocket.getOutputStream();
        InputStream socketIS = roomSocket.getInputStream();
        roomSocket.setKeepAlive(true);
//        Long timestamp = new Long(System.currentTimeMillis());
//        .addItem("a", timestamp.toString())
//                .addItem("b", StringUtil.toMD5(StringUtil.toMD5(timestamp.toString())+ "jJ+RvGHRJcuY_OJD81yY3I5pLjY0hA"))
        User user = DouyuTv.getInstance().getUser();
        MessageEncode messageEncode = MessageEncode.begin();
        ByteBuffer byteBuffer = messageEncode
                .addItem("type", "loginreq")
                .addItem("username", user == null ? "" : user.getUsername())
                .addItem("ct", "1")     //发送播放消息
                .addItem("password", user == null ? "" : user.getPassword())
                .addItem("roomid", roomID)
                .addItem("devid", DouyuTv.getInstance().getUUID())
                .build();

        String messge = messageEncode.getMsgBody();
        Log.e("MessageEncode", "messgeSendLogin:" + messge);

        socketOS.write(byteBuffer.array());
        socketOS.flush();

        while (!roomSocketShouldClosed() && !roomSocket.isClosed()) {
            Object obj = MessageDecode.decode(socketIS);
//            Thread.sleep(500);
            if (obj instanceof ErrorBean) {
                errorBean = (ErrorBean) obj;
            } else if (obj instanceof RoomBean) {
                roomBean = (RoomBean) obj;
            } else if (obj instanceof GroupBean) {
                groupBean = (GroupBean) obj;
            } else if (obj instanceof ServerGroupBean) {
                msgServerBean = (ServerGroupBean) obj;
            }
        }
        Log.e("RoomLoginTask","4");

        if (errorBean != null) {
            throw new IOException();
        }
        Log.e("RoomLoginTask","5");
        ArrayList<ServerBean> serverArray = msgServerBean.getServerArray();
        serverBean = serverArray.get((int) (Math.random() * (serverArray.size() - 1)));

        result.put("roomBean", roomBean);
        result.put("groupBean", groupBean);
        result.put("serverBean", serverBean);

        //type@=vct/vp@=%@/devid=%@/
        //加入移动端登录验证
        MessageEncode loginEncode = MessageEncode.begin();
        ByteBuffer loginBuffer = loginEncode
                .addItem("type", "vct")
                .addItem("vp", StringUtil.toMD5(StringUtil.toMD5(roomBean.getKeyName()) +
                        StringUtil.toMD5(CMessage.getInstance().getEncryption(DouyuTv.getInstance()))))
                        .build();

        String gmessge = loginEncode.getMsgBody();
        Log.e("MessageEncode", "messgeSendLogincheck:" + gmessge);

        socketOS.write(loginBuffer.array());
        socketOS.flush();

        return result;
    }

    private int toInt(byte[] binary) {
        return ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get();
    }

    public RoomLoginTask setUrl(String url) {
        this.url = url;

        return this;
    }

    public RoomLoginTask setPort(String port) {
        this.port = port;

        return this;
    }

    public RoomLoginTask setRoomID(String roomID) {
        this.roomID = roomID;

        return this;
    }

    public RoomLoginTask setRoomSocket(Socket roomSocket) {
        this.roomSocket = roomSocket;

        return this;
    }

    private boolean roomSocketShouldClosed() {
        return errorBean != null || (roomBean != null && msgServerBean != null && groupBean != null);
    }

}