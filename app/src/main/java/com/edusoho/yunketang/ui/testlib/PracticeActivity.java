package com.edusoho.yunketang.ui.testlib;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edusoho.yunketang.SYApplication;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.databinding.ActivityPracticeBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_practice, title = "章节练习", rightButtonRes = R.drawable.icon_menu)
public class PracticeActivity extends BaseActivity<ActivityPracticeBinding> {
    public static final String MODULE_ID = "module_id";
    public static final String TITLE_NAME = "title_name";
    public static final String SELECTED_COURSE = "selected_course";
    private int moduleId;
    private EducationCourse selectedCourse;

    private int pageNo = 1;
    private boolean isLoading;

    public ObservableField<Integer> searchType = new ObservableField<>(3);
    public ObservableField<Boolean> isShowMenu = new ObservableField<>(false);
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private LayoutInflater layoutInflater;
    private List<Examination> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            LinearLayout containerLayout = view.findViewById(R.id.container);
            containerLayout.removeAllViews();
            // 添加题型详情
            for (Examination.ExaminationInfo info : list.get(position).examinationInfo) {
                View innerView = layoutInflater.inflate(R.layout.item_inner_exercise, null);
                TextView questionTypeView = innerView.findViewById(R.id.questionTypeView);
                TextView questionInfoView = innerView.findViewById(R.id.questionInfoView);
                questionTypeView.setText(info.questionTypeName);
                questionTypeView.setBackground(info.getQuestionTypeDrawable(PracticeActivity.this));
                questionInfoView.setText(info.getQuestionInfo());
                containerLayout.addView(innerView);
            }
            containerLayout.setVisibility(list.get(position).isShowDetail ? View.VISIBLE : View.GONE);
            // 已完成
            view.findViewById(R.id.finishView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PracticeActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> {
                        dialog.dismiss();
                        Intent intent = new Intent(PracticeActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
                    });
                } else {
                    Intent intent = new Intent(PracticeActivity.this, AnswerReportActivity.class);
                    intent.putExtra(AnswerReportActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(AnswerReportActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(AnswerReportActivity.MODULE_ID, moduleId);
                    intent.putExtra(ExerciseActivity.IS_MODULE_EXERCISE, true);
                    intent.putExtra(AnswerReportActivity.SELECTED_COURSE, selectedCourse);
                    startActivityForResult(intent, AnswerReportActivity.FROM_REPORT_REQUEST_CODE);
                }
            });
            // 继续
            view.findViewById(R.id.continueView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PracticeActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> {
                        dialog.dismiss();
                        Intent intent = new Intent(PracticeActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
                    });
                } else {
                    Intent intent = new Intent(PracticeActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                    intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                    intent.putExtra(ExerciseActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(ExerciseActivity.LAST_PAGE_INDEX, list.get(position).lastPageIndex);
                    intent.putExtra(ExerciseActivity.IS_MODULE_EXERCISE, true);
                    startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                }
            });
            // 开始
            view.findViewById(R.id.startView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PracticeActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> {
                        dialog.dismiss();
                        Intent intent = new Intent(PracticeActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
                    });
                } else {
                    Intent intent = new Intent(PracticeActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                    intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                    intent.putExtra(ExerciseActivity.IS_MODULE_EXERCISE, true);
                    startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                }
            });
            return view;
        }
    };
    // item点击，是否显示题型详情
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        list.get(position).isShowDetail = !list.get(position).isShowDetail;
        adapter.notifyDataSetChanged();
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            pageNo = 1;
            getDataBinding().swipeView.setRefreshing(true);
            loadData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExerciseActivity.FROM_EXERCISE_CODE) {
            onRefreshListener.onRefresh();
        }
        if (requestCode == AnswerReportActivity.FROM_REPORT_REQUEST_CODE) {
            onRefreshListener.onRefresh();
        }
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            isLogin.set(SYApplication.getInstance().isLogin());
            onRefreshListener.onRefresh();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !getDataBinding().swipeView.isRefreshing()) {
                pageNo++;
                isLoading = true;
                loadData();
            }
        });
        loadData();
    }

    private void initView() {
        setTitleView(getIntent().getStringExtra(TITLE_NAME));
        isLogin.set(SYApplication.getInstance().isLogin());
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
        adapter.init(this, R.layout.item_exercise, list);
        layoutInflater = LayoutInflater.from(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().isLogin());
        if (!isLogin.get()) {
            isShowMenu.set(false);
        }
        rightButtonImageView.setVisibility(isLogin.get() ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport dataTransport = SYDataTransport.create(isLogin.get() ? SYConstants.MODULE_EXERCISE : SYConstants.MODULE_EXERCISE_NOT_LOGIN);
        if (isLogin.get()) {
            dataTransport.addParam("finishState", searchType.get())
                    .addParam("userId", getLoginUser().syjyUser.id);
        }
        dataTransport.addParam("businessType", selectedCourse.businessId)
                .addParam("levelId", selectedCourse.levelId)
                .addParam("courseId", selectedCourse.courseId)
                .addParam("moduleId", moduleId)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .addProgressing(list.size() == 0 && !getDataBinding().swipeView.isRefreshing(), this, "正在加载习题列表...")
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
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(false);
                        }
                    }
                }, new TypeToken<List<Examination>>() {
                });
    }

    @Override
    public void onRightButtonClick() {
        isShowMenu.set(!isShowMenu.get());
    }

    /**
     * 章节练习搜索类型点击
     */
    public void onSearchTypeClick(View view) {
        int type = Integer.valueOf(view.getTag().toString());
        isShowMenu.set(false);
        if (type != searchType.get()) {
            searchType.set(type);
            pageNo = 1;
            loadData();
        }
    }


    public void onBgClick(View view) {
        isShowMenu.set(false);
    }
}
