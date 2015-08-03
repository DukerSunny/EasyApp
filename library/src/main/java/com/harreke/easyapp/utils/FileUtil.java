package com.harreke.easyapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * 文件工具
 * <p/>
 * 支持拷贝、删除操作
 * <p/>
 * 可以读取、写入Bitmap、Drawable和文本文件
 */
public class FileUtil {
    protected final static String TAG = "FileUtil";

    public static boolean copyFile(@NonNull File source, @NonNull File target) {
        boolean create;
        boolean success = false;

        if (source.exists() && source.isFile()) {
            try {
                create = target.createNewFile();
                if (create || (target.isFile() && target.canWrite())) {
                    copyFile(new FileInputStream(source), new FileOutputStream(target));
                }
                success = true;
            } catch (IOException e) {
                success = false;
                Log.e(TAG, "Copy file " + source.getAbsolutePath() + " to " + target.getAbsolutePath() + " error!");
            }
        }

        return success;
    }

    public static boolean copyFile(@NonNull String sourceFilename, @NonNull String targetFilename) {
        return copyFile(new File(sourceFilename), new File(targetFilename));
    }

    public static boolean copyFile(InputStream inputStream, FileOutputStream fileOutputStream) {
        BufferedOutputStream outputStream;
        boolean success = false;
        byte[] buffer = new byte[1024];
        int read;

        if (inputStream != null && fileOutputStream != null) {
            try {
                outputStream = new BufferedOutputStream(fileOutputStream);
                while ((read = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.close();
                inputStream.close();
                success = true;
            } catch (IOException e) {
                success = false;
                Log.e(TAG, "Copy file error!");
            }
        }

        return success;
    }

    public static boolean deleteFile(@NonNull String filename) {
        return deleteFile(new File(filename));
    }

    public static boolean deleteFile(@NonNull File file) {
        return file.exists() && file.isFile() && file.delete();
    }

    public static String getFilePath(@NonNull Context context, @NonNull Uri uri) {
        String scheme = uri.getScheme();
        String data = null;
        Cursor cursor;
        int index;

        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        return data;
    }

    public static Bitmap readBitmap(@NonNull String filename) {
        return readBitmap(filename, 0, 0);
    }

    public static Bitmap readBitmap(@NonNull String filename, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeFile(filename);

        if (width > 0 && height > 0 && (width != bitmap.getWidth() || height != bitmap.getHeight())) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        return bitmap;
    }

    public static Bitmap readBitmap(@NonNull File file) {
        return readBitmap(file, 0, 0);
    }

    public static Bitmap readBitmap(@NonNull File file, int width, int height) {
        if (file.exists() && file.isFile()) {
            return readBitmap(file.getAbsolutePath(), width, height);
        } else {
            return null;
        }
    }

    public static Drawable readDrawable(@NonNull String filename) {
        return readDrawable(filename, 0, 0);
    }

    public static Drawable readDrawable(@NonNull String filename, int width, int height) {
        Drawable drawable = null;

        if (filename.length() > 0) {
            drawable = Drawable.createFromPath(filename);
            if (drawable != null && width > 0 && height > 0) {
                drawable.setBounds(0, 0, width, height);
            }
        }

        return drawable;
    }

    public static Drawable readDrawable(@NonNull File file) {
        return readDrawable(file, 0, 0);
    }

    public static Drawable readDrawable(@NonNull File file, int width, int height) {
        return file.exists() && file.isFile() ? readDrawable(file.getAbsolutePath(), width, height) : null;
    }

    public static String readTxt(@NonNull String filename) {
        return readTxt(new File(filename));
    }

    public static String readTxt(@NonNull File file) {
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        FileInputStream fileInputStream;
        String content = null;
        String line;

        if (file.exists() && file.isFile()) {
            try {
                fileInputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(fileInputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                content = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content += line;
                }
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
            } catch (IOException e) {
                content = null;
                Log.e(TAG, "Read txt file " + file.getAbsolutePath() + " error!");
            }
        }

        return content;
    }

    public static boolean writeBitmap(@NonNull String filename, @NonNull Bitmap bitmap) {
        return writeBitmap(new File(filename), bitmap);
    }

    public static boolean writeBitmap(@NonNull File file, @NonNull Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        boolean create;
        boolean success = false;

        try {
            create = file.createNewFile();
            if (create || file.canWrite()) {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
                fileOutputStream.close();
                fileOutputStream.flush();
                success = true;
            }
        } catch (IOException e) {
            success = false;
            Log.e(TAG, "Write bitmap " + file.getAbsolutePath() + " error!");
        }

        return success;
    }

    public static boolean writeDrawable(@NonNull String filename, @NonNull Drawable drawable) {
        return writeBitmap(new File(filename), ((BitmapDrawable) drawable).getBitmap());
    }

    //    public static boolean writeDrawable(@NonNull File file, @NonNull Drawable drawable) {
    //        Bitmap bitmap;
    //        FileOutputStream fileOutputStream;
    //        boolean create;
    //        boolean success = false;
    //
    //        try {
    //            create = file.createNewFile();
    //            if (create || file.canWrite()) {
    //                fileOutputStream = new FileOutputStream(file);
    //                bitmap = ((BitmapDrawable) drawable).getBitmap();
    //                bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
    //                fileOutputStream.close();
    //                fileOutputStream.flush();
    //                success = true;
    //            }
    //        } catch (IOException e) {
    //            success = false;
    //            Log.e(TAG, "Write drawable " + file.getAbsolutePath() + " error!");
    //        }
    //
    //        return success;
    //    }

    public static boolean writeTxt(@NonNull String filename, @NonNull String content) {
        return writeTxt(new File(filename), content);
    }

    public static boolean writeTxt(@NonNull File file, @NonNull String content) {
        PrintWriter printWriter;
        boolean create;
        boolean success = false;

        try {
            create = file.createNewFile();
            if (create || file.canWrite()) {
                printWriter = new PrintWriter(file);
                printWriter.write(content);
                printWriter.close();
                printWriter.flush();
                success = true;
            }
        } catch (IOException e) {
            success = false;
            Log.e(TAG, "Write file " + file.getAbsolutePath() + " error!");
        }

        return success;
    }
}