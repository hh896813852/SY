package com.edusoho.yunketang.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.BaseRecycleAdapter;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.adapter.SuperViewHolder;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.GridItemDecoration;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.databinding.ActivityMyBoughtExamBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.ui.testlib.AnswerReportActivity;
import com.edusoho.yunketang.ui.testlib.PastExamActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_my_bought_exam, title = "我购买的试卷")
public class MyBoughtExamActivity extends BaseActivity<ActivityMyBoughtExamBinding> {
    private int pageNo = 1;
    private boolean isLoading;
    private boolean canLoadMore;

    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            pageNo = 1;
            getDataBinding().swipeView.setRefreshing(true);
            loadData();
        }
    };

    private List<Examination> list = new ArrayList<>();
    public BaseRecycleAdapter<Examination> adapter = new BaseRecycleAdapter<Examination>() {

        @Override
        public void onBindViewHolder(@NonNull SuperViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            // 状态按钮
            holder.getView(R.id.statusView).setOnClickListener(v -> {
                if (list.get(position).finishState == 2) { // 已完成，去答题报告
                    Intent intent = new Intent(MyBoughtExamActivity.this, AnswerReportActivity.class);
                    intent.putExtra(AnswerReportActivity.HOMEWORK_ID, list.get(position).homeworkId);
                    intent.putExtra(AnswerReportActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(AnswerReportActivity.MODULE_ID, list.get(position).moduleId);
                    intent.putExtra(AnswerReportActivity.IS_EXAM, true);
                    EducationCourse selectedCourse = new EducationCourse();
                    selectedCourse.businessId = list.get(position).businessType;
                    selectedCourse.levelId = list.get(position).levelId;
                    selectedCourse.courseId = list.get(position).courseId;
                    intent.putExtra(AnswerReportActivity.SELECTED_COURSE, selectedCourse);
                    startActivity(intent);
                } else { // 开始（此处无继续状态）
                    Intent intent = new Intent(MyBoughtExamActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.EXAMINATION_MINUTE, list.get(position).sumMinute);
                    EducationCourse selectedCourse = new EducationCourse();
                    selectedCourse.businessId = list.get(position).businessType;
                    selectedCourse.levelId = list.get(position).levelId;
                    selectedCourse.courseId = list.get(position).courseId;
                    intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                    intent.putExtra(ExerciseActivity.MODULE_ID, list.get(position).moduleId);
                    intent.putExtra(ExerciseActivity.IS_EXAM_TEST, true);
                    startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                }
            });
        }
    };
    public BaseRecycleAdapter.OnItemClickListener onItemClick = (view, position) -> {
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                pageNo = 1;
                loadData();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_past_exam, list);
        initView();
        loadData();
    }

    private void initView() {
        // 设置布局管理器
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        getDataBinding().recycleView.setLayoutManager(layoutManager);
        // 设置item间距
        int verticalSpace = DensityUtil.dip2px(this, 10);
        int horizontalSpace = DensityUtil.dip2px(this, 10);
        getDataBinding().recycleView.addItemDecoration(new GridItemDecoration(verticalSpace, horizontalSpace));
        // 初始化适配器
        adapter.init(this, R.layout.item_past_exam, list);
        // 设置适配器
        getDataBinding().recycleView.setAdapter(adapter);
        getDataBinding().recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) getDataBinding().recycleView.getLayoutManager();
                // 当前第一个完全见的position
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    // 滑到顶部了
                }
                // 当前最后完全可见的position
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (canLoadMore && !isLoading && lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    // 滑到底部了
                    pageNo++;
                    isLoading = true;
                    loadData();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().getUser() != null);
    }

    private void loadData() {
        if (SYApplication.getInstance().getUser() == null) {
            return;
        }
        SYDataTransport.create(SYConstants.MY_BOUGHT_EXAM)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .addProgressing(list.size() == 0 && !getDataBinding().swipeView.isRefreshing(), this, "正在加载试卷列表...")
                .execute(new SYDataListener<List<Examination>>() {

                    @Override
                    public void onSuccess(List<Examination> data) {
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        hasData.set(list.size() > 0);
                        adapter.setDataList(list);
                        canLoadMore = data.size() == SYConstants.PAGE_SIZE;
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        isLoading = false;
                        canLoadMore = false;
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                        }
                    }
                }, new TypeToken<List<Examination>>() {
                });
    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
    };
}
