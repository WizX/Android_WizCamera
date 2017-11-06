package com.android.wizcamera.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/11/6 14:21<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class FileUtil {
    /**
     * 尽可能的创建文件夹
     *
     * @param directory 要川创建的文件夹
     * @return true:创建成功(或者已经存在)，其他创建失败
     */
    public static boolean createDir(File directory) {
        if (directory == null) {
            return false;
        }
        if (!directory.exists()) {
            boolean isMkdirs = directory.mkdirs();
            Log.w("wjc", "create directory " + directory.getAbsolutePath() + " " + isMkdirs);
            return isMkdirs;
        }
        if (directory.isDirectory()) {
            return true;
        }
        boolean delete = directory.delete();
        return delete && createDir(directory);
    }

    public static File generateName(File directory) {
        String suffix = ".jpg";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String time = dateFormat.format(new java.util.Date());
        String fileName = "IMG_" + time;
        File newFile = new File(directory, fileName + suffix);
        int index = 0;
        while (newFile.exists()) {
            index++;
            newFile = new File(directory, fileName + "_" + index + suffix);
        }
        return newFile;
    }

    /**
     * 关闭{@code Closeable},已处理异常
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e("FileUtil", e.toString());
            }
        }
    }
}
