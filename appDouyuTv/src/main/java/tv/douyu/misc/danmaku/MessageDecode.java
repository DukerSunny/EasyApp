package tv.douyu.misc.danmaku;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

import tv.douyu.misc.danmaku.bean.DanmakuBean;
import tv.douyu.misc.danmaku.bean.DanmakuSendResponseBean;
import tv.douyu.misc.danmaku.bean.ErrorBean;
import tv.douyu.misc.danmaku.bean.GroupBean;
import tv.douyu.misc.danmaku.bean.LiveStatusBean;
import tv.douyu.misc.danmaku.bean.RoomBean;
import tv.douyu.misc.danmaku.bean.ServerBean;
import tv.douyu.misc.danmaku.bean.ServerGroupBean;
import tv.douyu.misc.danmaku.bean.YuwanBean;

/**
 * Created by neavo on 14-3-17.
 */

public class MessageDecode {

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static Object decode(InputStream is) throws IOException {
        return new MessageDecode().doDecode(is);
    }

    private int toInt(byte[] binary) {
        return ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get();
    }

    private HashMap<String, String> getKVMap(String[] kvArray) {
        HashMap<String, String> kvMap = new HashMap<>();

        for (String v : kvArray) {
            String[] kv = v.split("@=");

            if (kv.length > 1) {
                kvMap.put(kv[0], kv[1]);
            }
        }

        return kvMap;
    }

    private Object doDecode(InputStream is) throws IOException {
        String msgBody = getMsgBody(is);
        HashMap<String, String> kvMap = getKVMap(msgBody.split("/"));
//        Log.e("MessageDecode", "messgeD:" + msgBody);
        switch (kvMap.get("type")) {
            case "error":
                return getErrorBean(kvMap);
            case "loginres":
                return getRoomBean(kvMap);
            case "setmsggroup":
                return getGroupBean(kvMap);
            case "chatmessage":
                return getDanmakuBean(kvMap);
            case "msgrepeaterlist":
                return getServerGroupBean(kvMap);
            case "rss":
                return getLiveStatusBEan(kvMap);
            case "rdr":
                return getYuwanBean(kvMap);
            case "scl":
                return getDanmuSendResponseBean(kvMap);
        }

        return new Object();
    }


    private YuwanBean getYuwanBean(HashMap<String, String> targetMap) {
        YuwanBean yuwanBean = new YuwanBean();
        yuwanBean.setYuwan_ms(targetMap.get("ms"));
        yuwanBean.setYuwan_r(targetMap.get("r"));
        yuwanBean.setYuwan_sb(targetMap.get("sb"));
        yuwanBean.setYuwan_strength(targetMap.get("strength"));
        yuwanBean.setYuwan_ms(targetMap.get("ms"));
        return yuwanBean;
    }


    private RoomBean getRoomBean(HashMap<String, String> targetMap) {
        RoomBean roomBean = new RoomBean();
        roomBean.setUseName(targetMap.get("username"));
        roomBean.setRoomGroup(targetMap.get("roomgroup"));
        roomBean.setkeyName(targetMap.get("s"));//客户端类型验证字符串
        roomBean.setNpv(targetMap.get("npv"));//客户端类型验证字符串
//        Log.e("MESS", "----String:"+roomBean.getKeyName());
        return roomBean;
    }

    private DanmakuSendResponseBean getDanmuSendResponseBean(HashMap<String, String> targetMap) {
        DanmakuSendResponseBean bean = new DanmakuSendResponseBean();
        bean.setCdtime(targetMap.get("cd"));
        bean.setMaxlength(targetMap.get("maxl"));

        return bean;
    }

    private LiveStatusBean getLiveStatusBEan(HashMap<String, String> targetMap) {

        LiveStatusBean liveBean = new LiveStatusBean();
        liveBean.setRoomID(targetMap.get("rid"));
        liveBean.setLiveStatus(targetMap.get("ss"));

//       Log.e("MESS", "----LiveStatusBean:"+liveBean.toString());

        return liveBean;
    }

    private GroupBean getGroupBean(HashMap<String, String> targetMap) {
        GroupBean groupBean = new GroupBean();
        groupBean.setRoomID(targetMap.get("rid"));
        groupBean.setGroupID(targetMap.get("gid"));

        return groupBean;
    }

    private ErrorBean getErrorBean(HashMap<String, String> targetMap) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setCode(targetMap.get("code"));

        return errorBean;
    }

    private DanmakuBean getDanmakuBean(HashMap<String, String> targetMap) {
        DanmakuBean danmakuBean = new DanmakuBean();
        danmakuBean.setContent(targetMap.get("content"));
        danmakuBean.setNickName(targetMap.get("snick"));
        danmakuBean.setResCode(targetMap.get("rescode"));
        return danmakuBean;
    }

    private ServerGroupBean getServerGroupBean(HashMap<String, String> targetMap) {
        ServerGroupBean msgServerBean = new ServerGroupBean();
        msgServerBean.setRoomID(targetMap.get("rid"));

        String listString = targetMap.get("list")
                .replaceAll("@A", "@")
                .replaceAll("@S", "/")
                .replaceAll("@A", "@");
        listString = listString.substring(0, listString.length() - 2);

        String[] itemArray = listString.split("//");
        ArrayList<ServerBean> serverArray = new ArrayList<>();

        for (String value : itemArray) {
            String[] kvArray = value.split("/");
            HashMap<String, String> kvMap = getKVMap(kvArray);

            ServerBean serverBean = new ServerBean();
            serverBean.setUrl(kvMap.get("ip"));
            serverBean.setPort(kvMap.get("port"));

            serverArray.add(serverBean);
        }

        msgServerBean.setServerArray(serverArray);

        return msgServerBean;
    }

    private String getMsgBody(InputStream is) throws IOException {
        byte[] allLengthBinary = new byte[4];
        is.read(allLengthBinary);

        int allLengthInt = toInt(allLengthBinary);
        int realLength = allLengthInt - 10;
        byte[] msgBodyBinary = new byte[realLength];
//        byte[] msgBodyBinarybackup = new byte[allLengthInt - 10];
        is.skip(8);
        int readedSize = is.read(msgBodyBinary);
//        Log.e("111", "readedSize:"+readedSize);
//        Log.e("111", "realLength:"+realLength);
        int readedSizeTmp = readedSize;

        while (readedSize < realLength) {
            int remainSize = realLength - readedSize;
//            Log.e("111", "remainSize:"+remainSize);
            realLength = remainSize;
            byte[] remainByte = new byte[remainSize];
            int readSizeNext = is.read(remainByte);
//            Log.e("111", "readSizeNext:"+readSizeNext);
            try {
                System.arraycopy(remainByte, 0, msgBodyBinary, readedSizeTmp, readSizeNext);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            readedSize = readSizeNext;
            readedSizeTmp += readSizeNext;
//            Log.e("111", "readedSizeTmp:"+readedSizeTmp);
        }
//
//        if (readedSize < realLength) {//加while机制
//            byte[] retainByte = new byte[realLength-readedSize];
//            int readSizeNext = is.read(retainByte);
//            System.arraycopy(retainByte,0,msgBodyBinary,readedSize,retainByte.length);
//        }

        is.skip(2);

        return new String(msgBodyBinary);
    }
}
