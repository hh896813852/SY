package com.edusoho.yunketang.base.core;

import android.app.Activity;

import com.edusoho.yunketang.utils.LogUtil;

import java.util.Vector;

/**
 * @author huhao on 2018/07/04
 */
public class ActivityManager {
    private static final String TAG = "ActivityManager";

    private static Vector<Activity> activities = new Vector<Activity>();

    public static int size() {
        return activities.size();
    }

    /**
     * 从某个Activity开始,清理掉下面所有的Activity
     *
     * @param activity 停止被清理的那个Activity
     */
    public static void clearBottomActivitiesUntil(Activity activity) {
        for (int i = 0; i < activities.size(); i++) {
            Activity activityInTask = activities.get(i);
            if (activityInTask.equals(activity)) {
                return;
            }
            removeActivity(activityInTask);
            activityInTask.finish();
            i--;
        }
    }

    /**
     * 关闭当前Activity与某个Activity之间的所有Activity
     *
     * @param stopFinishActivityClass 停止被关闭的Activity类 如果传null,则关闭当前Activity下面的所有Activity
     */
    public static void finishActivitiesUntil(Class<? extends Activity> stopFinishActivityClass) {
        for (int i = activities.size() - 2; i > -1; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass().equals(stopFinishActivityClass)) {
                break;
            }
            removeActivity(activity);
            activity.finish();
        }
    }

    public static Activity getPreviousActivity() {
        return size() > 1 ? activities.get(size() - 2) : null;
    }

    public static Activity getTopActivity() {
        return size() > 0 ? activities.get(size() - 1) : null;
    }

    public static void addActivity(Activity activity) {
        LogUtil.v(TAG, "[Add] " + activity.getClass().getSimpleName());
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            LogUtil.v(TAG, "[Del] " + activity.getClass().getSimpleName());
            activities.remove(activity);
        }
    }

    public static void removeActivity(Class<?> clazz) {
        for (int i = activities.size() - 1; i > -1; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass().equals(clazz)) {
                activities.remove(i);
                activity.finish();
                return;
            }
        }
    }

    public static Activity getActivity(Class<?> clazz) {
        for (int i = activities.size() - 1; i > -1; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass().equals(clazz)) {
                return activity;
            }
        }
        return null;
    }

    public static void clear() {
        LogUtil.v(TAG, "[CLS] ");
        for (int i = activities.size() - 1; i > -1; i--) {
            Activity activity = activities.get(i);
            removeActivity(activity);
            activity.finish();
            i = activities.size();
        }
    }

    public static void clearToTop() {
        LogUtil.v(TAG, "[CTT] ");
        for (int i = activities.size() - 2; i > -1; i--) {
            Activity activity = activities.get(i);
            removeActivity(activity);
            activity.finish();
            i = activities.size() - 1;
        }
    }

    public static boolean containsActivity(Class<? extends Activity> activityClass) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    public static void finishActivity(Class<? extends Activity> activityClass) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass().equals(activityClass)) {
                activities.get(i).finish();
            }
        }
    }
}