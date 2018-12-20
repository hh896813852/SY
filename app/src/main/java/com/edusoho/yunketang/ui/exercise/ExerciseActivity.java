package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.edusoho.yunketang.bean.FaultRecord;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.bean.base.Message;
import com.edusoho.yunketang.databinding.ActivityExerciseBinding;
import com.edusoho.yunketang.helper.ToastHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.login.RegisterActivity;
import com.edusoho.yunketang.utils.BitmapUtil;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.utils.ScreenUtil;
import com.edusoho.yunketang.widget.AnswerResultLayout;
import com.edusoho.yunketang.widget.SimpleDialog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.activity_exercise)
public class ExerciseActivity extends BaseActivity<ActivityExerciseBinding> {
    public static final int FROM_EXERCISE_CODE = RequestCodeUtil.next();
    public static final String EXAMINATION_ID = "examination_id";
    public static final String MODULE_ID = "module_id";
    public static final String SELECTED_COURSE = "selected_course";
    public static final String CLASS_ID = "class_id";
    public static final String LAST_PAGE_INDEX = "last_page_index";
    public static final String HOMEWORK_ID = "homework_id";
    public static final String IS_MY_COLLECTION = "is_my_collection";
    public static final String IS_MY_FAULTS = "is_my_faults";
    public static final String IS_EXAM_TEST = "is_exam_test";
    public static final String IS_MODULE_EXERCISE = "is_module_exercise";
    public static final String IS_CLASS_EXERCISE = "is_class_exercise";
    public String examinationId;
    private int moduleId;
    private EducationCourse selectedCourse;
    private String classId;
    private int lastPageIndex;
    private String homeworkId;
    private boolean isMyCollection;   // 是否是我的收藏题目
    private boolean isMyFaults;       // 是否是我的错题记录
    private boolean isExamTest;       // 是否是真题测试（考试）
    private boolean isModuleExercise; // 是否是模块练习
    private boolean isClassExercise;  // 是否是班级练习

    private int commitCount;         // 可提交问题次数
    private boolean isPreCommitExam; // 是否准备提交试卷

    private static final String ADD = "+";
    private static final String SUBTRACT = "-";
    private static final String MULTIPLY = "×";
    private static final String DIVIDE = "÷";

    public ObservableField<Boolean> isShowCommitAnswerBtn = new ObservableField<>(false); // 是否显示交卷按钮
    public ObservableField<Boolean> isQuestionStem = new ObservableField<>(true);         // 是否是题干
    public ObservableField<Boolean> isQuestionCollected = new ObservableField<>(false);   // 是否已收藏
    public ObservableField<Boolean> isCalculatorShowed = new ObservableField<>(false);    // 是否显示计算器
    public ObservableField<Boolean> isAnswerCardShowed = new ObservableField<>(false);    // 是否显示答题卡
    public ObservableField<Boolean> isAnswerAnalysisShowed = new ObservableField<>(false);// 是否显示答案解析
    public ObservableField<Boolean> isAnswerAnalysisTagShowed = new ObservableField<>(false);// 是否显示答案解析标签
    public ObservableField<Boolean> isShowDeleteIcon = new ObservableField<>(false);      // 是否显示问题删除图标
    public ObservableField<Boolean> isShowCommitIcon = new ObservableField<>(false);      // 是否显示试卷提交图标
    public ObservableField<Boolean> isShowAnswerCardIcon = new ObservableField<>(false);  // 是否显示答题卡图标
    public ObservableField<String> leftTime = new ObservableField<>("");  // 测试/考试剩余时间
    public ObservableField<String> correctAnswer = new ObservableField<>(); // 正确答案
    public ObservableField<String> answerAnalysis = new ObservableField<>();// 答案解析

    public ObservableField<String> questionTypeName = new ObservableField<>(); // 题型别名
    public ObservableField<String> questionTypeInfo = new ObservableField<>(); // 题型信息

    private List<Question> questionStem = new ArrayList<>(); // 题干集合
    private Question currentLoadQuestionStem;     // 当前加载题目的题干
    private int currentLoadQuestionStemIndex = 0; // 当前加载题目的题干的下标
    private String[] currentLoadQuestionIds;    // 当前加载题目的题干的题目id数组
    private String currentLoadQuestionId;       // 当前加载题目的题干的题目id
    private int currentLoadQuestionIdIndex = 0; // 当前加载题目的题干的题目id的下标
    private String currentLoadQuestionStemSum;  // 当前加载题型的题目总数

    private Question currentQuestion;           // 当前显示的题目
    public int currentChildQuestionIndex;       // 当前题目子题的下标，用于展示哪道子题的答案解析

    public ObservableField<String> inputText = new ObservableField<>("0");
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
            view.findViewById(R.id.correctView).setVisibility(View.GONE);
            view.findViewById(R.id.falseView).setVisibility(View.GONE);
            TextView questionTypeView = view.findViewById(R.id.questionTypeView);
            questionTypeView.setText(questionStem.get(position).getQuestionTypeName());
            AnswerResultLayout answerResultLayout = view.findViewById(R.id.answerResultLayout);
            answerResultLayout.setTags(list.get(position));
            answerResultLayout.setOnTagClickListener(index -> {
                // 获取该题的题干所在下标
                int stemIndex = questionList.indexOf(questionStem.get(position));
                // 显示该题的页面
                showPageByPosition(stemIndex + index + 1);
                // 关闭答题卡
                onBgClick(null);
            });
            return view;
        }
    };

    public List<String> answerAnalysisPicList = new ArrayList<>();
    public SYBaseAdapter answerAnalysisPicAdapter = new SYBaseAdapter();

    public List<String> correctAnswerPicList = new ArrayList<>();
    public SYBaseAdapter correctAnswerPicAdapter = new SYBaseAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if (isMyFaults) {
            // 加载错题列表
            loadMyFaults();
        } else if (isMyCollection) {
            // 加载收藏问题列表
            loadMyCollection();
        } else {
            // 加载题干
            loadQuestionStem();
        }
    }

    private void initView() {
        // 初始化接收参数
        examinationId = getIntent().getStringExtra(EXAMINATION_ID);
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
        classId = getIntent().getStringExtra(CLASS_ID);
        lastPageIndex = getIntent().getIntExtra(LAST_PAGE_INDEX, 0);
        homeworkId = getIntent().getStringExtra(HOMEWORK_ID);
        isMyCollection = getIntent().getBooleanExtra(IS_MY_COLLECTION, false);
        isMyFaults = getIntent().getBooleanExtra(IS_MY_FAULTS, false);
        isExamTest = getIntent().getBooleanExtra(IS_EXAM_TEST, false);
        isModuleExercise = getIntent().getBooleanExtra(IS_MODULE_EXERCISE, false);
        isClassExercise = getIntent().getBooleanExtra(IS_CLASS_EXERCISE, false);
        // 我的错题显示删除问题图标按钮
        isShowDeleteIcon.set(isMyFaults);
        // 班级练习 或 模块练习 或 真题测试 显示试卷提交按钮
        isShowCommitIcon.set(isClassExercise || isModuleExercise || isExamTest);
        // 班级练习 或 模块练习 或 真题测试 显示答题卡图标
        isShowAnswerCardIcon.set(isClassExercise || isModuleExercise || isExamTest);
        // 答题卡adapter
        adapter.init(this, R.layout.item_answer_report, list);
        // 正确答案图片adapter
        correctAnswerPicAdapter.init(this, R.layout.item_pic, correctAnswerPicList);
        // 答案解析图片adapter
        answerAnalysisPicAdapter.init(this, R.layout.item_pic, answerAnalysisPicList);
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
                // 班级练习或模块练习或真题测试 并且 是最后一题
                isShowCommitAnswerBtn.set((isClassExercise || isModuleExercise || isExamTest) && questionList.size() - 1 == position);
                // 获取当前问题
                currentQuestion = questionList.get(position);
                // 模块练习，班级练习，需一题一题提交
                if (isModuleExercise || isClassExercise) {
                    // 告诉后台当前页面和已完成题目数量
                    tellSomethingToYou();
                    // 添加问题提交缓冲区
                    checkPreCommitQuestion();
                    // 如果不是题干，添加该问题到"预提交问题集合"
                    if (currentQuestion.questionType > 0) {
                        preCommitQuestionList.add(currentQuestion);
                    }
                }
                // 刷新界面
                refreshView(currentQuestion);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 加载错题列表
     */
    private void loadMyFaults() {
        ProgressDialogUtil.showProgress(this, "正在加载错题信息，请稍后...", false);
        SYDataTransport.create(SYConstants.HOMEWORK_ALL_FAULTS)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("homeworkId", homeworkId)
                .execute(new SYDataListener<List<Question>>() {

                    @Override
                    public void onSuccess(List<Question> data) {
                        if (data.size() == 0) {
                            showSingleToast("未查询到错误题目！");
                            finish();
                            return;
                        }
                        for (int i = 0; i < data.size(); i++) {
                            Question question = data.get(i);
                            question.questionSort = i + 1;
                            question.sum = String.valueOf(data.size());
                            question.alias = data.get(i).subclassificationName;
                            for (Question.QuestionDetails details : question.details) {
                                details.options = details.getChoiceList();
                            }
                            questionList.add(question);
                        }
                        // 刷新界面
                        refreshView(questionList.get(0));
                        viewPagerAdapter.notifyDataSetChanged();
                        ProgressDialogUtil.hideProgress();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                    }
                }, new TypeToken<List<Question>>() {
                });
    }

    /**
     * 加载收藏问题列表
     */
    private void loadMyCollection() {
        ProgressDialogUtil.showProgress(this, "正在加载收藏试题，请稍后...", false);
        SYDataTransport.create(SYConstants.ALL_MY_EXAM_COLLECT)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("examinationId", examinationId)
                .execute(new SYDataListener<List<Question>>() {

                    @Override
                    public void onSuccess(List<Question> data) {
                        if (data.size() == 0) {
                            showSingleToast("未查询到错误题目！");
                            finish();
                            return;
                        }
                        for (int i = 0; i < data.size(); i++) {
                            Question question = data.get(i);
                            question.questionSort = i + 1;
                            question.sum = String.valueOf(data.size());
                            question.alias = data.get(i).subclassificationName;
                            for (Question.QuestionDetails details : question.details) {
                                details.options = details.getChoiceList();
                            }
                            questionList.add(question);
                        }
                        // 刷新界面
                        refreshView(questionList.get(0));
                        viewPagerAdapter.notifyDataSetChanged();
                        ProgressDialogUtil.hideProgress();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                    }
                }, new TypeToken<List<Question>>() {
                });

    }

    /**
     * 加载题干
     */
    private void loadQuestionStem() {
        ProgressDialogUtil.showProgress(this, "正在加载试卷信息，请稍后...", false);
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
                            // 该题型下有题目
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

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                        finish();
                    }
                }, new TypeToken<List<Question>>() {
                });
    }

    /**
     * 加载题目
     */
    private void loadQuestion() {
        SYDataTransport.create(SYConstants.QUESTION_QUERY)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("examinationId", examinationId)
                .addParam("moduleId", moduleId)
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
                            question.alias = currentLoadQuestionStem.alias;
                            for (Question.QuestionDetails details : question.details) {
                                details.options = details.getChoiceList();
                            }
                            // 还原用户答案
                            if (question.userResult != null) {
                                List<MyAnswer> userResultList = JsonUtil.fromJson(question.userResult, new TypeToken<List<MyAnswer>>() {
                                });
                                // 选择题
                                if (question.questionType > 0 && question.questionType < 6) {
                                    // 遍历子题
                                    for (int j = 0; j < question.details.size(); j++) {

                                        // 存在用户选项
                                        if (!TextUtils.isEmpty(userResultList.get(j).result)) {
                                            // 用户选项
                                            List<String> option = Arrays.asList(userResultList.get(j).result.split(","));
                                            // 遍历子题的选项
                                            for (int k = 0; k < question.details.get(j).options.size(); k++) {
                                                // 包含用户哪个选项，那么这个选项就是选中的
                                                question.details.get(j).options.get(k).isPicked = option.contains(String.valueOf(k + 1));
                                            }
                                        }
                                    }
                                } else { // 简答、综合
                                    // 遍历子题
                                    for (int j = 0; j < question.details.size(); j++) {
                                        // 存在用户作答
                                        if (!TextUtils.isEmpty(userResultList.get(j).result)) {
                                            question.details.get(j).myAnswerContent = userResultList.get(j).result;
                                        }
                                        // 存在用户作答图片
                                        if (!TextUtils.isEmpty(userResultList.get(j).userResultUrl)) {
                                            question.details.get(j).myAnswerPicUrl = userResultList.get(j).userResultUrl;
                                        }
                                    }
                                }
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
                            } else { // 试卷全部加载完毕
                                ProgressDialogUtil.hideProgress();
                                // 刷新ViewPager
                                viewPagerAdapter.notifyDataSetChanged();
                                // 模块练习 或 班级练习 去上次停留页面
                                if (isModuleExercise || isClassExercise) {
                                    // 去上次停留页面
                                    showPageByPosition(lastPageIndex);
                                }
                                // 如果是测试/考试，则开始倒计时
                                if (isExamTest) {
                                    // 开始倒计时
                                    startTimer(3 * 60 * 1000 + 3);
                                }
                                startTimer(3 * 60 * 1000 + 5 * 1000);
                            }
                        } else { // 该题型问题并未全部加载完毕，则继续加载
                            // 获取下次加载题目的第一个id下标
                            currentLoadQuestionIdIndex = currentLoadQuestionIdIndex + SYConstants.PAGE_SIZE;
                            // 获取下次加载题目的第一个id
                            currentLoadQuestionId = currentLoadQuestionIds[currentLoadQuestionIdIndex];
                            // 继续加载题目
                            loadQuestion();
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                        finish();
                    }
                }, new TypeToken<List<Question>>() {
                });
    }

    /**
     * 告诉后台当前页面和已完成题目数量
     */
    private void tellSomethingToYou() {
        SYDataTransport dataTransport = SYDataTransport.create(SYConstants.HOMEWORK_REDIS);
        dataTransport.addParam("userId", getLoginUser().syjyUser.id)
                .addParam("examinationId", examinationId)
                .addParam("index", getDataBinding().viewPager.getCurrentItem())
                .addParam("finishCount", getQuestionFinishCount());
        if (!TextUtils.isEmpty(classId)) {
            dataTransport.addParam("classId", classId);
        }
        if (moduleId != 0) {
            dataTransport.addParam("moduleId", moduleId);
        }
        dataTransport.execute(null);
    }

    /**
     * 提交问题队列里的问题
     */
    private void checkPreCommitQuestion() {
        // 清空可提交问题集合
        canCommitQuestionList.clear();
        // 将预提交问题添加到可提交集合中
        canCommitQuestionList.addAll(preCommitQuestionList);
        // 清空预提交问题集合
        preCommitQuestionList.clear();
        // 存在可提交问题
        if (canCommitQuestionList.size() > 0) {
            // 需提交次数
            commitCount = canCommitQuestionList.size();
            // 提交"可提交问题集合"里的问题
            for (Question question : canCommitQuestionList) {
                commitQuestionAnswer(question);
            }
        }
    }

    /**
     * 提交"可提交问题集合"里的问题
     */
    private void commitQuestionAnswer(final Question question) {
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
        dataTransport.addParam("correctResult", getCorrectAnswer(question))
                .addParam("userResult", getAnswerList(question))
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onMessage(Message message) {
                        super.onMessage(message);
                        commitCount--;
                        // 如果是准备提交试卷 并且 可提交问题均已提交过（包含提交失败）
                        if (isPreCommitExam && commitCount == 0) {
                            // 因为可能包含提交失败，所以在此检查是否均已提交
                            checkIsAnswerAllCommit();
                        }
                    }

                    @Override
                    public void onSuccess(String data) {
                        // 如果homeworkId为空，获取homeworkId
                        if (TextUtils.isEmpty(homeworkId)) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                homeworkId = jsonObject.getString("homeworkId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        // 提交失败，则将提交失败的问题添加到预提交集合中
                        preCommitQuestionList.add(question);
                    }
                }, String.class);
    }

    /**
     * 获取该问题的正确答案
     */
    private ArrayList<MyAnswer> getCorrectAnswer(Question question) {
        ArrayList<MyAnswer> correctAnswerList = new ArrayList<>();
        for (Question.QuestionDetails details : question.details) {
            MyAnswer mAnswer = new MyAnswer();
            StringBuilder builder = new StringBuilder();
            mAnswer.result = builder.append(details.correctResult).toString();
            if (question.questionType >= 6) {
                mAnswer.correctResultUrl = details.correctResultUrl;
            }
            correctAnswerList.add(mAnswer);
        }
        return correctAnswerList;
    }

    /**
     * 获取该问题的作答选项或内容
     */
    private ArrayList<MyAnswer> getAnswerList(Question question) {
        ArrayList<MyAnswer> answerList = new ArrayList<>();
        for (Question.QuestionDetails details : question.details) {
            MyAnswer mAnswer = new MyAnswer();
            StringBuilder builder = new StringBuilder();
            if (question.questionType < 6) {
                for (int i = 0; i < details.options.size(); i++) {
                    if (details.options.get(i).isPicked) {
                        builder.append(i + 1).append(",");
                    }
                }
                if (TextUtils.isEmpty(builder)) {
                    mAnswer.result = "";
                } else {
                    mAnswer.result = builder.deleteCharAt(builder.length() - 1).toString();
                }
            } else {
                mAnswer.result = details.myAnswerContent;
                mAnswer.userResultUrl = details.myAnswerPicUrl;
            }
            answerList.add(mAnswer);
        }
        return answerList;
    }

    /**
     * 通过下标显示对应的page
     */
    private void showPageByPosition(int position) {
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
     * 点击收藏
     */
    public void onCollectClick(View view) {
        if (currentQuestion.isStar) {
            SYDataTransport.create(SYConstants.QUESTION_CANCEL_COLLECTION)
                    .addParam("starId", currentQuestion.starId)
                    .addParam("questionId", currentQuestion.questionId)
                    .execute(new SYDataListener() {

                        @Override
                        public void onSuccess(Object data) {
                            isQuestionCollected.set(false);
                            currentQuestion.isStar = false;
                            currentQuestion.starId = "";
                            showSingleToast("已取消收藏！");
                        }
                    });
        } else {
            SYDataTransport.create(SYConstants.QUESTION_ADD_COLLECTION)
                    .addParam("userId", getLoginUser().syjyUser.id)
                    .addParam("examinationId", TextUtils.isEmpty(examinationId) ? currentQuestion.examinationId : examinationId)
                    .addParam("questionType", currentQuestion.questionType)
                    .addParam("questionId", currentQuestion.questionId)
                    .addParam("alias", currentQuestion.alias)
                    .execute(new SYDataListener<Question>() {

                        @Override
                        public void onSuccess(Question question) {
                            isQuestionCollected.set(true);
                            currentQuestion.isStar = true;
                            currentQuestion.starId = question.starId;
                            showSingleToast("收藏成功！");
                        }
                    }, Question.class);
        }
    }

    /**
     * 刷新界面
     */
    private void refreshView(Question question) {
        if (question.questionType > 0) {
            isQuestionCollected.set(question.isStar);
        }
        isQuestionStem.set(question.questionType == 0);
        isAnswerAnalysisTagShowed.set(question.questionType != 0);
        questionTypeName.set(question.getQuestionTypeName());
        questionTypeInfo.set(question.questionType == 0 ? "共" + question.sum + "题" : question.questionSort + "/" + question.sum);
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
     * 获取问题完成总数
     */
    private int getQuestionFinishCount() {
        int finishCount = 0;
        for (Question question : questionList) {
            if (checkQuestionIsDo(question)) {
                finishCount++;
            }
        }
        return finishCount;
    }

    /**
     * 检查是否作答
     */
    private boolean checkQuestionIsDo(Question question) {
        if (question.questionType > 0 && question.questionType < 6) { // 单 多 阅读 听力 判断
            // 遍历子题
            for (Question.QuestionDetails details : question.details) {
                // 遍历子题选项
                for (Question.QuestionDetails.Option option : details.options) {
                    // 只要有一个小题做出选择，就认为已作答
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
     * 检查作答是否正确
     */
    private boolean checkAnswerIsRight(Question question) {
        if (question.questionType > 0 && question.questionType < 6) { // 单 多 阅读 听力 判断
            int rightCount = 0;
            List<MyAnswer> correctList = getCorrectAnswer(question);
            List<MyAnswer> answerList = getAnswerList(question);
            if (correctList.size() == answerList.size()) {
                for (int i = 0; i < correctList.size(); i++) {
                    if (correctList.get(i).result.equals(answerList.get(i).result)) {
                        rightCount++;
                    }
                }
            }
            return rightCount == correctList.size();
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
        // 刷新答案解析
        if (isAnswerAnalysisTagShowed.get()) {
            // 子题下标
            int childQuestionIndex = currentQuestion.questionType == 3 || currentQuestion.questionType == 7 ? currentChildQuestionIndex : 0;
            correctAnswer.set(currentQuestion.questionType > 0 && currentQuestion.questionType < 6 ? currentQuestion.details.get(childQuestionIndex).getCorrectResult() : currentQuestion.details.get(childQuestionIndex).correctResult);
            answerAnalysis.set(currentQuestion.details.get(childQuestionIndex).resultResolve);
            // 正确答案图片
            String correctResultUrl = currentQuestion.details.get(childQuestionIndex).correctResultUrl;
            if (!TextUtils.isEmpty(correctResultUrl)) {
                correctAnswerPicList.addAll(Arrays.asList(correctResultUrl.split(",")));
                correctAnswerPicAdapter.notifyDataSetChanged();
            }
            // 答案解析图片
            String resultResolveUrl = currentQuestion.details.get(childQuestionIndex).resultResolveUrl;
            if (!TextUtils.isEmpty(resultResolveUrl)) {
                answerAnalysisPicList.addAll(Arrays.asList(resultResolveUrl.split(",")));
                answerAnalysisPicAdapter.notifyDataSetChanged();
            }
        }
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
        // 交卷
        commitExam();
    }

    /**
     * 交卷
     */
    private void commitExam() {
        if (TextUtils.isEmpty(homeworkId)) {
            showSingleToast("您还未作答，不可提交试卷！");
            return;
        }
        if (checkQuestionIsAllDo()) {
            ProgressDialogUtil.showProgress(this, "正在提交试卷，请稍后...");
            // 检查问题是否均已提交
            checkIsAnswerAllCommit();
        } else {
            // 显示未答完交卷提示对话框
            showNoFinishDialog();
        }
    }

    /**
     * 检查问题是否均已作答
     */
    private boolean checkQuestionIsAllDo() {
        return getQuestionFinishCount() == questionList.size() - questionStem.size();
    }

    /**
     * 检查问题是否均已提交
     */
    private void checkIsAnswerAllCommit() {
        // 问题均已提交
        if (preCommitQuestionList.size() == 0) {
            isPreCommitExam = false;
            // 提交试卷
            CommitExamination();
        } else { // 尚有问题未提交
            // 准备提交试卷
            isPreCommitExam = true;
            // 先把未提交的问题提交
            checkPreCommitQuestion();
        }
    }

    /**
     * 提交试卷
     */
    private void CommitExamination() {
        SYDataTransport.create(SYConstants.EXAMINATION_COMMIT)
                .addParam("homeworkId", homeworkId)
                .execute(new SYDataListener() {

                    @Override
                    public void onSuccess(Object data) {
                        showSingleToast("试卷提交成功！");
                        ProgressDialogUtil.hideProgress();
                        finish();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                    }
                });
    }

    /**
     * 监听返回按钮
     */
    @Override
    public void onBackPressed() {
        onBackButtonClick(null);
    }

    @Override
    public void onBackButtonClick(View view) {
        if (isAnswerAnalysisShowed.get() || isAnswerCardShowed.get() || isCalculatorShowed.get()) {
            onBgClick(null);
        } else {
            // 不存在homeworkId，说明没有提交过一道题
            if (TextUtils.isEmpty(homeworkId) || isMyFaults || isMyCollection) {
                finish();
            } else {
                // 提交过作答，则显示退出提示对话框
                showExitTipDialog();
            }
        }
    }

    /**
     * 显示退出未交卷提示框
     */
    private void showExitTipDialog() {
        DialogUtil.showSimpleAnimDialog(this, "您的题目还没有交卷，确定要退出吗？", "交卷", "退出", new SimpleDialog.OnSimpleClickListener() {
            @Override
            public void OnLeftBtnClicked(SimpleDialog dialog) {
                dialog.dismiss();
                // 交卷
                onCommitAnswerAndShowResultClick(null);
            }

            @Override
            public void OnRightBtnClicked(SimpleDialog dialog) {
                dialog.superDismiss(); // 无动画退出
                finish();
            }
        });
    }

    /**
     * 显示未答完交卷提示对话框
     */
    private void showNoFinishDialog() {
        DialogUtil.showSimpleAnimDialog(this, "您还有题目未作答，确认交卷吗？", "取消", "交卷", new SimpleDialog.OnSimpleClickListener() {
            @Override
            public void OnLeftBtnClicked(SimpleDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnRightBtnClicked(SimpleDialog dialog) {
                ProgressDialogUtil.showProgress(ExerciseActivity.this, "正在提交试卷，请稍后...");
                // 检查问题是否均已提交
                checkIsAnswerAllCommit();
            }
        });
    }

    /**
     * 删除错题
     */
    public void onQuestionDeleteClick(View view) {
        SYDataTransport.create(SYConstants.DELETE_FAULT_QUESTION)
                .addParam("selectId", currentQuestion.questionId)
                .addParam("errorId", currentQuestion.errorId)
                .execute(new SYDataListener() {

                    @Override
                    public void onSuccess(Object data) {
                        showSingleToast("删除成功！");
                        // 列表删除当前问题
                        questionList.remove(currentQuestion);
                        // 已经没有错题，则退出
                        if (questionList.size() == 0) {
                            finish();
                        } else {
                            // 刷新ViewPager
                            viewPagerAdapter.notifyDataSetChanged();
                            // 获取当前问题
                            currentQuestion = questionList.get(getDataBinding().viewPager.getCurrentItem());
                            // 刷新界面
                            refreshView(currentQuestion);
                        }
                    }
                });
    }

    /**
     * 答案提交按钮
     */
    public void onCommitAnswerClick(View view) {
        // 交卷
        commitExam();
    }

    /**
     * 开始倒计时
     */
    private void startTimer(long millisInFuture) {
        new MyCountDownTimer(millisInFuture, 1000).start();
    }

    /**
     * 测试/考试倒计时计时器
     */
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leftTime.set(DateUtils.second2Min((int) (millisUntilFinished / 1000)));
            if ((int) (millisUntilFinished / 1000) == 3 * 60) {
                ToastHelper.showCustomToast(ExerciseActivity.this);
            }
        }

        @Override
        public void onFinish() {
            leftTime.set("00:00");
            ProgressDialogUtil.showProgress(ExerciseActivity.this, "正在提交试卷，请稍后...", false);
            // 检查问题是否均已提交
            checkIsAnswerAllCommit();
        }
    }
}
