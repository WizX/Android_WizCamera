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


    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }
}
