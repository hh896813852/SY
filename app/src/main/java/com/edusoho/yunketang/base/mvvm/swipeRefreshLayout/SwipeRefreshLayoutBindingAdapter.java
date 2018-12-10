package com.edusoho.yunketang.base.mvvm.swipeRefreshLayout;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.edusoho.yunketang.R;

/**
 * Created by Administrator on 2017/8/10.
 */

public class SwipeRefreshLayoutBindingAdapter {

    @BindingAdapter({"onRefreshListener"})
    public static void onRefreshListener(SwipeRefreshLayout swipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    @BindingAdapter({"refreshing"})
    public static void refreshing(SwipeRefreshLayout swipeRefreshLayout, Boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
