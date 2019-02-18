package com.edusoho.yunketang.helper;

import android.app.Activity;
import android.graphics.Rect;
import android.widget.ImageView;

import com.edusoho.yunketang.bean.preview.UserViewInfo;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;

import java.util.ArrayList;
import java.util.List;

public class PicPreviewHelper {

    private static volatile PicPreviewHelper defaultInstance;
    private static ArrayList<UserViewInfo> mThumbViewInfoList;

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

    public PicPreviewHelper setUrl(ImageView imageView, String url) {
        mThumbViewInfoList.clear();
        Rect bounds = new Rect();
        imageView.getGlobalVisibleRect(bounds);
        UserViewInfo item = new UserViewInfo(url);
        item.setBounds(bounds);
        mThumbViewInfoList.add(item);
        return defaultInstance;
    }

    public PicPreviewHelper setData(List<Rect> rects, List<String> urls) {
        mThumbViewInfoList.clear();
        for (int i = 0; i < urls.size(); i++) {
            UserViewInfo item = new UserViewInfo(urls.get(i));
            item.setBounds(rects.get(i));
            mThumbViewInfoList.add(item);
        }
        return defaultInstance;
    }

    public PicPreviewHelper setData(Rect rect, List<String> urls, int position) {
        mThumbViewInfoList.clear();
        for (int i = 0; i < urls.size(); i++) {
            UserViewInfo item = new UserViewInfo(urls.get(i));
            if (i == position) {
                item.setBounds(rect);
            }
            mThumbViewInfoList.add(item);
        }
        return defaultInstance;
    }

    public void preview(Activity activity, int currentIndex) {
        if (mThumbViewInfoList == null) {
            return;
        }
        // 打开预览界面
        GPreviewBuilder.from(activity)                     // activity实例必须
                .setData(mThumbViewInfoList)               // 集合
                .setCurrentIndex(currentIndex)             // 设置当前位置
                .setSingleFling(true)                      // 是否在黑屏区域点击返回
                .setDrag(false)                            // 是否禁用图片拖拽返回
                .setType(GPreviewBuilder.IndicatorType.Dot)// 指示器类型
                .start();                                  // 启动

    }
}
