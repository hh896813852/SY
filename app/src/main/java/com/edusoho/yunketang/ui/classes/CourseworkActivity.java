package com.edusoho.yunketang.ui.classes;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.bean.ClassInfo;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.bean.Rank;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.databinding.ActivityCourseworkBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.ui.testlib.AnswerReportActivity;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Translucent
@Layout(value = R.layout.activity_coursework, title = "课程作业")
public class CourseworkActivity extends BaseActivity<ActivityCourseworkBinding> {
    public static final String CLASS_INFO = "class_info";
    private ClassInfo classInfo;

    private boolean isLoading;
    private int pageNo = 1;

    public ObservableField<String> classRank = new ObservableField<>();
    public ObservableField<String> correctPercent = new ObservableField<>();
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private List<Examination> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            ViewUtils.setProgressBarAnim(progressBar, list.get(position).getProgressMax(), list.get(position).getCurrentProgress(), 1000);
            // 已完成
            view.findViewById(R.id.finishView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(CourseworkActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                } else {
                    Intent intent = new Intent(CourseworkActivity.this, AnswerReportActivity.class);
                    intent.putExtra(AnswerReportActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(AnswerReportActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(AnswerReportActivity.MODULE_ID, list.get(position).moduleId);
                    intent.putExtra(AnswerReportActivity.CLASS_ID, list.get(position).classId);
                    intent.putExtra(AnswerReportActivity.IS_CLASS_EXERCISE, true);
                    EducationCourse selectedCourse = new EducationCourse();
                    selectedCourse.businessId = list.get(position).businessType;
                    selectedCourse.levelId = list.get(position).levelId;
                    selectedCourse.courseId = list.get(position).courseId;
                    intent.putExtra(AnswerReportActivity.SELECTED_COURSE, selectedCourse);
                    startActivityForResult(intent, AnswerReportActivity.FROM_REPORT_REQUEST_CODE);
                }
            });
            // 继续
            view.findViewById(R.id.continueView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(CourseworkActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                } else {
                    Intent intent = new Intent(CourseworkActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.CLASS_ID, list.get(position).classId);
                    intent.putExtra(ExerciseActivity.BUSINESS_ID, classInfo.businessType);
                    intent.putExtra(ExerciseActivity.LEVEL_ID, list.get(position).levelId);
                    intent.putExtra(ExerciseActivity.COURSE_ID, list.get(position).courseId);
                    intent.putExtra(ExerciseActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(ExerciseActivity.LAST_PAGE_INDEX, list.get(position).androidIndex);
                    intent.putExtra(ExerciseActivity.IS_CLASS_EXERCISE, true);
                    EducationCourse selectedCourse = new EducationCourse();
                    selectedCourse.businessId = list.get(position).businessType;
                    selectedCourse.levelId = list.get(position).levelId;
                    selectedCourse.courseId = list.get(position).courseId;
                    intent.putExtra(AnswerReportActivity.SELECTED_COURSE, selectedCourse);
                    startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                }
            });
            // 开始
            view.findViewById(R.id.startView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(CourseworkActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                } else {
                    Intent intent = new Intent(CourseworkActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.CLASS_ID, list.get(position).classId);
                    intent.putExtra(ExerciseActivity.BUSINESS_ID, classInfo.businessType);
                    intent.putExtra(ExerciseActivity.LEVEL_ID, list.get(position).levelId);
                    intent.putExtra(ExerciseActivity.COURSE_ID, list.get(position).courseId);
                    intent.putExtra(ExerciseActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(ExerciseActivity.IS_CLASS_EXERCISE, true);
                    EducationCourse selectedCourse = new EducationCourse();
                    selectedCourse.businessId = list.get(position).businessType;
                    selectedCourse.levelId = list.get(position).levelId;
                    selectedCourse.courseId = list.get(position).courseId;
                    intent.putExtra(AnswerReportActivity.SELECTED_COURSE, selectedCourse);
                    startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                }
            });
            return view;
        }
    };

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null && !isLoading) {
            pageNo = 1;
            loadData();
            // 加载班级排行
            loadClassRank();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExerciseActivity.FROM_EXERCISE_CODE) {
            onRefreshListener.onRefresh();
            // 加载班级排行
            loadClassRank();
        }
        if (requestCode == AnswerReportActivity.FROM_REPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onRefreshListener.onRefresh();
            // 加载班级排行
            loadClassRank();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classInfo = (ClassInfo) getIntent().getSerializableExtra(CLASS_INFO);
        adapter.init(this, R.layout.item_work, list);
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !getDataBinding().swipeView.isRefreshing()) {
                pageNo++;
                isLoading = true;
                loadData();
            }
        });
        loadData();
        // 加载班级排行
        loadClassRank();
    }

    /**
     * 加载班级排行
     */
    private void loadClassRank() {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser != null && loginUser.syjyUser != null) {
            SYDataTransport.create(SYConstants.CLASS_RANK)
                    .addParam("userId", loginUser.syjyUser.id)
                    .addParam("businessType", classInfo.businessType)
                    .addParam("classId", classInfo.id)
                    .execute(new SYDataListener<Rank>() {

                        @Override
                        public void onSuccess(Rank data) {
                            classRank.set(TextUtils.isEmpty(data.classRank) ? "0" : data.classRank);
                            correctPercent.set(data.percent == 0 ? "0" : String.valueOf(data.percent));
                        }
                    }, Rank.class);
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null || loginUser.syjyUser == null) {
            return;
        }
        SYDataTransport.create(SYConstants.CLASS_HOMEWORK)
                .addParam("userId", loginUser.syjyUser.id)
                .addParam("businessType", classInfo.businessType)
                .addParam("classId", classInfo.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .addProgressing(list.size() == 0 && !getDataBinding().swipeView.isRefreshing(), this, "正在加载课程作业列表...")
                .execute(new SYDataListener<List<Examination>>() {
                    @Override
                    public void onSuccess(List<Examination> data) {
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                        hasData.set(list.size() > 0);
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(data.size() == SYConstants.PAGE_SIZE);
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        isLoading = false;
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(false);
                        }
                    }
                }, new TypeToken<List<Examination>>() {
                });
    }
}
