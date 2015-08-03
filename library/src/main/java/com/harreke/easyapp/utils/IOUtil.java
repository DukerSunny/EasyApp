package com.harreke.easyapp.utils;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/03/20
 */
public class IOUtil {
    //    public static byte[] getBytes(int i) {
    //        byte[] bytes = new byte[4];
    //
    //        bytes[3] = (byte) (i & 0xff);
    //        bytes[2] = (byte) (i >> 8 & 0xff);
    //        bytes[1] = (byte) (i >> 16 & 0xff);
    //        bytes[0] = (byte) (i >> 24 & 0xff);
    //
    //        return bytes;
    //    }
    //
    //    public static byte[] getBytes(short s) {
    //        byte[] bytes = new byte[2];
    //
    //        bytes[1] = (byte) (s & 0xff);
    //        bytes[0] = (byte) (s >> 8 & 0xff);
    //
    //        return bytes;
    //    }
    //
    //    public static int getInt(byte[] bytes, int offset) {
    //        int b0 = bytes[offset] & 0xff;
    //        int b1 = bytes[offset + 1] & 0xff;
    //        int b2 = bytes[offset + 2] & 0xff;
    //        int b3 = bytes[offset + 3] & 0xff;
    //
    //        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    //    }

    public static boolean isSocketAvailable(Socket socket) {
        return socket != null && !socket.isClosed();
    }

    public static byte[] read(@NonNull InputStream stream, int count, String tag) {
        byte[] result = null;
        int length = 0;
        int read;

        if (count > 0) {
            result = new byte[count];
            try {
                while (length < count) {
                    read = stream.read(result, length, count - length);
                    //                    if (read == -1) {
                    //                        break;
                    //                    }
                    length += read;
                }
            } catch (IOException e) {
                LogUtil.e(tag, "read error at " + length + "/" + count + ", " + e.getMessage());
            }
        }

        return result;
    }

    public static boolean skip(@NonNull InputStream stream, int count, String tag) {
        int length = 0;
        long skip;

        if (count > 0) {
            try {
                while (length < count) {
                    skip = stream.skip(count - length);
                    length += skip;
                }
            } catch (IOException e) {
                LogUtil.e(tag, "skip error at " + length + "/" + count + ", " + e.getMessage());
            }
        }

        return length == count;
    }

    //    public static int readInt(InputStream stream) throws IOException {
    //        byte[] bytes;
    //
    //        if (stream != null) {
    //            bytes = read(stream, 4);
    //
    //            return getInt(bytes, 0);
    //        } else {
    //            throw new IOException();
    //        }
    //    }
    //
    //    public static boolean skip(InputStream stream, int count) throws IOException {
    //        long length = 0l;
    //        long skip;
    //
    //        if (stream != null) {
    //            while (length < count) {
    //                skip = stream.skip(count - length);
    //                if (skip == 0) {
    //                    break;
    //                }
    //                length += skip;
    //            }
    //        }
    //
    //        return length == count;
    //    }

    public static void write(OutputStream stream, byte[] bytes) throws IOException {
        stream.write(bytes);
        stream.flush();
    }
}