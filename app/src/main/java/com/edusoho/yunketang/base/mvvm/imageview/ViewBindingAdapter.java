package com.edusoho.yunketang.base.mvvm.imageview;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/8/9.
 */
public class ViewBindingAdapter {

    @BindingAdapter({"url"})
    public static void url(ImageView imageView, String url) {
        if (url != null) {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }

}
