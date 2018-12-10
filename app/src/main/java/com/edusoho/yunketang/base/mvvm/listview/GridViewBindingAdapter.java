package com.edusoho.yunketang.base.mvvm.listview;

import android.databinding.BindingAdapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by any on 17/6/27.
 */

public class GridViewBindingAdapter {

    @BindingAdapter({"adapter"})
    public static void adapter(GridView gridView, BaseAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    @BindingAdapter({"onItemClick"})
    public static void onItemClick(GridView gridView, AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }
}
