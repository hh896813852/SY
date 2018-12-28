package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentQuestionTypeBinding;

@Layout(value = R.layout.fragment_question_type)
public class QuestionTypeFragment extends BaseFragment<FragmentQuestionTypeBinding> {
    private Question question;

    public ObservableField<String> questionTypeSort = new ObservableField<>(); // 题型顺序
    public ObservableField<String> questionTypeName = new ObservableField<>(); // 题型别名
    public ObservableField<String> questionPointInfo = new ObservableField<>();// 题型分数信息
    public ObservableField<String> questionExplain = new ObservableField<>();  // 题型说明

    public static QuestionTypeFragment newInstance(Question question) {
        QuestionTypeFragment fragment = new QuestionTypeFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        question = (Question) getArguments().getSerializable("question");
        questionTypeName.set(question.alias);
//        questionPointInfo.set("（每题" + question.point + "分）");
        questionExplain.set(question.explain);
        questionTypeSort.set(getSort());
    }

    /**
     * 获取题型序号
     */
    private String getSort() {
        switch (question.sort) {
            case 0:
                return "一、";
            case 1:
                return "二、";
            case 2:
                return "三、";
            case 3:
                return "四、";
            case 4:
                return "五、";
            case 5:
                return "六、";
            case 6:
                return "七、";
            case 7:
                return "八、";
            case 8:
                return "九、";
            case 9:
                return "十、";
            case 10:
                return "十一、";
            case 11:
                return "十二、";
        }
        return question.sort + "、";
    }
}
