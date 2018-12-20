package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_child_read_select)
public class ChildReadSelectFragment extends BaseFragment {
    private Question.QuestionDetails childQuestion;

    public ObservableField<String> questionTopic = new ObservableField<>();

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView imageView = view.findViewById(R.id.imageView);
            Glide.with(getSupportedActivity()).load(picList.get(position)).placeholder(R.drawable.bg_load_default_4x3).into(imageView);
            return view;
        }
    };

    public List<Question.QuestionDetails.Option> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 是否是第一次选择 TODO 注释掉是因为阅读选择可以为多选，所以取消自动下一题功能
//        boolean isFirstPick = true;
//        for (int i = 0; i < list.size(); i++) {
            // 有选项选过，则不是第一次选择
//            if (list.get(i).isPicked) {
//                isFirstPick = false;
//            }
//            list.get(i).isPicked = position == i;
//        }
        list.get(position).isPicked = !list.get(position).isPicked;
        adapter.notifyDataSetChanged();
//        if (isFirstPick && getParentFragment() != null) {
//            // 第一次选择，显示下一页
//            ((ReadSelectedFragment)getParentFragment()).showNextPage();
//        }
    };

    public static ChildReadSelectFragment newInstance(Question.QuestionDetails childQuestion) {
        ChildReadSelectFragment fragment = new ChildReadSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("childQuestion", childQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        childQuestion = (Question.QuestionDetails) getArguments().getSerializable("childQuestion");
        initView();
    }

    private void initView() {
        questionTopic.set(childQuestion.childQuestionSort + "、" + childQuestion.topicSubsidiary);

        if(!TextUtils.isEmpty(childQuestion.topicSubsidiaryUrl)) {
            picList.addAll(Arrays.asList(childQuestion.topicSubsidiaryUrl.split(",")));
        }
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);

        list.addAll(childQuestion.options);
        adapter.init(getSupportedActivity(), R.layout.item_single_option, list);
    }
}
