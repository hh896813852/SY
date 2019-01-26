package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.helper.PicLoadHelper;
import com.edusoho.yunketang.helper.PicPreviewHelper;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.databinding.FragmentChildReadSelectBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_child_read_select)
public class ChildReadSelectFragment extends BaseFragment<FragmentChildReadSelectBinding> {
    private Question.QuestionDetails childQuestion;

    public ObservableField<String> questionTopic = new ObservableField<>();

    // 答案解析部分
    public ObservableField<Boolean> isShowAnswerAnalysis = new ObservableField<>(false);
    public ObservableField<Boolean> isUserAnswerCorrect = new ObservableField<>(false);
    public ObservableField<String> correctAnswer = new ObservableField<>();
    public ObservableField<String> userAnswer = new ObservableField<>();
    public ObservableField<String> answerAnalysis = new ObservableField<>();
    public List<String> answerAnalysisPicList = new ArrayList<>();

    public List<String> picList = new ArrayList<>();

    public List<Question.QuestionDetails.Option> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            // 手动添加选项内容和图片，防止ScrollView下ListView内容高度不确定的情况下被限制
            LinearLayout optionContainer = view.findViewById(R.id.optionContainer);
            optionContainer.removeAllViews();
            if (list.get(position).choiceType == 0) {
                TextView optionContent = new TextView(getSupportedActivity());
                optionContent.setText(list.get(position).optionContent);
                optionContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                optionContent.setGravity(Gravity.CENTER_VERTICAL); // 垂直居中
                optionContent.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_light_black));
                optionContainer.addView(optionContent);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) optionContent.getLayoutParams();
                params.setMargins(DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5), DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5));
                optionContent.setLayoutParams(params);
                view.setOnClickListener(v -> {
                    // 答案解析不可更改选项
                    if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                        return;
                    }
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
                        new Handler().postDelayed(() -> {
                            // 第一次选择，停留300毫秒显示下一页
                            ((ExerciseActivity) getActivity()).showNextPage();
                        }, 300);
                    }
                });
            } else {
                ImageView optionImage = new ImageView(getSupportedActivity());
                optionImage.setScaleType(ImageView.ScaleType.FIT_XY);
                optionContainer.addView(optionImage);
                PicLoadHelper.load_16_10(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 110), list.get(position).optionPicUrl, optionImage);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) optionImage.getLayoutParams();
                params.setMargins(DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5), 0, DensityUtil.dip2px(getSupportedActivity(), 5));
                optionImage.setLayoutParams(params);
                optionImage.setOnClickListener(v -> {
                    PicPreviewHelper.getInstance().setUrl(optionImage, list.get(position).optionPicUrl).preview(getSupportedActivity(), 0);
                });
            }
            // 选项背景
            TextView optionView = view.findViewById(R.id.optionView);
            if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                if (list.get(position).isPicked) {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), isUserAnswerCorrect.get() ? R.drawable.shape_oval_bg_theme_color : R.drawable.shape_oval_bg_red));
                } else {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), R.drawable.bg_white_stroke_gray_corner_16));
                }
            } else {
                optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), list.get(position).isPicked ? R.drawable.shape_oval_bg_orange : R.drawable.bg_white_stroke_gray_corner_16));
            }
            // 选项点击
            FrameLayout optionLayout = view.findViewById(R.id.optionLayout);
            optionLayout.setOnClickListener(v -> {
                // 答案解析不可更改选项
                if (getActivity() == null || ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                    return;
                }
//                // 是否是第一次选择
//                boolean isFirstPick = true;
//                for (int i = 0; i < list.size(); i++) {
//                    // 有选项选过，则不是第一次选择
//                    if (list.get(i).isPicked) {
//                        isFirstPick = false;
//                    }
//                    list.get(i).isPicked = position == i;
//                }
                list.get(position).isPicked = !list.get(position).isPicked;
                adapter.notifyDataSetChanged();
//                if (isFirstPick && getParentFragment() != null) {
//                    // 第一次选择，显示下一页
//                    ((ReadSelectedFragment) getParentFragment()).showNextPage();
//                }
            });
            return view;
        }
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
        // 题目序号 + 题目
        questionTopic.set(childQuestion.childQuestionSort + "、" + childQuestion.topicSubsidiary);
        // 题目图片adapter
        if (!TextUtils.isEmpty(childQuestion.topicSubsidiaryUrl)) {
            getDataBinding().topicPicContainer.setVisibility(View.VISIBLE);
            picList.addAll(Arrays.asList(childQuestion.topicSubsidiaryUrl.split(",")));
            for (int i = 0; i < picList.size(); i++) {
                View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                ImageView imageView = innerView.findViewById(R.id.imageView);
                imageView.setTag(String.valueOf(i));
                PicLoadHelper.load(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 20), picList.get(i), imageView);
                imageView.setOnClickListener(v -> {
                    int position = Integer.valueOf(imageView.getTag().toString());
                    PicPreviewHelper.getInstance().setUrl(imageView, picList.get(position)).preview(getSupportedActivity(), 0);
                });
                getDataBinding().topicPicContainer.addView(innerView);
            }
        } else {
            getDataBinding().topicPicContainer.setVisibility(View.GONE);
        }

        // 是否显示答案解析
        isShowAnswerAnalysis.set(getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis);
        // 如果显示答案解析
        if (isShowAnswerAnalysis.get()) {
            // 正确答案
            correctAnswer.set(childQuestion.getCorrectResult());
            // 如果用户作答了
            if (getParentFragment() != null && !TextUtils.isEmpty(((ReadSelectedFragment) getParentFragment()).question.userResult)) {
                // 解析用户答案
                List<MyAnswer> userAnswerList = JsonUtil.fromJson(((ReadSelectedFragment) getParentFragment()).question.userResult, new TypeToken<List<MyAnswer>>() {
                });
                // 用户答案
                if (!TextUtils.isEmpty(userAnswerList.get(childQuestion.childQuestionSort - 1).result)) {
                    userAnswer.set(childQuestion.getUserResult(userAnswerList.get(childQuestion.childQuestionSort - 1).result));
                } else {
                    userAnswer.set("未作答");
                }
            } else {
                userAnswer.set("未作答");
            }
            // 用户答案是否正确
            isUserAnswerCorrect.set(TextUtils.equals(userAnswer.get(), correctAnswer.get()));
            // 答案解析
            answerAnalysis.set(childQuestion.resultResolve);
            // 答案解析图片
            String resultResolveUrl = childQuestion.resultResolveUrl;
            if (!TextUtils.isEmpty(resultResolveUrl)) {
                answerAnalysisPicList.addAll(Arrays.asList(resultResolveUrl.split(",")));
                for (String url : answerAnalysisPicList) {
                    View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                    ImageView imageView = innerView.findViewById(R.id.imageView);
                    PicLoadHelper.load(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 20), url, imageView);
                    getDataBinding().answerAnalysisPicContainer.addView(innerView);
                }
            }
        }
        // 选项adapter
        list.addAll(childQuestion.options);
        adapter.init(getSupportedActivity(), R.layout.item_single_option, list);
    }
}
