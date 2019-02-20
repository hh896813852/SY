package com.baijia.playbackui.chat;

import com.baijia.playbackui.base.PBBasePresenter;
import com.baijia.playbackui.base.PBBaseView;
import com.baijiahulian.livecore.models.imodels.IMessageModel;

/**
 * Created by wangkangfei on 17/8/17.
 */

public interface PBChatContract {
    public interface View extends PBBaseView<Presenter> {
    }

    public interface Presenter extends PBBasePresenter {
    }

}
