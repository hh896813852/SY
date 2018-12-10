package com.edusoho.yunketang.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author huhao on 2018/4/20.
 */
public class ScreenUtil {

    /**
     * 获取状态栏高度 1
     * 优点：不需要在Activity的回调方法onWindowFocusChanged()执行后才能得到结果
     * 缺点：不管你是否设置全屏模式，或是不显示标题栏，高度是固定的；因为系统资源属性是固定的、真实的，不管你是否隐瞒（隐藏或者显示），它都在那里
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        // 获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度 2
     * 优点：依赖于WMS，是在界面构建后根据View获取的，所以高度是动态的
     * 缺点：在Activity的回调方法onWindowFocusChanged()执行后，才能得到预期结果
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取标题栏高度
     * 注：在Activity的回调方法onWindowFocusChanged()执行后，才能得到预期结果
     */
    public static int getTitleHeight(Activity activity) {
        return getViewDrawHeight(activity) - getStatusBarHeight(activity);
    }

    /**
     * 获取View绘制区域TOP高度
     * 注：在Activity的回调方法onWindowFocusChanged()执行后，才能得到预期结果
     */
    public static int getViewDrawHeight(Activity activity) {
        // View绘制区域高度
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        return dm.heightPixels;
    }

    /**
     * 获取虚拟功能键高度
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 截屏
     */
    public static Bitmap shotActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() - getVirtualBarHeight(activity));
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bp;
    }
}
