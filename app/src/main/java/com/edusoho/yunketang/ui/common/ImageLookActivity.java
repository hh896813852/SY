package com.edusoho.yunketang.ui.common;

import com.edusoho.yunketang.R;
import com.previewlibrary.GPreviewActivity;

public class ImageLookActivity extends GPreviewActivity {
    /***
     * 重写该方法
     * 使用你的自定义布局
     **/
    @Override
    public int setContentLayout() {
        return R.layout.activity_image_look;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        transformOut();
    }
}