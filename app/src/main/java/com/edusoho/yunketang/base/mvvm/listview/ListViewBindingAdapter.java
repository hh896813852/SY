package com.edusoho.yunketang.base.mvvm.listview;

import android.databinding.BindingAdapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by any on 17/6/27.
 */

public class ListViewBindingAdapter {

    @BindingAdapter({"adapter"})
    public static void adapter(ListView listView, BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @BindingAdapter({"onItemClick"})
    public static void onItemClick(ListView listView, AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }
}
