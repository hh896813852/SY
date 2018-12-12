package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.QuestionViewPagerAdapter;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.ActivityExerciseBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.BitmapUtil;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.edusoho.yunketang.widget.AnswerResultLayout;
import com.edusoho.yunketang.widget.SimpleDialog;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_exercise)
public class ExerciseActivity extends BaseActivity<ActivityExerciseBinding> {
    public static final String EXAMINATION_ID = "examination_id";
    public static final String MODULE_ID = "module_id";
    public static final String SELECTED_COURSE = "selected_course";
    public static final String CLASS_ID = "class_id";
    public String examinationId;
    private int moduleId;
    private EducationCourse selectedCourse;
    private String classId;

    private static final String ADD = "+";
    private static final String SUBTRACT = "-";
    private static final String MULTIPLY = "×";
    private static final String DIVIDE = "÷";

    public ObservableField<Boolean> isQuestionCollected = new ObservableField<>(false);
    public ObservableField<Boolean> isCalculatorShowed = new ObservableField<>(false);
    public ObservableField<Boolean> isAnswerCardShowed = new ObservableField<>(false);
    public ObservableField<Boolean> isAnswerAnalysisShowed = new ObservableField<>(false);
    public ObservableField<String> inputText = new ObservableField<>("0");

    public ObservableField<String> questionTypeName = new ObservableField<>(); // 题型别名
    public ObservableField<String> questionTypeInfo = new ObservableField<>(); // 题型信息

    private List<Question> questionStem = new ArrayList<>(); // 题干集合
    private Question currentLoadQuestionStem;     // 当前加载题目的题干
    private int currentLoadQuestionStemIndex = 0; // 当前加载题目的题干的下标
    private String[] currentLoadQuestionIds;    // 当前加载题目的题干的题目id数组
    private String currentLoadQuestionId;       // 当前加载题目的题干的题目id
    private int currentLoadQuestionIdIndex = 0; // 当前加载题目的题干的题目id的下标
    private String currentLoadQuestionStemSum;  // 当前加载题型的题目总数

    private StringBuilder inputOne = new StringBuilder("0"); // 计算器输入的第一个数字
    private StringBuilder inputTwo = new StringBuilder();    // 计算器输入的第二个数字
    private String operateSymbol = ""; // 当前操作字符 + -×÷（正负号不算在内）
    private boolean isResetInputOne;   // 是否重置第一个数，计算出结果后，如果点数字，则重新生成inputOne，否则inputOne为刚计算的结果

    private List<Question> questionList = new ArrayList<>(); // 试卷题目集合（包含题干）
    private QuestionViewPagerAdapter viewPagerAdapter;       // 试卷adapter

    private List<Question> preCommitQuestionList = new ArrayList<>(); // 预提交问题集合（包含未作答问题，不包含题干）
    private List<Question> canCommitQuestionList = new ArrayList<>(); // 可提交问题集合（不包含题干）

    public List<List<Integer>> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView questionTypeView = view.findViewById(R.id.questionTypeView);
            questionTypeView.setText(questionStem.get(position).getQuestionTypeName());
            AnswerResultLayout answerResultLayout = view.findViewById(R.id.answerResultLayout);
            answerResultLayout.setTags(list.get(position));
            return view;
        }
    };

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        examinationId = getIntent().getStringExtra(EXAMINATION_ID);
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
        classId = getIntent().getStringExtra(CLASS_ID);
        initView();
        loadQuestionStem();
    }

    private void initView() {
        // 答题卡adapter
        adapter.init(this, R.layout.item_answer_report, list);
        // 答案解析图片adapter
        picAdapter.init(this, R.layout.item_pic, picList);
        // 题目pagerAdapter
        viewPagerAdapter = new QuestionViewPagerAdapter(getSupportFragmentManager(), questionList);
        getDataBinding().viewPager.setOffscreenPageLimit(2);
        getDataBinding().viewPager.setAdapter(viewPagerAdapter);
        getDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 检查"预提交问题集合"里的问题，并提交已作答
                checkPreCommitQuestion();
                // 如果不是题干，添加该问题到"预提交问题集合"
                if (questionList.get(position).questionType > 0) {
                    preCommitQuestionList.add(questionList.get(position));
                }
                // 刷新界面
                refreshView(questionList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 答案解析图片
        for (int i = 0; i < 3; i++) {
            picList.add(i + "");
        }
        picAdapter.notifyDataSetChanged();
    }

    /**
     * 检查"预提交问题集合"里的问题
     */
    private void checkPreCommitQuestion() {
        // 清空可提交问题集合
        canCommitQuestionList.clear();
        for (Question question : preCommitQuestionList) {
            // 如果已作答，即可提交
            if (checkAnswer(question)) {
                canCommitQuestionList.add(question);
            }
        }
        if (canCommitQuestionList.size() > 0) {
            // 提交"可提交问题集合"里的问题
            commitQuestionAnswer();
        }
    }

    /**
     * 提交"可提交问题集合"里的问题
     */
    private void commitQuestionAnswer() {
        for (Question question : canCommitQuestionList) {
            SYDataTransport dataTransport = SYDataTransport.create(SYConstants.QUESTION_COMMIT);
            dataTransport.addParam("examinationId", examinationId)
                    .addParam("userId", getLoginUser().syjyUser.id)
                    .addParam("businessType", selectedCourse.businessId)
                    .addParam("levelId", selectedCourse.levelId)
                    .addParam("courseId", selectedCourse.courseId)
                    .addParam("questionType", question.questionType)
                    .addParam("homeworkType", TextUtils.isEmpty(classId) ? 1 : 0) // 0：班级作业，1：模块练习
                    .addParam("questionIndex", question.questionSort)
                    .addParam("questionId", question.questionId);
            if (!TextUtils.isEmpty(classId)) {
                dataTransport.addParam("classId", classId);
            }
            if (moduleId != 0) {
                dataTransport.addParam("moduleId", moduleId);
            }
            dataTransport.addParam("correctResult", getAnswerList(question))
                    .execute(new SYDataListener() {

                        @Override
                        public void onSuccess(Object data) {
                            // 提交成功，则删除"预提交问题集合"中的问题
                            preCommitQuestionList.remove(question);
                        }

                        @Override
                        public void onFail(int status, String failMessage) {
                            super.onFail(status, failMessage);
                        }
                    });
        }
    }

    /**
     * 获取该问题的作答选项或内容
     */
    private ArrayList getAnswerList(Question question) {
        ArrayList<MyAnswer> answerList = new ArrayList<>();
        for (Question.QuestionDetails details : question.details) {
            MyAnswer mAnswer = new MyAnswer();
            if (question.questionType < 6) {
                for (int i = 0; i < details.options.size(); i++) {
                    if (details.options.get(i).isPicked) {
                        mAnswer.result = i + ",";
                    }
                }
                mAnswer.result = mAnswer.result.substring(0, mAnswer.result.length() - 1);
            }
            answerList.add(mAnswer);
        }
        return answerList;
    }

    /**
     * 通过下标显示对应的page
     */
    public void showPageByPosition(int position) {
        getDataBinding().viewPager.setCurrentItem(position);
    }

    /**
     * 显示下一页
     */
    public void showNextPage() {
        if (getDataBinding().viewPager.getCurrentItem() + 1 < viewPagerAdapter.getCount()) {
            getDataBinding().viewPager.setCurrentItem(getDataBinding().viewPager.getCurrentItem() + 1, true);
        }
    }

    /**
     * 加载题干
     */
    private void loadQuestionStem() {
        SYDataTransport.create(SYConstants.QUESTION_STEM_QUERY)
                .addParam("examinationId", examinationId)
                .execute(new SYDataListener<List<Question>>() {

                    @Override
                    public void onSuccess(List<Question> data) {
                        questionStem.addAll(data);
                        if (questionStem.size() > 0) {
                            // 获取当前题干（第一个题干）
                            currentLoadQuestionStem = questionStem.get(currentLoadQuestionStemIndex);
                            // 获取当前题型的题目总数
                            currentLoadQuestionStemSum = currentLoadQuestionStem.sum;
                            // 将题干添加到题目集合
                            questionList.add(currentLoadQuestionStem);
                            // 改题型下有题目
                            if (currentLoadQuestionStem.sids.length() > 0) {
                                // 获取当前题干下的题目id数组
                                currentLoadQuestionIds = currentLoadQuestionStem.sids.split(",");
                                // 获取第一道题目的id
                                currentLoadQuestionId = currentLoadQuestionIds[0];
                                // 加载当前题型的题目
                                loadQuestion();
                                // 刷新界面
                                refreshView(questionList.get(0));
                            }
                        }
                    }
                }, new TypeToken<List<Question>>() {
                });
    }

    /**
     * 加载题目
     */
    private void loadQuestion() {
        SYDataTransport.create(SYConstants.QUESTION_QUERY)
                .addParam("examinationId", examinationId)
                .addParam("questionType", currentLoadQuestionStem.type)
                .addParam("questionId", currentLoadQuestionId)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<Question>>() {

                    @Override
                    public void onSuccess(List<Question> data) {
                        // 遍历赋值题目序号、题目总数、题目选项
                        for (int i = 0; i < data.size(); i++) {
                            Question question = data.get(i);
                            question.questionSort = currentLoadQuestionIdIndex + i + 1;
                            question.sum = currentLoadQuestionStemSum;
                            for (Question.QuestionDetails details : question.details) {
                                details.options = details.getChoiceList();
                            }
                            questionList.add(question);
                        }
                        // 如果加载的数据最后一条id等于该题型最后一条数据的id，则该题型问题全部加载完毕
                        if (data.size() > 0 && data.get(data.size() - 1).questionId.equals(currentLoadQuestionIds[currentLoadQuestionIds.length - 1])) {
                            // 重置题目id的下标
                            currentLoadQuestionIdIndex = 0;
                            // 如果还存在下一题型，则加载下一题型的问题
                            if (currentLoadQuestionStemIndex < questionStem.size() - 1) {
                                // 题干集合下标+1
                                currentLoadQuestionStemIndex++;
                                // 获取下一题型
                                currentLoadQuestionStem = questionStem.get(currentLoadQuestionStemIndex);
                                // 获取下一题型的题目总数
                                currentLoadQuestionStemSum = currentLoadQuestionStem.sum;
                                // 题目集合添加下一题题干
                                questionList.add(currentLoadQuestionStem);
                                // 获取下一题型的题目数组
                                currentLoadQuestionIds = currentLoadQuestionStem.sids.split(",");
                                // 获取下一题型的第一道题目id
                                currentLoadQuestionId = currentLoadQuestionIds[0];
                                // 加载下一题型的题目
                                loadQuestion();
                            }
                        } else { // 该题型问题并未全部加载完毕，则继续加载
                            // 获取下次加载题目的第一个id下标
                            currentLoadQuestionIdIndex = currentLoadQuestionIdIndex + SYConstants.PAGE_SIZE;
                            // 获取下次加载题目的第一个id
                            currentLoadQuestionId = currentLoadQuestionIds[currentLoadQuestionIdIndex];
                            // 继续加载题目
                            loadQuestion();
                        }
                        // 刷新ViewPager
                        viewPagerAdapter.notifyDataSetChanged();
                    }
                }, new TypeToken<List<Question>>() {
                });
    }

    /**
     * 刷新界面
     */
    private void refreshView(Question question) {
        questionTypeName.set(question.getQuestionTypeName());
        questionTypeInfo.set(question.questionType == 0 ? "共" + question.sum + "题" : question.questionSort + "/" + question.sum);
    }

    @Override
    public void onBackButtonClick(View view) {
        DialogUtil.showSimpleAnimDialog(this, "您的题目还没有交卷，确定要退出吗？", "交卷", "退出", new SimpleDialog.OnSimpleClickListener() {
            @Override
            public void OnLeftBtnClicked(SimpleDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnRightBtnClicked(SimpleDialog dialog) {
                dialog.superDismiss(); // 无动画退出
            }
        });
    }

    /**
     * 显示计算器
     */
    public void onShowCalculatorClick(View view) {
        if (!isCalculatorShowed.get()) {
            isCalculatorShowed.set(true);
            getDataBinding().bgLayout.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
            getDataBinding().calculateLayout.startAnimation(animation);
        } else {
            onBgClick(null);
        }
    }

    /**
     * 显示答题卡
     */
    public void onShowAnswerCardClick(View view) {
        // 刷新答题卡数据
        refreshAnswerCardData();
        // 背景模糊
        Bitmap blurBitmap = BitmapUtil.blurBitmap(this, ScreenUtil.shotActivity(this), 15);
        getDataBinding().blurBgImage.setImageBitmap(blurBitmap);
        isAnswerCardShowed.set(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
        getDataBinding().answerCardLayout.startAnimation(animation);
    }

    /**
     * 检查是否作答
     */
    private boolean checkAnswer(Question question) {
        if (question.questionType > 0 && question.questionType < 6) { // 单 多 阅读 听力 判断
            // 遍历子题
            for (Question.QuestionDetails details : question.details) {
                // 遍历子题选项
                for (Question.QuestionDetails.Option option : details.options) {
                    // 只要有一个小题做出选择，就可提交
                    if (option.isPicked) {
                        return true;
                    }
                }
            }
        } else if (question.questionType == 6 || question.questionType == 7) { // 简答题 综合题
            // 遍历子题
            for (Question.QuestionDetails details : question.details) {
                // 已作答
                if (!TextUtils.isEmpty(details.myAnswerContent) || !TextUtils.isEmpty(details.myAnswerPicUrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 刷新答题卡数据
     */
    private void refreshAnswerCardData() {
        list.clear();
        for (Question q : questionStem) {
            List<Integer> child = new ArrayList<>();
            for (Question question : questionList) {
                if (question.questionType == q.type) {
                    if (question.questionType > 0 && question.questionType < 6) { // 单 多 阅读 听力 判断
                        int answerCount = 0;
                        // 遍历子题
                        for (Question.QuestionDetails details : question.details) {
                            // 遍历子题选项
                            for (Question.QuestionDetails.Option option : details.options) {
                                // 已选
                                if (option.isPicked) {
                                    answerCount++;
                                    break;
                                }
                            }
                        }
                        //　全部小题均已作答，改题才视为已作答
                        child.add(answerCount == question.details.size() ? AnswerResultLayout.ANSWERED : AnswerResultLayout.NOT_ANSWER);
                    } else if (question.questionType == 6 || question.questionType == 7) { // 简答题 综合题
                        int answerCount = 0;
                        // 遍历子题
                        for (Question.QuestionDetails details : question.details) {
                            // 已作答
                            if (!TextUtils.isEmpty(details.myAnswerContent) || !TextUtils.isEmpty(details.myAnswerPicUrl)) {
                                answerCount++;
                            }
                        }
                        //　全部小题均已作答，改题才视为已作答
                        child.add(answerCount == question.details.size() ? AnswerResultLayout.ANSWERED : AnswerResultLayout.NOT_ANSWER);
                    }
                }
            }
            list.add(child);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 解析
     */
    public void onAnalysisClick(View view) {
        // 背景模糊
        Bitmap blurBitmap = BitmapUtil.blurBitmap(this, ScreenUtil.shotActivity(this), 15);
        getDataBinding().blurBgImage.setImageBitmap(blurBitmap);
        isAnswerAnalysisShowed.set(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
        getDataBinding().answerCardLayout.startAnimation(animation);
    }

    /**
     * 清空
     */
    public void onClearClick(View view) {
        inputOne.delete(0, inputOne.length());
        inputOne.append("0");
        inputTwo.delete(0, inputTwo.length());
        operateSymbol = "";
        inputText.set("0");
    }

    /**
     * 正负号 +/-
     */
    public void onSignClick(View view) {
        isResetInputOne = false;
        if (TextUtils.isEmpty(operateSymbol)) { // 没有操作符，说明针对第一个数字编辑
            if (TextUtils.isEmpty(inputOne)) {
                inputOne.append("0");
            }
            if (new BigDecimal(inputOne.toString()).compareTo(new BigDecimal(0)) > 0) {
                inputOne.insert(0, "-"); // 添加负号
            } else if (new BigDecimal(inputOne.toString()).compareTo(new BigDecimal(0)) < 0) {
                inputOne.deleteCharAt(0); // 删除负号
            }
        } else { // 针对第二个数字编辑
            if (TextUtils.isEmpty(inputTwo)) {
                inputTwo.append("-");
            } else if (TextUtils.equals(inputTwo, "-")) {
                inputTwo.deleteCharAt(0);
            } else if (new BigDecimal(inputTwo.toString()).compareTo(new BigDecimal(0)) > 0) {
                inputTwo.insert(0, "-");// 添加负号
            } else if (new BigDecimal(inputTwo.toString()).compareTo(new BigDecimal(0)) < 0) {
                inputTwo.deleteCharAt(0); // 删除负号
            }
        }
        // 显示结果
        showInputText();
    }

    /**
     * 删除
     */
    public void onDeleteClick(View view) {
        if (!TextUtils.isEmpty(inputTwo)) { // 有第二个数字，则删除第二个数字的最后一位
            inputTwo.delete(inputTwo.length() - 1, inputTwo.length());
        } else if (!TextUtils.isEmpty(operateSymbol)) { // 没有第二个数字，但有操作符，则删除操作符
            operateSymbol = "";
        } else if (!TextUtils.isEmpty(inputOne)) { // 没有第二个数字，也没操作符，但有第一个数
            if (isResetInputOne) {
                isResetInputOne = false;
                inputOne.delete(0, inputOne.length());
                inputOne.append("0");
            } else {
                inputOne.delete(inputOne.length() - 1, inputOne.length());
                if (TextUtils.isEmpty(inputOne)) {
                    inputOne.append("0");
                }
            }
        }
        // 显示结果
        showInputText();
    }

    /**
     * 数字键
     */
    public void onNumberClick(View view) {
        // 添加数字
        addNumber(((Button) view).getText().toString());
    }

    /**
     * 点
     */
    public void onDotClick(View view) {
        if (TextUtils.isEmpty(operateSymbol)) { // 没有操作符，说明针对第一个数字编辑
            if (isResetInputOne) {
                isResetInputOne = false;
                inputOne.delete(0, inputOne.length());
                inputOne.append("0.");
            } else {
                if (TextUtils.isEmpty(inputOne)) {
                    inputOne.append("0.");
                }
                if (!inputOne.toString().contains(".")) {
                    inputOne.append(".");
                }
            }
        } else { // 针对第二个数字编辑
            if (TextUtils.isEmpty(inputTwo)) {
                inputTwo.append("0.");
            }
            if (!inputTwo.toString().contains(".")) {
                inputTwo.append(".");
            }
        }
        // 显示结果
        showInputText();
    }

    /**
     * + - × ÷ 操作符
     */
    public void onOperateClick(View view) {
        isResetInputOne = false;
        // 有第一个数
        if (!TextUtils.isEmpty(inputOne)) {
            if (!TextUtils.isEmpty(inputTwo)) { // 没第二个数
                // 计算
                calculate();
            }
            operateSymbol = ((Button) view).getText().toString();
            // 显示结果
            showInputText();
        }
    }

    /**
     * 等号
     */
    public void onEqualClick(View view) {
        if (!TextUtils.isEmpty(operateSymbol) && !TextUtils.isEmpty(inputTwo)) {
            isResetInputOne = true;
            // 计算
            calculate();
            operateSymbol = "";
        }
    }

    /**
     * 添加数字
     */
    private void addNumber(String number) {
        if (TextUtils.isEmpty(operateSymbol)) { // 没有操作符，即为第一个数字
            if (TextUtils.equals(inputOne, "0")) {
                inputOne.replace(0, 1, number);
                isResetInputOne = false;
            } else {
                if (isResetInputOne) {
                    isResetInputOne = false;
                    inputOne.delete(0, inputOne.length());
                }
                inputOne.append(number);
            }
        } else {
            if (TextUtils.equals(inputTwo, "0")) {
                inputTwo.replace(0, 1, number);
            } else {
                inputTwo.append(number);
            }
        }
        showInputText();
    }

    /**
     * 计算结果
     */
    private void calculate() {
        BigDecimal resultDecimal;
        switch (operateSymbol) {
            case ADD:
                resultDecimal = new BigDecimal(inputOne.toString()).add(new BigDecimal(inputTwo.toString()));
                inputText.set(resultDecimal.stripTrailingZeros().toPlainString());
                break;
            case SUBTRACT:
                resultDecimal = new BigDecimal(inputOne.toString()).subtract(new BigDecimal(inputTwo.toString()));
                inputText.set(resultDecimal.stripTrailingZeros().toPlainString());
                break;
            case MULTIPLY:
                resultDecimal = new BigDecimal(inputOne.toString()).multiply(new BigDecimal(inputTwo.toString()));
                inputText.set(resultDecimal.stripTrailingZeros().toPlainString());
                break;
            case DIVIDE:
                if (TextUtils.equals(inputTwo, "0")) {
                    showSingleToast("不能除以0！");
                    return;
                }
                resultDecimal = new BigDecimal(inputOne.toString()).divide(new BigDecimal(inputTwo.toString()), 8, BigDecimal.ROUND_HALF_UP);
                inputText.set(resultDecimal.stripTrailingZeros().toPlainString());
                break;
        }
        inputOne.delete(0, inputOne.length()).append(inputText.get());
        inputTwo.delete(0, inputTwo.length());
    }

    /**
     * 显示输入内容
     */
    private void showInputText() {
        inputText.set(inputOne.toString() + operateSymbol + inputTwo.toString());
    }

    /**
     * 背景点击
     */
    public void onBgClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
        if (isAnswerCardShowed.get()) {
            getDataBinding().answerCardLayout.startAnimation(animation);
        } else if (isAnswerAnalysisShowed.get()) {
            getDataBinding().answerCardLayout.startAnimation(animation);
        } else if (isCalculatorShowed.get()) {
            getDataBinding().calculateLayout.startAnimation(animation);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isAnswerCardShowed.get()) {
                    isAnswerCardShowed.set(false);
                } else if (isAnswerAnalysisShowed.get()) {
                    isAnswerAnalysisShowed.set(false);
                } else if (isCalculatorShowed.get()) {
                    isCalculatorShowed.set(false);
                    getDataBinding().bgLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 关闭答题卡
     */
    public void onAnswerCardCloseClick(View view) {
        onBgClick(null);
    }

    /**
     * 交卷并查看结果
     */
    public void onCommitAnswerAndShowResultClick(View view) {

    }
}
