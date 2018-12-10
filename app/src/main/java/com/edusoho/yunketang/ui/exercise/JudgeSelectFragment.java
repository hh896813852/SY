package com.edusoho.yunketang.ui.exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_judge_select)
public class JudgeSelectFragment extends BaseFragment {
    private Question question;

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter();

    public List<String> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            // 选项点击
            view.findViewById(R.id.optionView).setOnClickListener(v -> {

            });
            return view;
        }
    };

    public static JudgeSelectFragment newInstance(Question question) {
        JudgeSelectFragment fragment = new JudgeSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        question = (Question) getArguments().getSerializable("question");
        initView();
    }

    private void initView() {
        picList.add("");
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);

        list.add("");
        list.add("");
        adapter.init(getSupportedActivity(), R.layout.item_single_option, list);
    }
}
