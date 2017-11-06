package com.android.wizcamera.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public final class DimensUtil {

    @Deprecated
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Deprecated
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @param ctx
     *
     * @return
     */
    public static int getDisplayWidth(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        if (ctx != null) {
            WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            winManager.getDefaultDisplay().getMetrics(metric);
        }
        return metric.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     *
     * @param ctx
     *
     * @return
     */
    public static int getDisplayHeight(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        if (ctx != null) {
            WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            winManager.getDefaultDisplay().getMetrics(metric);
        }
        return metric.heightPixels;
    }

    /**
     * 获取组件在屏幕中的位置
     */
    public static Rect getMatricInDisplay(View view) {
        if (view == null) {
            return null;
        }

        Rect rect = new Rect();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.right = view.getWidth() + rect.left;
        rect.bottom = view.getHeight() + rect.top;
        return rect;
    }

}
