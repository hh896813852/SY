package com.edusoho.yunketang.ui.testlib;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.AnswerReport;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.ActivityAnswerReportBinding;
import com.edusoho.yunketang.helper.QuestionHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.widget.AnswerResultLayout;
import com.edusoho.yunketang.widget.CircleBarView;
import com.edusoho.yunketang.widget.dialog.SXYDialog;
import com.edusoho.yunketang.widget.dialog.SimpleDialog;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Layout(value = R.layout.activity_answer_report, title = "答题报告", rightButton = "再做一遍")
public class AnswerReportActivity extends BaseActivity<ActivityAnswerReportBinding> {
    public static final int FROM_REPORT_REQUEST_CODE = RequestCodeUtil.next();
    public static final String HOMEWORK_ID = "homework_id";
    public static final String EXAMINATION_ID = "examination_id";
    public static final String SELECTED_COURSE = "selected_course";
    public static final String MODULE_ID = "module_id";
    public static final String CLASS_ID = "class_id";
    public static final String IS_EXAM = "is_exam";
    public static final String IS_MODULE_EXERCISE = "is_module_exercise";
    public static final String IS_CLASS_EXERCISE = "is_class_exercise";
    private String homeworkId;
    public String examinationId;
    private EducationCourse selectedCourse;
    private int moduleId;
    private String classId;
    private boolean isModuleExercise; // 是否是模块练习
    private boolean isClassExercise;  // 是否是班级练习

    public ObservableField<String> finishDate = new ObservableField<>(); // 日期
    public ObservableField<String> usedTime = new ObservableField<>();   // 用时
    public ObservableField<String> trueNum = new ObservableField<>("0");
    public ObservableField<String> falseNum = new ObservableField<>("0");
    public ObservableField<Boolean> isExam = new ObservableField<>(); // 是否是考试

    private List<AnswerReport.AnswerDetails> answerDetailsList;
    private List<Question> faultQuestionStemList = new ArrayList<>(); // 错题题干集合

    public List<Map<String, List<Integer>>> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = super.getView(position, convertView, parent);
            View view = DataBindingUtil.inflate(LayoutInflater.from(AnswerReportActivity.this), R.layout.item_answer_report, parent, false).getRoot();
            TextView correctView = view.findViewById(R.id.correctView);
            TextView falseView = view.findViewById(R.id.falseView);
            if (answerDetailsList.get(position).questionType >= 6) { // 简答题 综合题不显示对错
                correctView.setVisibility(View.GONE);
                falseView.setVisibility(View.GONE);
            } else {
                correctView.setVisibility(View.VISIBLE);
                falseView.setVisibility(View.VISIBLE);
                correctView.setText("对" + answerDetailsList.get(position).correctCount);
                falseView.setText("错" + answerDetailsList.get(position).falseCount);
            }
            // 题目类型
            TextView questionTypeView = view.findViewById(R.id.questionTypeView);
            questionTypeView.setText(answerDetailsList.get(position).alias);
            // 作答情况
            AnswerResultLayout answerResultLayout = view.findViewById(R.id.answerResultLayout);
            answerResultLayout.setTags(list.get(position));
            answerResultLayout.setOnTagClickListener((firstIndex, secondIndex) -> {
                AnswerReport.AnswerDetails details = answerDetailsList.get(position);
                // 获取该题的题干顺序
                int stemSort = details.sort;
                Intent intent = new Intent(AnswerReportActivity.this, ExerciseActivity.class);
                intent.putExtra(ExerciseActivity.IS_FROM_REPORT_CARD, true);
                intent.putExtra(ExerciseActivity.REPORT_CARD_STEM_SORT, stemSort);
                intent.putExtra(ExerciseActivity.REPORT_CARD_QUESTION_TYPE, details.questionType);
                intent.putExtra(ExerciseActivity.REPORT_CARD_FIRST_INDEX, firstIndex);
                intent.putExtra(ExerciseActivity.REPORT_CARD_SECOND_INDEX, secondIndex);
                intent.putExtra(ExerciseActivity.EXAMINATION_ID, examinationId);
                intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                intent.putExtra(ExerciseActivity.CLASS_ID, classId);
                intent.putExtra(ExerciseActivity.IS_ANSWER_ANALYSIS, true);
                startActivity(intent);
            });
            return view;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {
        homeworkId = getIntent().getStringExtra(HOMEWORK_ID);
        examinationId = getIntent().getStringExtra(EXAMINATION_ID);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        classId = getIntent().getStringExtra(CLASS_ID);
        isExam.set(getIntent().getBooleanExtra(IS_EXAM, false));
        isModuleExercise = getIntent().getBooleanExtra(IS_MODULE_EXERCISE, false);
        isClassExercise = getIntent().getBooleanExtra(IS_CLASS_EXERCISE, false);
        adapter.init(this, R.layout.item_answer_report, list);

        // 设置根据进度改变文字的TextView
        getDataBinding().circleView.setTextView(getDataBinding().progressText);
        // 设置动画进度监听来改变文字和进度条颜色
        getDataBinding().circleView.setOnAnimationListener(new CircleBarView.OnAnimationListener() {
            @Override
            public String howToChangeText(float interpolatedTime, float progressNum, float maxNum) {
                DecimalFormat decimalFormat = new DecimalFormat("0");
                return decimalFormat.format(interpolatedTime * progressNum / maxNum * 100);
            }

            @Override
            public void howToChangeProgressColor(Paint paint, float interpolatedTime, float updateNum, float maxNum) {

            }
        });
        // 设置最大值
        getDataBinding().circleView.setMaxNum(100);
    }

    private void loadData() {
        SYDataTransport.create(SYConstants.CHECK_REPORT)
                .addParam("homeworkId", homeworkId)
                .execute(new SYDataListener<AnswerReport>() {

                    @Override
                    public void onSuccess(AnswerReport data) {
                        answerDetailsList = data.returnList;
                        // 刷新界面
                        refreshView(data);
                        // 刷新答题报告
                        refreshAnswerReport();
                        // 有错题
                        if (data.falseSum > 0) {
                            // 获取错题题干
                            getFaultStem();
                        }
                    }
                }, AnswerReport.class);
    }

    /**
     * 获取错题题干
     */
    private void getFaultStem() {
        for (AnswerReport.AnswerDetails details : answerDetailsList) {
            // 错题不展示简答题、综合题
            if (details.questionType < 6) {
                // 该题型下有错题
                if (details.falseCount > 0) {
                    Question questionStem = new Question();
                    questionStem.sort = details.sort;
                    questionStem.alias = details.alias;
                    questionStem.type = details.questionType;
                    questionStem.explain = details.explain;
                    questionStem.questionSum = String.valueOf(details.falseCount);
                    StringBuilder builder = new StringBuilder();
                    if (details.questionType == 3) {
                        for (int i = 0; i < details.userAnswerResult.size(); i++) {
                            for (AnswerReport.AnswerDetails.AnswerType answerType : details.userAnswerResult.get(i)) {
                                // type错误类型（0：错误，1：未答，2 : 已答，3：正确）
                                if (answerType.type < 2) {
                                    builder.append(i + 1).append(",");
                                    break;
                                }
                            }
                        }
                    } else {
                        for (AnswerReport.AnswerDetails.Mistake mistake : details.homeworkMistakes) {
                            builder.append(mistake.idex).append(",");
                        }
                    }
                    // 错题序号
                    if (!TextUtils.isEmpty(builder)) {
                        questionStem.faultSort = builder.deleteCharAt(builder.length() - 1).toString();
                    }
                    faultQuestionStemList.add(questionStem);
                }
            }
        }
    }

    /**
     * 刷新界面
     */
    private void refreshView(AnswerReport data) {
        trueNum.set(String.valueOf(data.correctSum));
        falseNum.set(String.valueOf(data.falseSum));
        if (isExam.get()) {
            usedTime.set("用时：" + DateUtils.second2Min(data.completeTime));
            finishDate.set("日期：" + DateUtils.formatDate(data.updateDate, "MM-dd"));
            getDataBinding().circleView.setMaxNum(data.totalScore);
            getDataBinding().scoreText.setText(data.sumPoint);
        }
        // 设置进度和动画执行时间，并开始动画
        getDataBinding().circleView.setProgressNum(isExam.get() ? Float.valueOf(data.sumPoint) : Float.valueOf(data.percent), 2000);
        // 显示上小元对话框
        showSXYDialog(data.percent);
    }

    /**
     * 显示上小元对话框
     */
    private void showSXYDialog(String percent) {
        DialogUtil.showSXY(this, percent, dialog -> {
            dialog.dismiss();
            onRightButtonClick();
        });
    }

    /**
     * 刷新答题报告
     */
    private void refreshAnswerReport() {
        list.clear();
        for (AnswerReport.AnswerDetails details : answerDetailsList) {
            Map<String, List<Integer>> map = new LinkedHashMap<>();
            if (details.questionType == 3) { // 阅读选择题
                for (int i = 0; i < details.userAnswerResult.size(); i++) {
                    List<Integer> child = new ArrayList<>();
                    for (AnswerReport.AnswerDetails.AnswerType answerType : details.userAnswerResult.get(i)) {
                        if (answerType.type == 0) {
                            child.add(AnswerResultLayout.ANSWER_FALSE);
                        }
                        if (answerType.type == 1) {
                            child.add(AnswerResultLayout.NOT_ANSWER);
                        }
                        if (answerType.type == 3) {
                            child.add(AnswerResultLayout.ANSWER_TRUE);
                        }
                    }
                    map.put(QuestionHelper.getSort(i), child);
                }
            } else if (details.questionType == 6) { // 简答题
                List<Integer> child = new ArrayList<>();
                for (AnswerReport.AnswerDetails.Mistake mistake : details.homeworkMistakes) {
                    List<MyAnswer> answers = JsonUtil.fromJson(mistake.userResult, new TypeToken<List<MyAnswer>>() {
                    });
                    child.add(TextUtils.isEmpty(answers.get(0).result) && TextUtils.isEmpty(answers.get(0).userResultUrl) ? AnswerResultLayout.NOT_ANSWER : AnswerResultLayout.ANSWERED);
                }
                map.put("", child);
            } else if (details.questionType == 7) { // 综合题
                for (int i = 0; i < details.userAnswerResult.size(); i++) {
                    List<Integer> child = new ArrayList<>();
                    for (AnswerReport.AnswerDetails.AnswerType answerType : details.userAnswerResult.get(i)) {
                        child.add(answerType.type == 1 ? AnswerResultLayout.NOT_ANSWER : AnswerResultLayout.ANSWERED);
                    }
                    map.put(QuestionHelper.getSort(i), child);
                }
            } else { // 单选题
                List<Integer> child = new ArrayList<>();
                for (int i = 0; i < details.falseCount + details.correctCount; i++) {
                    child.add(AnswerResultLayout.ANSWER_TRUE);
                }
                for (AnswerReport.AnswerDetails.Mistake mistake : details.homeworkMistakes) {
                    child.set(mistake.idex - 1, mistake.type == 0 ? AnswerResultLayout.ANSWER_FALSE : AnswerResultLayout.NOT_ANSWER);
                }
                map.put("", child);
            }
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRightButtonClick() {
        DialogUtil.showSimpleAnimDialog(this, "再做一遍将会清空您之前的作答记录，是否再做一遍？", "取消", "再做一遍", new SimpleDialog.OnSimpleClickListener() {
            @Override
            public void OnLeftBtnClicked(SimpleDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void OnRightBtnClicked(SimpleDialog dialog) {
                dialog.dismiss();
                doAgain();
            }
        });
    }

    /**
     * 再做一遍
     */
    private void doAgain() {
        SYDataTransport.create(TextUtils.isEmpty(classId) ? SYConstants.DO_AGAIN_IN_MODULE : SYConstants.DO_AGAIN_IN_CLASS)
                .addParam("homeworkId", homeworkId)
                .addProgressing(this, "正在清空之前的作答...")
                .execute(new SYDataListener() {
                    @Override
                    public void onSuccess(Object data) {
                        setResult(Activity.RESULT_OK);
                        Intent intent = new Intent(AnswerReportActivity.this, ExerciseActivity.class);
                        intent.putExtra(ExerciseActivity.EXAMINATION_ID, examinationId);
                        intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                        intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                        intent.putExtra(ExerciseActivity.CLASS_ID, classId);
                        intent.putExtra(ExerciseActivity.IS_EXAM_TEST, isExam.get());
                        intent.putExtra(ExerciseActivity.IS_CLASS_EXERCISE, isClassExercise);
                        intent.putExtra(ExerciseActivity.IS_MODULE_EXERCISE, isModuleExercise);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    /**
     * 全部解析
     */
    public void onAllAnalysisClick(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.EXAMINATION_ID, examinationId);
        intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
        intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
        intent.putExtra(ExerciseActivity.CLASS_ID, classId);
        intent.putExtra(ExerciseActivity.IS_ANSWER_ANALYSIS, true);
        startActivity(intent);
    }

    /**
     * 错题解析
     */
    public void onFaultsAnalysisClick(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.EXAMINATION_ID, examinationId);
        intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
        intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
        intent.putExtra(ExerciseActivity.CLASS_ID, classId);
        intent.putExtra(ExerciseActivity.IS_ANSWER_ANALYSIS, true);
        intent.putExtra(ExerciseActivity.ONLY_FAULT_ANALYSIS, true);
        intent.putExtra(ExerciseActivity.FAULT_QUESTION_STEM, (Serializable) faultQuestionStemList);
        startActivity(intent);
    }
}
