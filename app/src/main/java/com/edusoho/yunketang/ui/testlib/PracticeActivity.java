package com.edusoho.yunketang.ui.testlib;

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
    public static final String SELECTED_COURSE = "selected_course";
    private int moduleId;
    private EducationCourse selectedCourse;

    private int pageNo = 1;
    private boolean isLoading;

    public ObservableField<Integer> searchType = new ObservableField<>(0);
    public ObservableField<Boolean> isShowMenu = new ObservableField<>(false);

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
            // 继续
            view.findViewById(R.id.continueView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PracticeActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                }
            });
            //　开始
            view.findViewById(R.id.startView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) {
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PracticeActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                } else {
                    Intent intent = new Intent(PracticeActivity.this, ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                    intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                    intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                    startActivity(intent);
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
            getDataBinding().listView.setCanLoadMore(true);
            loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
        adapter.init(this, R.layout.item_exercise, list);
        layoutInflater = LayoutInflater.from(this);

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

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(SYConstants.MODULE_EXERCISE)
                .addParam("businessType", selectedCourse.businessId)
                .addParam("levelId", selectedCourse.levelId)
                .addParam("courseId", selectedCourse.courseId)
                .addParam("moduleId", moduleId)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<Examination>>() {

                    @Override
                    public void onSuccess(List<Examination> data) {
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
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
        searchType.set(type);
        isShowMenu.set(false);
    }
}
