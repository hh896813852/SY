package com.edusoho.yunketang.bean;

import java.io.Serializable;

/**
 * @author huhao on 2018/12/14.
 */

public class Setting implements Serializable {
    public int isAllow4GPlay;       // 0、不允许4G流量播放视频 1、允许
    public int soundSwitchState;    // 0、消息声音未开启 1、消息声音已开启
    public int vibrationSwitchState;// 0、震动未开启 1、震动已开启
}