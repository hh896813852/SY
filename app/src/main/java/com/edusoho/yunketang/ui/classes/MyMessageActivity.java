package com.edusoho.yunketang.ui.classes;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.FaultRecord;
import com.edusoho.yunketang.bean.MsgInfo;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.databinding.ActivityMyMessageBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_my_message, title = "我的消息")
public class MyMessageActivity extends BaseActivity<ActivityMyMessageBinding> {
    private int pageNo = 1;
    private boolean isLoading;

    public ObservableField<Boolean> hasData = new ObservableField<>(false);
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);

    private int clickItemPosition;
    private List<MsgInfo> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        clickItemPosition = position;
        Intent intent = new Intent(MyMessageActivity.this, ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.IS_TEACHER_NOTATION, true);
        intent.putExtra(ExerciseActivity.IS_ANSWER_ANALYSIS, true);
        intent.putExtra(ExerciseActivity.TEACHER, list.get(position).teacherName);
        intent.putExtra(ExerciseActivity.MESSAGE_ID, list.get(position).id);
        startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
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
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                pageNo = 1;
                loadData();
            }
        }
        if (requestCode == ExerciseActivity.FROM_EXERCISE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                list.get(clickItemPosition).readStatus = 1;
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_my_message, list);
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

    @Override
    protected void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().isLogin());
    }

    /**
     * 加载数据
     */
    private void loadData() {
        User loginUser = SYApplication.getInstance().getUser();
        SYDataTransport.create(SYConstants.MY_MESSAGE)
                .addParam("studentId", loginUser.syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<MsgInfo>>() {

                    @Override
                    public void onSuccess(List<MsgInfo> data) {
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        hasData.set(list.size() > 0);
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
                        isLoading = false;
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(false);
                        }
                    }
                }, new TypeToken<List<MsgInfo>>() {
                });
    }

    /**
     * 登录
     */
    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
    }
}
