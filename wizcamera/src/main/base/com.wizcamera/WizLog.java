package com.wizcamera;

import android.os.Build;
import android.os.Debug;
import android.util.Log;

/**
 * description：打印日志 <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/10/20 17:20<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class WizLog {
    private static boolean debug = true;

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, formatString(msg, Build.VERSION.SDK_INT));
    }

    public static void e(String tag, String msg, Exception e) {
        if (debug)
            Log.e(tag, formatString(msg, Build.VERSION.SDK_INT), e);
    }

    public static void e(String tag, String msg, Object... args) {
        if (debug)
            Log.e(tag, formatString(msg, Build.VERSION.SDK_INT, args));
    }

    private static String formatString(String str, Object... args) {
        return String.format(str, args);
    }

    private static String getTag(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
