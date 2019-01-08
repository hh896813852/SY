package com.edusoho.yunketang.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.utils.ScreenUtil;

public class PicLoadHelper {

    /**
     * 按原图比例，根据屏幕宽度，改变图片的高度
     */
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.bg_load_default_4x3).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtil.getScreenWidth(context) * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = height;
                para.width = ScreenUtil.getScreenWidth(context);
                imageView.setImageBitmap(resource);
            }
        });
    }

    /**
     * 按原图比例，根据宽度，改变图片的高度
     */
    public static void load(Context context, int width, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.bg_load_default_4x3).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtil.getScreenWidth(context) * imageHeight / imageWidth;
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = height;
                params.width = ScreenUtil.getScreenWidth(context);
                imageView.setImageBitmap(resource);
            }
        });
    }

    /**
     * 按原图比例，根据宽度，改变图片的高度
     */
    public static void load(Context context, int width, int resourceId, ImageView imageView) {
        Glide.with(context).load(resourceId).asBitmap().placeholder(R.drawable.bg_load_default_4x3).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = width * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.width = width;
                para.height = height;
                imageView.setImageBitmap(resource);
            }
        });
    }
}
