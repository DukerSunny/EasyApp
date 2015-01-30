package tv.douyu.misc.danmaku;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by neavo on 14-3-17.
 */

public class MessageEncode {

    private String msgBody = "";

    public static MessageEncode begin() {
        return new MessageEncode();
    }

    public String getMsgBody() {
        return msgBody;
    }

    public ByteBuffer build() {
        ByteBuffer result = null;
//        System.out.println(msgBody);
//        Log.e("MessageEncode", "msgnody:" + msgBody);
        try {
            result = ByteBuffer.allocate(msgBody.getBytes("UTF-8").length + 13);
            result.order(ByteOrder.LITTLE_ENDIAN);
            result.putInt(msgBody.getBytes("UTF-8").length + 9);
            result.putInt(msgBody.getBytes("UTF-8").length + 9);
            result.putShort((short) 689);
            result.put(new byte[2]);
            result.put(msgBody.getBytes("UTF-8"));
            result.put(new byte[1]);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result != null ? result : ByteBuffer.allocate(0);
    }

    private String escape(String target) {
        return target.replaceAll("/", "@S").replaceAll("@", "@A");
    }

    public MessageEncode addItem(String key, String value) {
        msgBody = msgBody + escape(key) + "@=" + escape(value) + "/";

        return this;
    }
}
