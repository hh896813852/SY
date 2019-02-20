package com.baijia.playbackui.base;

import com.baijia.playbackui.activity.PBRouterListener;

/**
 * Created by wangkangfei on 17/8/17.
 */

public interface PBBasePresenter {
    void setRouter(PBRouterListener listener);

    void create();

    void destroy();

    void subscribe();

    void unSubscribe();
}
