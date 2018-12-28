package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_multiple_select)
public class MultipleSelectFragment extends BaseFragment {
    private Question question;

    public ObservableField<String> questionTopic = new ObservableField<>();

    // 答案解析部分
    public ObservableField<Boolean> isShowAnswerAnalysis = new ObservableField<>(false);
    public ObservableField<Boolean> isUserAnswerCorrect = new ObservableField<>(false);
    public ObservableField<String> correctAnswer = new ObservableField<>();
    public ObservableField<String> userAnswer = new ObservableField<>();
    public ObservableField<String> answerAnalysis = new ObservableField<>();
    public List<String> answerAnalysisPicList = new ArrayList<>();
    public SYBaseAdapter answerAnalysisPicAdapter = new SYBaseAdapter();

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
    public SYBaseAdapter adapter = new SYBaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.findViewById(R.id.optionImage).setVisibility(list.get(position).choiceType == 1 ? View.VISIBLE : View.GONE);
            TextView optionView = view.findViewById(R.id.optionView);
            if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                if (list.get(position).isPicked) {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), isUserAnswerCorrect.get() ? R.drawable.bg_theme_color_corner_4 : R.drawable.bg_red_corner_4));
                } else {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), R.drawable.bg_white_stroke_dark_gray_corner_4));
                }
            } else {
                optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), list.get(position).isPicked ? R.drawable.bg_orange_corner_4 : R.drawable.bg_white_stroke_dark_gray_corner_4));
            }
            return view;
        }
    };
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 答案解析不可更改选项
        if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
            return;
        }
        list.get(position).isPicked = !list.get(position).isPicked;
        adapter.notifyDataSetChanged();
    };

    public static MultipleSelectFragment newInstance(Question question) {
        MultipleSelectFragment fragment = new MultipleSelectFragment();
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
        // 题目序号 + 题目
        questionTopic.set(question.questionSort + "、" + question.topic);
        // 题目图片adapter
        if (!TextUtils.isEmpty(question.topicPictureUrl)) {
            picList.addAll(Arrays.asList(question.topicPictureUrl.split(",")));
        }
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);
        // 是否显示答案解析
        isShowAnswerAnalysis.set(getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis);
        // 如果显示答案解析
        if (isShowAnswerAnalysis.get()) {
            // 正确答案
            correctAnswer.set(question.details.get(0).getCorrectResult());
            // 如果用户作答了
            if (!TextUtils.isEmpty(question.userResult)) {
                // 解析用户答案
                List<MyAnswer> userAnswerList = JsonUtil.fromJson(question.userResult, new TypeToken<List<MyAnswer>>() {
                });
                // 用户答案
                if (!TextUtils.isEmpty(userAnswerList.get(0).result)) {
                    userAnswer.set(question.details.get(0).getUserResult(userAnswerList.get(0).result));
                } else {
                    userAnswer.set("未作答");
                }
            } else {
                userAnswer.set("未作答");
            }
            // 用户答案是否正确
            isUserAnswerCorrect.set(TextUtils.equals(userAnswer.get(), correctAnswer.get()));
            // 答案解析
            answerAnalysis.set(question.details.get(0).resultResolve);
            // 答案解析图片adapter
            answerAnalysisPicAdapter.init(getSupportedActivity(), R.layout.item_pic, answerAnalysisPicList);
            // 答案解析图片
            String resultResolveUrl = question.details.get(0).resultResolveUrl;
            if (!TextUtils.isEmpty(resultResolveUrl)) {
                answerAnalysisPicList.addAll(Arrays.asList(resultResolveUrl.split(",")));
                answerAnalysisPicAdapter.notifyDataSetChanged();
            }
        }
        // 选项adapter
        list.addAll(question.details.get(0).options);
        adapter.init(getSupportedActivity(), R.layout.item_multiple_option, list);
    }
}
