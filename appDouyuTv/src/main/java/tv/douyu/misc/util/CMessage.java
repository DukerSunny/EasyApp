package tv.douyu.misc.util;

import android.content.Context;

/**
 * Created by Administrator on 2014/12/23.
 */
public class CMessage {
    public static CMessage message = null;
    private static final String TAG = "CMessage";

    static {
        System.loadLibrary("encryption");
    }

    public static synchronized CMessage getInstance() {
        if (message == null) {
            message = new CMessage();
        }

        return message;

    }

    private native String jni_getEncryption(Context context) throws IllegalStateException;
    private native String jni_getCodeEncryption(Context context) throws IllegalStateException;
    private native String jni_getKeyEncryption(Context context) throws IllegalStateException;

    public String getEncryption(Context c){

        return jni_getEncryption(c);

    }

    public String getCodeEncryption(Context c){

        return jni_getCodeEncryption(c);
    }

    public String getKeyEncryption(Context c){

        return jni_getKeyEncryption(c);
    }

}
