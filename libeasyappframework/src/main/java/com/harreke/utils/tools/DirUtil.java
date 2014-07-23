package com.harreke.utils.tools;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class DirUtil {
    public static int calculateDirSize(String path) {
        File[] files = getDirFiles(path);
        int dirSize = 0;

        if (files != null) {
            for (File file : files) {
                if (file.exists() && !file.isDirectory()) {
                    dirSize += file.length();
                }
            }
        }

        return dirSize;
    }

    public static boolean clearDir(String path) {
        File[] files = getDirFiles(path);

        if (files != null) {
            for (File file : files) {
                if (file.exists() && !file.isDirectory()) {
                    return file.delete();
                }
            }
        }

        return false;
    }

    public static File[] getDirFiles(String path) {
        File dir = new File(path);

        if (dir.isDirectory()) {
            return dir.listFiles();
        } else {
            return null;
        }
    }

    public static <T> ArrayList<T> readDirFile(File[] files, Class<T> classOfT) {
        ArrayList<T> list = new ArrayList<T>();
        T object;

        if (files != null) {
            for (File file : files) {
                if (file.exists() && !file.isDirectory()) {
                    object = GsonUtil.toBean(FileUtil.readFile(file), classOfT);
                    if (object != null) {
                        list.add(object);
                    }
                }
            }
        }

        return list;
    }

    public static <T> ArrayList<T> readDirFile(File[] files, Type type) {
        ArrayList<T> list = new ArrayList<T>();
        T object;

        if (files != null) {
            for (File file : files) {
                if (file.exists() && !file.isDirectory()) {
                    object = GsonUtil.toBean(FileUtil.readFile(file), type);
                    if (object != null) {
                        list.add(object);
                    }
                }
            }
        }

        return list;
    }

    public static <T> ArrayList<T> readDirFile(String path, Class<T> classOfT) {
        return readDirFile(getDirFiles(path), classOfT);
    }

    public static File[] sortFilesByTime(File[] files) {
        if (files != null) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    long result = lhs.lastModified() - rhs.lastModified();

                    if (result < 0) {
                        return 1;
                    } else if (result > 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            return files;
        } else {
            return null;
        }
    }
}