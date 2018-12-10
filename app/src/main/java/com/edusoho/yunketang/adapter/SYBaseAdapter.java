package com.edusoho.yunketang.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.edusoho.yunketang.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.List;

public class SYBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;
    private List<?> list;

    public SYBaseAdapter() {

    }

    public void init(Context context, int layoutId, List<?> list) {
        try {
            String className = context.getApplicationContext().getPackageName() + ".BR";
            Class brClass = Class.forName(className);
            Field viewModelField = brClass.getField("viewModel");
            int resId = viewModelField.getInt(brClass);
            this.layoutId = layoutId;
            this.list = list;
            this.variableId = resId;
            inflater = LayoutInflater.from(context);
        } catch (Exception e) {
            LogUtil.e("Adapter BR ERROR:", e);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        } else {
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        dataBinding.setVariable(variableId, list.get(position));
        return dataBinding.getRoot();
    }
}