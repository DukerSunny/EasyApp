package tv.douyu.misc.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

/**
 * Created by neavo on 14-3-17.
 */

public class IOUtils {

    public static void closeQuite(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void closeQuite(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void closeQuite(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void closeQuite(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void write(byte[] binary, File target) {
        try {
            target.getParentFile().mkdirs();

            FileOutputStream targetOS = new FileOutputStream(target);
            BufferedInputStream binaryIS = new BufferedInputStream(new ByteArrayInputStream(binary));

            int Length;
            byte[] Buffer = new byte[4096];
            while ((Length = binaryIS.read(Buffer)) != -1) {
                targetOS.write(Buffer, 0, Length);
            }

            targetOS.flush();
            targetOS.close();
            binaryIS.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static byte[] readByte(InputStream is) throws IOException {
        byte[] result = new byte[0];

        BufferedInputStream bufferedIS = new BufferedInputStream(is);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        try {
            int Length;
            byte[] Buffer = new byte[4096];

            while ((Length = bufferedIS.read(Buffer)) != -1) {
                byteArrayOS.write(Buffer, 0, Length);
            }

            byteArrayOS.flush();
            result = byteArrayOS.toByteArray();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuite(bufferedIS);
            IOUtils.closeQuite(byteArrayOS);
        }

        return result;
    }

}
