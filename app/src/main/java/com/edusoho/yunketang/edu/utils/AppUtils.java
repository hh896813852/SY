package com.edusoho.yunketang.edu.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by JesseHuang on 2017/5/5.
 */

public class AppUtils {

    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks).replace("B", "");
    }

    public static int getDisplayScreenWidth(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int getDisplayScreenHeight(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue / scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getDecimalMultiply(String num1, String num2) {
        return new BigDecimal(num1).multiply(new BigDecimal(num2)).floatValue();
    }

    public static float getDecimalDivide(String num1, String num2) {
        return new BigDecimal(num1).divide(new BigDecimal(num2)).floatValue();
    }

    public static void setDrawableStroke(Drawable drawable, int color) {
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.mutate();
            gradientDrawable.setStroke(4, color);
        }
    }

    public static void setDrawableColor(Drawable drawable, int color) {
        drawable.mutate();
        if (drawable instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) drawable;
            shapeDrawable.getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            colorDrawable.setColor(color);
        }
    }

    public static boolean isForegroundActivity(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);
        ComponentName componentInfo = task.get(0).topActivity;
        return componentInfo.getClassName().equals(activity.getClass().getName());
    }

    /**
     * month -> 月
     *
     * @param unit month，year
     * @return
     */
    public static String getVIPUnitNameByType(String unit) {
        if (unit.equals("day")) {
            return "天";
        } else if (unit.equals("month")) {
            return "个月";
        } else if (unit.equals("year")) {
            return "年";
        } else {
            return "";
        }
    }

    public static boolean isInstall(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                Log.d("flag--", "isInstall: " + pn);
                if (pn.equals(packageName)) {
                    Log.d("flag--", "isInstall: weixin--" + pn);
                    return true;
                }
            }
        }
        return false;
    }

    public static String encode2(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 0x2ff);
        }
        return new String(a);
    }

    public static boolean isNetConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static File getHtmlPluginStorage(Context context, String domain) {
        File html5plugin = context.getDir("html5plugin", Context.MODE_PRIVATE);
        File schoolStorage = new File(html5plugin, domain);
        if (!schoolStorage.exists()) {
            schoolStorage.mkdirs();
        }

        return schoolStorage;
    }
}
