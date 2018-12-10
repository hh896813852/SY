package com.edusoho.yunketang.edu.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.edusoho.yunketang.R;


/**
 * Created by suju on 16/10/21.
 */
public class ActivityUtil {

    public static void setStatusBarTranslucent(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setRootViewFitsWindow(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusView = createStatusView(activity, color);
            decorView.addView(statusView);
        }
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        if (rootView.getChildCount() == 0) {
            return;
        }
        ViewGroup contentView = (ViewGroup) rootView.getChildAt(0);
        contentView.setFitsSystemWindows(true);
        contentView.setClipToPadding(true);
    }

    private static View createStatusView(Activity activity, int color) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        View statusView = new View(activity.getBaseContext());
        statusView.setId(R.id.custom_status_view);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public static void setStatusBarFitsByColor(Activity activity, int color) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(activity.getResources().getColor(color));
        tintManager.setStatusBarTintResource(color);
    }
}
