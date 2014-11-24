package com.harreke.easyapp.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
 *
 * 文件工具
 *
 * 支持拷贝、删除操作
 *
 * 可以读取、写入Bitmap、Drawable和文本文件
 */
public class FileUtil {
    protected final static String TAG = "FileUtil";

    public static boolean copyFile(File source, File target) {
        boolean create;
        boolean success = false;

        if (source != null && source.exists() && source.isFile()) {
            try {
                create = target.createNewFile();
                if (create || target.canWrite()) {
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

    public static boolean copyFile(String sourceFilename, String targetFilename) {
        return sourceFilename != null && sourceFilename.startsWith("file:") && targetFilename != null &&
                targetFilename.startsWith("file:") &&
                copyFile(new File(sourceFilename), new File(targetFilename));
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

    public static boolean deleteFile(String filename) {
        return filename != null && filename.startsWith("file:") && deleteFile(new File(filename));
    }

    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.isFile() && file.delete();
    }

    public static Bitmap readBitmap(String filename) {
        return readBitmap(filename, 0, 0);
    }

    public static Bitmap readBitmap(String filename, int width, int height) {
        Bitmap bitmap = null;

        if (filename != null && filename.startsWith("file:")) {
            bitmap = BitmapFactory.decodeFile(filename);
            if (width > 0 && height > 0 && width != bitmap.getWidth() && height != bitmap.getHeight()) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
        }

        return bitmap;
    }

    public static Bitmap readBitmap(File file) {
        return readBitmap(file, 0, 0);
    }

    public static Bitmap readBitmap(File file, int width, int height) {
        if (file != null && file.exists() && file.isFile()) {
            return readBitmap(file.getAbsoluteFile(), width, height);
        } else {
            return null;
        }
    }

    public static Drawable readDrawable(String filename) {
        return readDrawable(filename, 0, 0);
    }

    public static Drawable readDrawable(String filename, int width, int height) {
        Drawable drawable = null;

        if (filename != null && filename.length() > 0) {
            drawable = Drawable.createFromPath(filename);
            if (drawable != null && width > 0 && height > 0) {
                drawable.setBounds(0, 0, width, height);
            }
        }

        return drawable;
    }

    public static Drawable readDrawable(File file) {
        return readDrawable(file, 0, 0);
    }

    public static Drawable readDrawable(File file, int width, int height) {
        if (file != null && file.exists() && file.isFile()) {
            return readDrawable(file.getAbsoluteFile(), width, height);
        } else {
            return null;
        }
    }

    public static String readTxt(String filename) {
        if (filename != null && filename.startsWith("file:")) {
            return readTxt(new File(filename));
        } else {
            return null;
        }
    }

    public static String readTxt(File file) {
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        FileInputStream fileInputStream;
        String content = null;
        String line;

        if (file != null && file.exists() && file.isFile()) {
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

    public static boolean writeBitmap(String filename, Bitmap bitmap) {
        return filename != null && filename.startsWith("file:") && writeBitmap(new File(filename), bitmap);
    }

    public static boolean writeBitmap(File file, Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        boolean create;
        boolean success = false;

        if (file != null && bitmap != null) {
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
        }

        return success;
    }

    public static boolean writeDrawable(String filename, Drawable drawable) {
        return filename != null && filename.startsWith("file:") && writeDrawable(new File(filename), drawable);
    }

    public static boolean writeDrawable(File file, Drawable drawable) {
        Bitmap bitmap;
        FileOutputStream fileOutputStream;
        boolean create;
        boolean success = false;

        if (file != null && drawable != null) {
            try {
                create = file.createNewFile();
                if (create || file.canWrite()) {
                    fileOutputStream = new FileOutputStream(file);
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
                    fileOutputStream.close();
                    fileOutputStream.flush();
                    success = true;
                }
            } catch (IOException e) {
                success = false;
                Log.e(TAG, "Write drawable " + file.getAbsolutePath() + " error!");
            }
        }

        return success;
    }

    public static boolean writeTxt(String filename, String content) {
        return filename != null && filename.startsWith("file:") && writeTxt(new File(filename), content);
    }

    public static boolean writeTxt(File file, String content) {
        PrintWriter printWriter;
        boolean create;
        boolean success = false;

        if (file != null && content != null) {
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
        }

        return success;
    }
}