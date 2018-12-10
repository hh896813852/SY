package com.edusoho.yunketang.edu.bean;

/**
 * Created by JesseHuang on 2017/4/25.
 */

public class MessageEvent<T> {

    public static final int NO_CODE                           = -1;
    public static final int COURSE_EXIT                       = 0;
    public static final int LEARN_TASK                        = 1;
    public static final int SHOW_NEXT_TASK                    = 2;
    public static final int LOGIN                             = 3;
    public static final int FINISH_TASK_SUCCESS               = 4;
    public static final int FULL_SCREEN                       = 5;
    /**
     * 更新Adapter item状态：高亮、半圈
     */
    public static final int COURSE_TASK_ITEM_UPDATE           = 6;
    public static final int LEARN_NEXT_TASK                   = 7;
    public static final int SHOW_VIP_BUTTON                   = 8;
    public static final int PAY_SUCCESS                       = 9;
    public static final int PPT_DOWNLOAD_DOING                = 10;
    public static final int PPT_DONWLOAD_FINISH               = 11;
    public static final int WXPAY_FINISH                      = 12;
    public static final int HIDE_CONTROLLER                   = 13;
    public static final int SHOW_CONTROLLER                   = 14;
    public static final int REWARD_POINT_NOTIFY               = 15;
    public static final int BIND_THIRD_SUCCESS                = 16;
    public static final int UPDATE_ORDER_VIEW                 = 17;
    public static final int UPDATE_COURSE_INDEX               = 18;
    public static final int TESTPAPER_PREVIOUS_QUESTION       = 19;
    public static final int TESTPAPER_NEXT_QUESTION           = 20;
    public static final int TESTPAPER_PREVIOUS_TYPE           = 21;
    public static final int TESTPAPER_NEXT_TYPE               = 22;
    public static final int TESTPAPER_CARD_CLICK_SWITCH_TYPE  = 23;
    public static final int TESTPAPER_CARD_CLICK_SWITCH_INDEX = 24;

    public static final int LOGIN_REFRESH_COURSE_SET = 25;

    /**
     * 点击课程中的计划，刷新课程任务Fragment中的列表
     */
    public static final int REFRESH_COURSESET_TASK = 19;
    public static final int CREDENTIAL_EXPIRED     = 401;


    private T   mMessage;
    private int mCode;

    public MessageEvent(T message) {
        mMessage = message;
        mCode = NO_CODE;
    }

    public MessageEvent(T message, int code) {
        mMessage = message;
        mCode = code;
    }

    public MessageEvent(int code) {
        mMessage = null;
        mCode = code;
    }

    public T getMessageBody() {
        return mMessage;
    }

    public int getType() {
        return mCode;
    }
}
