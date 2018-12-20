package com.edusoho.yunketang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.ui.exercise.IntegratedExercisesFragment;
import com.edusoho.yunketang.ui.exercise.JudgeSelectFragment;
import com.edusoho.yunketang.ui.exercise.ListenSelectFragment;
import com.edusoho.yunketang.ui.exercise.MultipleSelectFragment;
import com.edusoho.yunketang.ui.exercise.QuestionTypeFragment;
import com.edusoho.yunketang.ui.exercise.ReadSelectedFragment;
import com.edusoho.yunketang.ui.exercise.SingleSelectFragment;

import java.util.List;

/**
 * @author huhao on 2018/12/1.
 */
public class QuestionViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Question> questionList;

    public QuestionViewPagerAdapter(FragmentManager fm, List<Question> questionList) {
        super(fm);
        this.questionList = questionList;
    }

    @Override
    public Fragment getItem(int position) {
        Question question = questionList.get(position);
        switch (question.questionType) {
            case 1: // 单选题
                return SingleSelectFragment.newInstance(question);
            case 2: // 多选题
                return MultipleSelectFragment.newInstance(question);
            case 3: // 阅读选择题：子题选项可能是多选
                return ReadSelectedFragment.newInstance(question);
            case 4: // 听力选择题
                return ListenSelectFragment.newInstance(question);
            case 5: // 判断选择题
                return JudgeSelectFragment.newInstance(question);
            case 6: // 简答题： 一段文字（含图片），一个题目（含图片），一个答案（可上传图片）
                return IntegratedExercisesFragment.newInstance(question);
            case 7: // 综合题： 一段文字（含图片），多个题目（含图片），每个题目一个答案（可上传图片）
                return IntegratedExercisesFragment.newInstance(question);
        }
        // 题干
        return QuestionTypeFragment.newInstance(question);
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}