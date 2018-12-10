package com.edusoho.yunketang.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huhao on 2018/8/24
 */
public class BaseRecycleAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {
    protected Context context;
    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;

    protected List<T> dataList = new ArrayList<>();

    public void init(Context context, int layoutId, List<T> dataList) {
        try {
            String className = context.getApplicationContext().getPackageName() + ".BR";
            Class brClass = Class.forName(className);
            Field viewModelField = brClass.getField("viewModel");
            int resId = viewModelField.getInt(brClass);
            this.context = context;
            this.layoutId = layoutId;
            this.variableId = resId;
            inflater = LayoutInflater.from(context);
            setDataList(dataList);
        } catch (Exception e) {
            LogUtil.e("Adapter BR ERROR:", e);
        }
    }

    /**
     * 为每一个item加载布局，并将布局传递给自定义的ViewHolder。
     */
    @NonNull
    @Override
    public SuperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return new SuperViewHolder(dataBinding.getRoot());
    }

    /**
     * 建立起自定义ViewHolder中视图与数据的关联。
     */
    @Override
    public void onBindViewHolder(@NonNull SuperViewHolder holder, int position) {
        ViewDataBinding dataBinding = DataBindingUtil.getBinding(holder.item);
        dataBinding.setVariable(variableId, dataList.get(position));
        // 添加点击事件
        addOnClickListener(holder);
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
//            holder.item.setOnLongClickListener(v -> {
//                int layoutPosition = holder.getLayoutPosition();
//                onItemClickListener.onItemLongClick(holder.item, layoutPosition);
//                return false;
//            });
        }
    }

    /**
     * 对item进行计数。
     */
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 设置数据
     */
    public void setDataList(List<T> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    public void addAll(List<T> list) {
        int lastIndex = dataList.size();
        addAll(list, lastIndex);
    }

    /**
     * 添加数据
     */
    public void addAll(List<T> list, int position) {
        if (dataList.addAll(list)) {
            notifyItemRangeInserted(position, list.size());
        }
    }

    public void addData(T data) {
        int lastIndex = dataList.size();
        addData(lastIndex, data);
    }

    public void addData(int pos, T t) {
        dataList.add(pos, t);
        notifyItemInserted(pos);
    }

    public void removeData(int pos) {
        dataList.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * 改变某个数据
     */
    public void change(int position, T t) {
        dataList.remove(position);
        dataList.add(position, t);
        notifyItemChanged(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

//        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
