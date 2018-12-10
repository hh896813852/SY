package com.edusoho.yunketang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import com.edusoho.yunketang.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CropUtils {
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private static File mCropPath; // 裁剪后图片文件

    public static String getCropPath() {
        return mCropPath == null ? "" : mCropPath.getAbsolutePath();
    }

    /**
     * 调用系统照片的裁剪功能(适配Android7.0)
     */
    public static Intent invokeSystemCrop(Uri uri, float aspectX, float aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", aspectX * 250);
        intent.putExtra("outputY", aspectY * 250);
        intent.putExtra("return-data", false);
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true);

        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        mCropPath = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropPath));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 使用UCrop去裁剪
     */
    public static void startUCrop(Activity activity, Uri sourceUri, float aspectX, float aspectY) {
        // 初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        // 设置裁剪图片可操作的手势
//        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        // 设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.theme_color));
        // 设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.theme_color));
        // 是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        // 是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);

        mCropPath = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
        Uri destinationUri = Uri.fromFile(mCropPath); // 裁剪后保存到文件中
        UCrop.of(sourceUri, destinationUri) // 第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
                .withAspectRatio(aspectX, aspectY) // 设置宽高比例
                .withMaxResultSize(750, 500)
                .withOptions(options)
                .start(activity);
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 解决Android 7.0之后的Uri安全问题
     */
    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.edusoho.yunketang.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}