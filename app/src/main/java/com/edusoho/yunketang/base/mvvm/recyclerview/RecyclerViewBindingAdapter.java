package com.edusoho.yunketang.base.mvvm.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.edusoho.yunketang.adapter.BaseRecycleAdapter;

public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"adapter", "onItemClick"}, requireAll = false)
    public static void adapter(RecyclerView recyclerView, BaseRecycleAdapter adapter, BaseRecycleAdapter.OnItemClickListener listener) {
        recyclerView.setAdapter(adapter);
        if (listener != null) {
            adapter.setOnItemClickListener(listener);
        }
    }
}
