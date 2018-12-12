package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_judge_select)
public class JudgeSelectFragment extends BaseFragment {
    private Question question;

    public ObservableField<String> questionTopic = new ObservableField<>();

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter();

    public List<Question.QuestionDetails.Option> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 是否是第一次选择
        boolean isFirstPick = true;
        for (int i = 0; i < list.size(); i++) {
            // 有选项选过，则不是第一次选择
            if (list.get(i).isPicked) {
                isFirstPick = false;
            }
            list.get(i).isPicked = position == i;
        }
        adapter.notifyDataSetChanged();
        if (isFirstPick && getActivity() != null) {
            // 第一次选择，显示下一页
            ((ExerciseActivity) getActivity()).showNextPage();
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
        questionTopic.set(question.questionSort + "、" + question.topic);

        if(!TextUtils.isEmpty(question.topicPictureUrl)) {
            picList.addAll(Arrays.asList(question.topicPictureUrl.split(",")));
        }
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);

        list.addAll(question.details.get(0).options);
        adapter.init(getSupportedActivity(), R.layout.item_single_option, list);
    }
}
