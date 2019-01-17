package com.edusoho.yunketang.helper;

import android.app.Activity;
import android.graphics.Rect;

import com.edusoho.yunketang.ui.common.ImageLookActivity;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.enitity.ThumbViewInfo;

import java.util.ArrayList;
import java.util.List;

public class PicPreviewHelper {

    private static volatile PicPreviewHelper defaultInstance;
    private static ArrayList<ThumbViewInfo> mThumbViewInfoList;

    private PicPreviewHelper() {
    }

    public static PicPreviewHelper getInstance() {
        if (defaultInstance == null) {
            synchronized (PicPreviewHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new PicPreviewHelper();
                    mThumbViewInfoList = new ArrayList<>();
                    ZoomMediaLoader.getInstance().init(new ImageLoader());
                }
            }
        }
        return defaultInstance;
    }

    public PicPreviewHelper setUrl(String url) {
        mThumbViewInfoList.clear();
        Rect bounds = new Rect();
        ThumbViewInfo item = new ThumbViewInfo(url);
        item.setBounds(bounds);
        mThumbViewInfoList.add(item);
        return defaultInstance;
    }

    public PicPreviewHelper setData(List<String> urls) {
        mThumbViewInfoList.clear();
        for (String url : urls) {
            Rect bounds = new Rect();
            ThumbViewInfo item = new ThumbViewInfo(url);
            item.setBounds(bounds);
            mThumbViewInfoList.add(item);
        }
        return defaultInstance;
    }

    public void preview(Activity activity, int currentIndex) {
        // 打开预览界面
        GPreviewBuilder.from(activity)
                //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
                .to(ImageLookActivity.class)
                .setData(mThumbViewInfoList)
                .setCurrentIndex(currentIndex)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                // 小圆点
                .setType(GPreviewBuilder.IndicatorType.Dot)
                .start();//启动
    }
}
