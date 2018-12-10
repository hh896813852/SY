package com.edusoho.yunketang.ui.classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_coursework, title = "课程作业")
public class CourseworkActivity extends BaseActivity {

    private List<String> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.findViewById(R.id.startView).setOnClickListener(v -> {
                Intent intent = new Intent(CourseworkActivity.this, ExerciseActivity.class);
                startActivity(intent);
            });
            return view;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_work, list);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        for (int i = 0; i < 8; i++) {
            list.add(i + "");
        }
        adapter.notifyDataSetChanged();
    }
}
