package com.edusoho.yunketang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.bean.Course;

import java.util.List;

/**
 * Created by zy on 2018/11/8 0008.
 */

public class FirstCourseAdapter extends RecyclerView.Adapter<SuperViewHolder> {
    private Context context;
    private List<Course.Discovery> list;

    public FirstCourseAdapter(Context context, List<Course.Discovery> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SuperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final SuperViewHolder holder = new SuperViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_course, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuperViewHolder holder, int position) {
        Course.Discovery discovery = list.get(position);
        Glide.with(context).load(discovery.cover.large).placeholder(R.drawable.bg_load_default_4x3).into((ImageView) holder.getView(R.id.coverImage));
        ((TextView)holder.getView(R.id.titleView)).setText(discovery.title);
        ((TextView)holder.getView(R.id.priceView)).setText(discovery.price > 0 ? discovery.price2.getPriceWithUnit() : "免费");
        ((TextView)holder.getView(R.id.personCountView)).setText(discovery.studentNum + "人参与");
        // 添加点击事件
        addOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 添加点击事件
     */
    private void addOnClickListener(SuperViewHolder holder) {
        if (onItemClickListener != null) {
            holder.item.setOnClickListener(v -> {
                int layoutPosition = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.item, layoutPosition);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
