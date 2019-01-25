package com.edusoho.yunketang.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.animation.BaseAnimatorSet;
import com.edusoho.yunketang.base.animation.BounceEnter.BounceTopEnter;
import com.edusoho.yunketang.base.animation.SlideExit.SlideTopExit;
import com.edusoho.yunketang.base.animation.ZoomEnter.ZoomInEnter;
import com.edusoho.yunketang.base.animation.ZoomExit.ZoomOutExit;
import com.edusoho.yunketang.widget.dialog.SXYDialog;
import com.edusoho.yunketang.widget.dialog.SimpleDialog;
import com.edusoho.yunketang.widget.dialog.TipDialog;
import com.edusoho.yunketang.widget.dialog.UpdateDialog;

/**
 * @author any
 */
public class DialogUtil {

    /**
     * 展示一个提示框（一个按钮）
     * 按钮名称为确定
     * 按钮点击监听为关闭当前提示框
     *
     * @param context 上下文
     * @param title   提示框标题，不填为默认的“提示”
     * @param msg     提示信息
     */
    public static void showAlert(Context context, String title, String msg) {
        showAlert(context, title, msg, "确定");
    }

    /**
     * 展示一个提示框（一个按钮）
     * 此处按钮的作用仅仅是关闭提示框
     *
     * @param context    上下文
     * @param title      提示框标题，不填为默认的“提示”
     * @param msg        提示信息
     * @param buttonText 按钮文字
     */
    public static void showAlert(Context context, String title, String msg, String buttonText) {
        showAlert(context, title, msg, buttonText, null);
    }

    /**
     * 展示一个提示框（一个按钮）
     * 按钮名称固定为“确定”
     * 可自由监听按钮点击
     *
     * @param context  上下文
     * @param title    提示框标题，不填为默认的“提示”
     * @param msg      提示信息
     * @param listener 点击按钮后的监听
     */
    public static Dialog showAlert(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        return showAlert(context, title, msg, "确定", listener);
    }

    /**
     * 展示一个提示框
     * 可自由设置按钮名称
     * 可自由监听按钮点击
     *
     * @param context    上下文
     * @param title      提示框标题，不填为默认的“提示”
     * @param msg        提示信息
     * @param buttonText 按钮文字
     * @param listener   点击按钮后的监听
     */
    public static Dialog showAlert(Context context, String title, String msg, String buttonText, DialogInterface.OnClickListener listener) {
        return showDialog(context, title, msg, buttonText, listener, null, null, null, null, true);
    }

    /**
     * 展示一个包含两个按钮的对话框
     *
     * @param context     上下文
     * @param title       提示框标题，不填为默认的“提示”
     * @param msg         提示信息
     * @param buttonText1 第一个按钮文字
     * @param listener1
     * @param buttonText2 第二个按钮文字
     * @param listener2
     * @param cancelable  点击外部区域和BACK键是否可以取消
     * @return
     */
    public static Dialog showDialog(Context context,
                                    String title,
                                    String msg,
                                    String buttonText1, DialogInterface.OnClickListener listener1,
                                    String buttonText2, DialogInterface.OnClickListener listener2, boolean cancelable) {
        return showDialog(context, title, msg, buttonText1, listener1, buttonText2, listener2, null, null, cancelable);
    }

    /**
     * 弹出一个Prompt
     *
     * @param view
     * @param title
     * @param okListener
     * @param cancelListener
     */
    public static Dialog showPrompt(View view,
                                    String title,
                                    DialogInterface.OnClickListener okListener,
                                    DialogInterface.OnClickListener cancelListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                    .setTitle(title)
                    .setView(view)
                    .setPositiveButton("确定", okListener)
                    .setNegativeButton("取消", cancelListener);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            LogUtil.e("=======Show Dialog Error=======", e);
        }
        return null;
    }

    /**
     * 展示一个包含三个按钮的对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param buttonText1
     * @param listener1
     * @param buttonText2
     * @param listener2
     * @param buttonText3
     * @param listener3
     * @param cancelable  点击外部区域和BACK键是否可以取消
     * @return
     */
    public static Dialog showDialog(Context context,
                                    String title,
                                    String msg,
                                    String buttonText1, DialogInterface.OnClickListener listener1,
                                    String buttonText2, DialogInterface.OnClickListener listener2,
                                    String buttonText3, DialogInterface.OnClickListener listener3, boolean cancelable) {
        try {
            if (title == null || title.equals("")) {
                title = "提示";
            }
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT < 21) {
                builder = new AlertDialog.Builder(context);
            } else {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
            }
            builder.setTitle(title).setMessage(msg);
            if (buttonText1 != null) {
                builder.setPositiveButton(buttonText1, listener1);
            }
            if (buttonText2 != null) {
                builder.setNeutralButton(buttonText2, listener2);
            }
            if (buttonText3 != null) {
                builder.setNegativeButton(buttonText3, listener3);
            }
            AlertDialog dialog = builder.create();
            dialog.setCancelable(cancelable);
            dialog.show();
            return dialog;
        } catch (Throwable e) {
            LogUtil.e("=======Show Dialog Error=======", e);
        }
        return null;
    }

    /**
     * 展示一个列表选择
     *
     * @param context
     * @param title
     * @param items
     * @param listener
     * @param cancelable 点击外部区域和BACK键是否可以取消
     * @return
     */
    public static Dialog showList(Context context,
                                  String title,
                                  String[] items,
                                  DialogInterface.OnClickListener listener,
                                  boolean cancelable) {
        try {
            if (title == null || title.equals("")) {
                title = "提示";
            }
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT < 21) {
                builder = new AlertDialog.Builder(context);
            } else {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
            }
            builder.setTitle(title).setItems(items, listener);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(cancelable);
            dialog.show();
            return dialog;
        } catch (Throwable e) {
            LogUtil.e("=======Show Dialog Error=======", e);
        }
        return null;
    }

    public static BaseDialog showAnimationDialog(Context context, int layoutId) {
        return showAnimationDialog(context, layoutId, true);
    }

    public static BaseDialog showAnimationDialog(Context context, int layoutId, boolean cancelable) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setCanceledOnTouchOutside(cancelable);
        baseDialog.setCustomLayout(layoutId)
                .widthScale(0.8f)
                .showAnim(new BounceTopEnter())
                .dismissAnim(new SlideTopExit(300))
                .show();
        return baseDialog;
    }


    /**
     * 简易Dialog
     *
     * @param context             上下文
     * @param content             内容
     * @param leftBtnText         左侧按钮文字
     * @param rightBtnText        右侧按钮文字
     * @param simpleClickListener 监听器
     */
    public static void showSimpleAnimDialog(Context context, String content, String leftBtnText, String rightBtnText, SimpleDialog.OnSimpleClickListener simpleClickListener) {
        showSimpleAnimDialog(context, content, leftBtnText, rightBtnText, new BounceTopEnter(), new SlideTopExit(300), simpleClickListener);
    }

    /**
     * 简易Dialog
     *
     * @param context             上下文
     * @param content             内容
     * @param leftBtnText         左侧按钮文字
     * @param rightBtnText        右侧按钮文字
     * @param simpleClickListener 监听器
     */
    public static void showSimpleAnimDialog(Context context, String content, String leftBtnText, String rightBtnText, BaseAnimatorSet showAnim, BaseAnimatorSet dismissAnim, SimpleDialog.OnSimpleClickListener simpleClickListener) {
        SimpleDialog simpleDialog = new SimpleDialog(context);
        simpleDialog.setCanceledOnTouchOutside(true);
        simpleDialog.setContentText(content)
                .setLeftBtnText(leftBtnText)
                .setRightBtnText(rightBtnText)
                .setOnSimpleClickListener(simpleClickListener)
                .setCustomLayout(R.layout.dialog_simple)
                .widthScale(0.8f)
                .showAnim(showAnim)
                .dismissAnim(dismissAnim)
                .show();
    }

    /**
     * 更新Dialog
     * @param context         上下文
     * @param version         版本号
     * @param content         更新内容
     * @param onClickListener 监听器
     */
    public static void showUpdateDialog(Context context, boolean isForce, String version, String content, UpdateDialog.OnClickListener onClickListener) {
        UpdateDialog updateDialog = new UpdateDialog(context);
        updateDialog.setCanceledOnTouchOutside(!isForce);
        updateDialog.setVersionText(version)
                .setOnClickListener(onClickListener)
                .setIsForce(isForce)
                .setContentText(content)
                .setCustomLayout(R.layout.dialog_update)
                .widthScale(0.8f)
                .showAnim(new ZoomInEnter())
                .dismissAnim(new ZoomOutExit())
                .show();
    }

    /**
     * 上小元Dialog
     *
     * @param context         上下文
     * @param percent         正确率
     * @param onClickListener 监听器
     */
    public static void showSXY(Context context, String percent, SXYDialog.OnClickListener onClickListener) {
        SXYDialog sxyDialog = new SXYDialog(context);
        sxyDialog.setCanceledOnTouchOutside(true);
        sxyDialog.setCorrectPercent(percent)
                .setOnClickListener(onClickListener)
                .setCustomLayout(R.layout.dialog_sxy)
                .widthScale(0.8f)
                .showAnim(new ZoomInEnter())
                .dismissAnim(new ZoomOutExit())
                .show();
    }

    /**
     * 提示对话框
     *
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     */
    public static void showTipDialog(Context context, String title, String content) {
        TipDialog tipDialog = new TipDialog(context);
        tipDialog.setCanceledOnTouchOutside(true);
        tipDialog.setTitleText(title)
                .setContentText(content)
                .setCustomLayout(R.layout.dialog_tip)
                .widthScale(0.8f)
                .showAnim(new ZoomInEnter())
                .dismissAnim(new ZoomOutExit())
                .show();
    }
}