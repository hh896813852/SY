package com.edusoho.yunketang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.ui.exercise.ChildIntegratedExercisesFragment;
import com.edusoho.yunketang.ui.exercise.ChildSingleSelectFragment;

import java.util.List;

/**
 * @author huhao on 2018/12/1.
 */
public class ChildQuestionViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Question.QuestionDetails> childQuestionList;

    public ChildQuestionViewPagerAdapter(FragmentManager fm, List<Question.QuestionDetails> childQuestionList) {
        super(fm);
        this.childQuestionList = childQuestionList;
    }

    @Override
    public Fragment getItem(int position) {
        Question.QuestionDetails childQuestion = childQuestionList.get(position);
        switch (childQuestion.childQuestionType) {
            case 1: // 单选题
                return ChildSingleSelectFragment.newInstance(childQuestion);
            case 2: // 简答题： 一段文字（含图片），一个题目（含图片），一个答案（可上传图片）
                return ChildIntegratedExercisesFragment.newInstance(childQuestion);
        }
        return null;
    }

    @Override
    public int getCount() {
        return childQuestionList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}