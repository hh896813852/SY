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

    /**
     * 按原图比例和最大宽高比例和最大宽度，改变图片的高度
     *
     * @param maxWidth 最大宽度
     */
    public static void load_16_10(Context context, int maxWidth, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.bg_load_default_4x3).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();  // 图片宽度
                int imageHeight = resource.getHeight();// 图片高度
                int maxHeight = maxWidth * 10 / 16;    // 最大高度
                // 图片最终结果宽度
                int resultWidth;
                // 图片最终结果高度
                int resultHeight;
                // 如果图片宽高均小于等于其最大限制
                if (imageWidth <= maxWidth && imageHeight <= maxHeight) {
                    resultWidth = imageWidth;
                    resultHeight = imageHeight;
                } else {
                    // 把图片的宽度设为最大宽度,得到图片变化后的高度
                    resultHeight = maxWidth * imageHeight / imageWidth;
                    // 如果图片变化后的高度大于最大高度，设置图片最大高度为 maxHeight
                    if (resultHeight > maxHeight) {
                        resultHeight = maxHeight;
                        resultWidth = imageWidth * maxHeight / imageHeight;
                    } else {
                        resultWidth = maxWidth;
                    }
                }
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = resultHeight;
                params.width = resultWidth;
                imageView.setImageBitmap(resource);
            }
        });
    }
}
