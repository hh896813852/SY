package com.edusoho.yunketang.ui.testlib;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.bean.FaultRecord;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.BusinessModule;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.databinding.ActivityMyFaultsBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.login.LoginActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_my_faults, title = "错题记录")
public class MyFaultsActivity extends BaseActivity<ActivityMyFaultsBinding> {
    private int pageNo;
    private boolean isLoading;

    public ObservableField<Boolean> hasFaults = new ObservableField<>(true);
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);

    private List<FaultRecord> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            view.findViewById(R.id.wipeView).setOnClickListener(v -> {
                Intent intent = new Intent(MyFaultsActivity.this, ExerciseActivity.class);
                intent.putExtra(ExerciseActivity.IS_MY_FAULTS, true);
                intent.putExtra(ExerciseActivity.HOMEWORK_ID, list.get(position).homeworkId);
                startActivity(intent);
            });
            return view;
        }
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            pageNo = 0;
            getDataBinding().swipeView.setRefreshing(true);
            loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_my_faults, list);
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !getDataBinding().swipeView.isRefreshing()) {
                pageNo++;
                isLoading = true;
                loadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().isLogin());
        if (isLogin.get()) {
            loadData();
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        User loginUser = SYApplication.getInstance().getUser();
        SYDataTransport.create(SYConstants.MY_FAULTS)
                .addParam("userId", loginUser.syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<FaultRecord>>() {

                    @Override
                    public void onSuccess(List<FaultRecord> data) {
                        isLoading = false;
                        if (pageNo == 0) {
                            list.clear();
                        }
                        list.addAll(data);
                        hasFaults.set(list.size() > 0);
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
                }, new TypeToken<List<FaultRecord>>() {
                });
    }

    /**
     * 登录
     */
    public void onLoginClick(View view) {
        startActivity(LoginActivity.class);
    }
}
