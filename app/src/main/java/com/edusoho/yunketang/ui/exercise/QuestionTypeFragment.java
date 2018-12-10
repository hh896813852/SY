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

    public ObservableField<String> questionTypeName = new ObservableField<>();
    public ObservableField<String> questionCount = new ObservableField<>();

    public static QuestionTypeFragment newInstance(Question question) {
        QuestionTypeFragment fragment = new QuestionTypeFragment();
        Bundle args = new Bundle();
        args.putSerializable("question",question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        question = (Question) getArguments().getSerializable("question");
        questionTypeName.set(question.questionTypeName);
        questionCount.set(String.valueOf(question.questionType));
    }
}
