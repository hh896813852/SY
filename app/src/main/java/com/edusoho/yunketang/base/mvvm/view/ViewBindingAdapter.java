package com.edusoho.yunketang.base.mvvm.view;

import android.databinding.BindingAdapter;
import android.view.View;


/**
 * Created by Administrator on 2017/8/9.
 */

public class ViewBindingAdapter {

    public ViewBindingAdapter() {

    }

    @BindingAdapter({"clickCommand"})
    public static void clickCommand(View view, View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
    }

}
