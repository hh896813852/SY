package com.edusoho.yunketang.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by any on 17/3/31.
 */

public class ToastHelper {

    public static void showToast(Context context, String message, boolean longToast) {
//        Toast mToast = new Toast(context);
//        View v = LayoutInflater.from(context).inflate(R.layout.widget_toast, null);
//        TextView textView = (TextView) v.findViewById(R.id.message);
//        textView.setText(message);
//        mToast.setDuration(longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.CENTER, 0, 0);
//        mToast.setView(v);
//        mToast.setText(message);
//        mToast.show();
        Toast.makeText(context, message, longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        showToast(context, message, false);
    }

    // 之前显示的内容
    private static String oldMsg;
    // Toast对象
    private static Toast toast = null;
    // 第一次时间
    private static long oneTime = 0;
    // 第二次时间
    private static long twoTime = 0;

    public static void showSingleToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
