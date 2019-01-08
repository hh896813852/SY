package com.edusoho.yunketang.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class LuBanUtil {
    private static File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/edu/compress");
    private static List<File> compressPathList; // 压缩后的图片文件集合
    private static int pathIndex = 0;           // 图片path下标

    public static void luBanCompress(Context context, List<String> pathList, OnImageCompressListener onImageCompressListener) {
        compressPathList = new ArrayList<>();
        Luban.with(context)
                .load(pathList)
                .ignoreBy(100) // 当原始图像文件大小小于100KB时不压缩
                .setTargetDir(getCompressPath())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))) // 过滤空path和gif图
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        pathIndex++;
                        // 压缩成功后调用，返回压缩后的图片文件
                        compressPathList.add(file);
                        if(pathIndex == pathList.size()) {
                            onImageCompressListener.OnImageCompressSuccess(compressPathList);
                            pathIndex = 0;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用
                        pathIndex++;
                        if(pathIndex == pathList.size()) {
                            onImageCompressListener.OnImageCompressSuccess(compressPathList);
                            pathIndex = 0;
                        }
                    }
                }).launch();
    }

    private static String getCompressPath() {
        PHOTO_DIR.mkdirs();
        return PHOTO_DIR.getAbsolutePath();
    }

    public interface OnImageCompressListener {
        void OnImageCompressSuccess(List<File> compressFiles);
    }
}
