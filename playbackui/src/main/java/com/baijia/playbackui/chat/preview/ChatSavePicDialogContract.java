package com.baijia.playbackui.chat.preview;

import com.baijia.playbackui.base.PBBasePresenter;
import com.baijia.playbackui.base.PBBaseView;


/**
 * Created by wangkangfei on 17/5/13.
 */

public interface ChatSavePicDialogContract {

    interface View extends PBBaseView<Presenter> {

    }

    interface Presenter extends PBBasePresenter {
        void realSavePic();
    }
}
